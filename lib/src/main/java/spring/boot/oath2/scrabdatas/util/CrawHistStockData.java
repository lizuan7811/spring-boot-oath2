package spring.boot.oath2.scrabdatas.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import spring.boot.oath2.scrabdatas.entity.StockHistEntity;
import spring.boot.oath2.scrabdatas.model.HistJsonModel;
import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.persistent.StockHistRepo;
import spring.boot.oath2.scrabdatas.property.ScrawProperty;

/**
 * 爬Stock 歷史資料Tool
 */
@Component
@Slf4j
public class CrawHistStockData {
//  歷史資料
//	https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20220101&stockNo=0050
//	即時資料
//	https://mis.twse.com.tw/stock/api/getStockInfo.jsp?json=1&delay=0&ex_ch=tse_2330.tw%7Ctse_0050.tw%7C
	// private static String STOCK_HIST_FQDN =
	// "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=${DATE}&stockNo=${STOCKNO}";
	// private final String STOCK_PURE_CODE_FILE =
	// "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\stockpurecode.txt";
	// private final String STOCK_HISTDATA_FILE =
	// "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\stockhistdata.txt";

	private final List<StockHistEntity> histEntityList = new ArrayList<StockHistEntity>();

	@Autowired
	private StockHistRepo stockHistRepo;
	@Autowired
	private ScrawProperty scrawProperty;

	/**
	 * 查詢歷史資料方法(開始位置)
	 */
	public void startScrawHistData(boolean saveToDb, boolean isHist) {
		if (saveToDb) {
			startScrawHistToDb(getParseDataFunc(), isHist);
		} else {
			NormalUtils.ifFileNotExitThenCreate(scrawProperty.getStockHistdataFile());
			startScrawHistToFile(scrawProperty.getStockHistdataFile(), getParseDataFunc(), isHist);
		}
	}

	/**
	 * 取得要解析Data的Function
	 */
	public Function<String, HistJsonModel> getParseDataFunc() {
		return new Function<String, HistJsonModel>() {
			@Override
			public HistJsonModel apply(String t) {
				StringBuilder sb = new StringBuilder();
				try {
					HttpsURLConnection connection = ConnectionFactory.getConnectionInst(new URL(t));
					BufferedReader reader = ConnectionFactory.getBufferReader(connection);
					String rdLine = Strings.EMPTY;
					while (StringUtils.isNotBlank(rdLine = reader.readLine())) {
						System.out.println(rdLine);
						if (rdLine.indexOf("\"total\":0") == -1) {
							sb.append(rdLine);
							sb.append(System.lineSeparator());
						} else {
							System.out.println("else: " + rdLine);
						}
					}
					ConnectionFactory.disConnection();
				} catch (IOException e) {
					log.debug(">>> getParseDataFunc IOException: {} ", e.getMessage());
				}
				return (HistJsonModel) NormalUtils.parseJson(sb.toString(), HistJsonModel.class);
			}
		};
	}

	/**
	 * 取得已經解析過的Data的Function
	 */
	public Function<HistJsonModel, List<Object>> getParsedDataFunc(String code) {
		return new Function<HistJsonModel, List<Object>>() {
			@Override
			public List<Object> apply(HistJsonModel histJsonModel) {

				List<Object> modelList = new ArrayList<Object>();
				if (Objects.nonNull(histJsonModel)) {
					try {
						Field field = histJsonModel.getClass().getDeclaredField("data");
						ReflectionUtils.makeAccessible(field);
						List<List<String>> dataList = (List<List<String>>) field.get(histJsonModel);

						dataList.stream().forEach(innerDataList -> {
							StockHistModel stockHistModel = (StockHistModel) NormalUtils
									.transToObject(StockHistModel.class, innerDataList);
							stockHistModel.setStockCode(code);
							modelList.add(stockHistModel);
						});
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
							| IllegalAccessException e) {
						log.debug(">>> getParsedDataFunc Exceptions: {} ", e.getMessage());
					}
				}
				return modelList;
			}
		};
	}

	/**
	 * 產生查詢使用的網址
	 */
	private List<String> buildQuoteUrl(String code, boolean scrawHist) {
		List<String> scrawList = new ArrayList<String>();
		LocalDateTime curT = LocalDateTime.now().minusDays(1);
//		判斷是否抓歷史資料
		int startYear=scrawProperty.getStartYear();
		System.out.println(startYear);
		if (scrawHist) {
			boolean stopFlag = false;
			for (int j = 0; j <= 10; j++) {
				int year = startYear + j;
				for (int month = 1; month <= 12; month++) {
					LocalDateTime ldt = LocalDateTime.of(year, month, 1, 0, 0, 0);
					if (ldt.isBefore(curT)) {
						String ldtStr = ldt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
						scrawList.add(scrawProperty.getStockHistFqdn().replace("${DATE}", ldtStr).replace("${STOCKNO}", code));
					} else {
						stopFlag = true;
						break;
					}
				}
				if (stopFlag)
					break;
			}
		} else {
//			若非抓歷史資料，那就是抓前一日資料
			scrawList.add(scrawProperty.getStockHistFqdn()
					.replace("${DATE}", curT.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
					.replace("${STOCKNO}", code));
		}
		return scrawList;
	}

	/**
	 * 讀歷史資料存資料庫
	 */
	private <T> void startScrawHistToDb(Function<String, T> parseJsonFunc, boolean isHist) {
		try {
			System.out.println(scrawProperty.getStockPureCodeFile());
			List<String> codeList = Files.readAllLines(Paths.get(scrawProperty.getStockPureCodeFile()));
			
			codeList.stream().filter(cd -> Integer.valueOf(cd) >= Integer.valueOf("1737")).forEach(code -> {
				buildQuoteUrl(code, isHist).stream().forEach(url -> {
					try {
						System.out.println(url);
						log.debug(">>> url: {} ", url);
						Thread.sleep(ConnectionFactory.randMill(scrawProperty.getBaseRandTime(),scrawProperty.getRandTime()));
						T histJsonModel = (T) parseJsonFunc.apply(url);
						List<Object> modelList = (List<Object>) (getParsedDataFunc(code)
								.apply((HistJsonModel) histJsonModel));
//						若model不為空，就轉換成Entity
						if (!modelList.isEmpty()) {
							modelList.stream().forEach(model -> {
								System.out.println(">>>~~~model:\t" + model.toString());
								consumeAndSaveToDb(model);
							});
						}
					} catch (InterruptedException | SecurityException | IllegalArgumentException e) {
						log.debug(">>> startScrawHistToDb Exceptions: {} ", e.getMessage());
					}
				});
			});
			
		} catch (IOException e) {
			log.debug(">>> startScrawHistToDb IOException: {} ", e.getMessage());
		}finally {
			flushListToDb();
		}
	}

	private void flushListToDb() {
		if (histEntityList.size() != 0) {
			stockHistRepo.saveAll(histEntityList);
			histEntityList.clear();
		}
	}

	/**
	 * 達1000筆資料儲存一次DB
	 */
	private void consumeAndSaveToDb(Object model) {
		StockHistEntity target = new StockHistEntity();
		BeanUtils.copyProperties(model, target);
		histEntityList.add(target);
		if (histEntityList.size() == 1000) {
			stockHistRepo.saveAll(histEntityList);
			histEntityList.clear();
		}
	}

	/**
	 * 讀歷史資料存實體檔
	 */
	private String startScrawHistToFile(String fileName, Function<String, HistJsonModel> function, boolean isHist) {
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND)) {
			List<String> codeList = Files.readAllLines(Paths.get(scrawProperty.getStockPureCodeFile()));
			codeList.stream().forEach(code -> {
				buildQuoteUrl(code, isHist).stream().forEach(url -> {
					try {
						Thread.sleep(ConnectionFactory.randMill(scrawProperty.getBaseRandTime(),scrawProperty.getRandTime()));
						bw.write(function.apply(url).toString());
						bw.flush();
						System.out.println("===========");

					} catch (IOException | InterruptedException e) {
						log.debug(">>> startScrawHistToFile Exceptions: {} ", e.getMessage());
					}
				});
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
