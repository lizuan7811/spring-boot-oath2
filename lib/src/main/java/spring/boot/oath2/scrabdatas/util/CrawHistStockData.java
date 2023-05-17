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

import spring.boot.oath2.scrabdatas.entity.StockHistEntity;
import spring.boot.oath2.scrabdatas.model.HistJsonModel;
import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.persistent.StockHistRepo;

@Component
public class CrawHistStockData {

//	https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20220101&stockNo=0050
//	https://mis.twse.com.tw/stock/api/getStockInfo.jsp?json=1&delay=0&ex_ch=tse_2330.tw%7Ctse_0050.tw%7C
	private static String STOCK_HIST_FQDN = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=${DATE}&stockNo=${STOCKNO}";
	private final String STOCK_PURE_CODE_FILE = "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\stockpurecode.txt";
	private final String STOCK_HISTDATA_FILE = "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\stockhistdata.txt";
	private final String TEST = "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\test.txt";

	private final List<StockHistEntity> histEntityList = new ArrayList<StockHistEntity>();
//	private final List<StockHistModel> stockModelList = new ArrayList<StockHistModel>();

	@Autowired
	private StockHistRepo stockHistRepo;
	/**
	 * 查詢歷史資料方法(開始位置)
	 */
	public void startScrawHistData(boolean saveToDb) {
		if (saveToDb) {
			startScrawHistToDb(getParseDataFunc());
		} else {
			NormalUtils.ifFileNotExitThenCreate(STOCK_HISTDATA_FILE);
			startScrawHistToFile(STOCK_HISTDATA_FILE, getParseDataFunc());
		}
	}

	/**
	 * 取得要解析Data的Function
	 */
	public  Function<String, HistJsonModel> getParseDataFunc() {
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
						}
					}
					ConnectionFactory.disConnection();
				} catch (IOException e) {
					e.printStackTrace();
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
							StockHistModel stockHistModel = (StockHistModel)NormalUtils.transToObject(StockHistModel.class, innerDataList);
							stockHistModel.setStockCode(code);
							modelList.add(stockHistModel);
						});
// TODO從這裡開始。
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
							| IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return modelList;
			}
		};
	}
	
	/**
	 * 產生查詢使用的網址
	 */
	private List<String> buildQuoteUrl(String code) {
		Map<String, List<String>> quoteUrlMap = new HashMap<String, List<String>>();
		List<String> scrawList = new ArrayList<String>();
		int startYear = 2010;
		for (int j = 0; j <= 10; j++) {
			int year = startYear + j;
			for (int month = 1; month <= 12; month++) {
				String ldt = LocalDateTime.of(year, month, 1, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				scrawList.add(STOCK_HIST_FQDN.replace("${DATE}", ldt).replace("${STOCKNO}", code));
			}
		}
		quoteUrlMap.put(code, scrawList);
		return scrawList;
	}

	/**
	 * 讀歷史資料存資料庫
	 */
	private <E, T> void startScrawHistToDb(Function<String, T> parseJsonFunc) {
		try {
			List<String> codeList = Files.readAllLines(Paths.get(STOCK_PURE_CODE_FILE));
			codeList.stream().forEach(code -> {
				buildQuoteUrl(code).stream().forEach(url -> {
					try {
						System.out.println(url);
						Thread.sleep(ConnectionFactory.randMill());
						T histJsonModel = (T) parseJsonFunc.apply(url);
						List<Object> modelList =  (List<Object>) (getParsedDataFunc(code).apply((HistJsonModel)histJsonModel));
//						若model不為空，就轉換成Entity
						if (!modelList.isEmpty()) {
							modelList.stream().forEach(model -> {
								System.out.println(">>>~~~model:\t"+model.toString());
								consumeAndSaveToDb(model);
							});
						}
					} catch (InterruptedException | SecurityException | IllegalArgumentException e) {
						e.printStackTrace();
					}
				});
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 達1000筆資料儲存一次DB
	 */
	private void consumeAndSaveToDb(Object model){
		StockHistEntity target=new StockHistEntity();
		BeanUtils.copyProperties(model, target);
		histEntityList.add(target);
		if(histEntityList.size()==1000) {
			stockHistRepo.saveAll(histEntityList);
			histEntityList.clear();
		}
	}
	
	/**
	 * 讀歷史資料存實體檔
	 */
	private String startScrawHistToFile(String fileName, Function<String, HistJsonModel> function) {
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND)) {
			List<String> codeList = Files.readAllLines(Paths.get(STOCK_PURE_CODE_FILE));
			codeList.stream().forEach(code -> {
				buildQuoteUrl(code).stream().forEach(url -> {
					try {
						Thread.sleep(ConnectionFactory.randMill());
						bw.write(function.apply(url).toString());
						bw.flush();
						System.out.println("===========");

					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				});
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
