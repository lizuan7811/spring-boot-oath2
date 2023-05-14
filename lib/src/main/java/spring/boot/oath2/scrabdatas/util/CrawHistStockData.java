package spring.boot.oath2.scrabdatas.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.logging.log4j.util.Strings;

import com.nimbusds.oauth2.sdk.util.StringUtils;

public class CrawHistStockData {

//	https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20220101&stockNo=0050
//	https://mis.twse.com.tw/stock/api/getStockInfo.jsp?json=1&delay=0&ex_ch=tse_2330.tw%7Ctse_0050.tw%7C
	private static String STOCK_HIST_FQDN = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=${DATE}&stockNo=${STOCKNO}";
	private final String STOCK_PURE_CODE_FILE = "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\stockpurecode.txt";
	private final String STOCK_HISTDATA_FILE = "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\stockhistdata.txt";
	private final String TEST = "C:\\Users\\Lizuan\\Desktop\\DevTest_dir\\test.txt";

	public void scrawingHistData() {
		try {
			List<String> stockCodeList = Files.readAllLines(Paths.get(STOCK_PURE_CODE_FILE));
			stockCodeList.stream().forEach(code -> {
				buildQuoteUrl(code);
			});
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public List<String> buildQuoteUrl(String code) {
		List<String> scrawList = new ArrayList<String>();
		int startYear = 2010;
		for (int j = 0; j <= 10; j++) {
			int year = startYear + j;
			for (int month = 1; month <= 12; month++) {
				String ldt = LocalDateTime.of(year, month, 1, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				scrawList.add(STOCK_HIST_FQDN.replace("${DATE}", ldt).replace("${STOCKNO}", code));
			}
		}
		return scrawList;
	}

	private String startScrawHistData(String fileName, Function<String, String> function) {
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND)) {
			List<String> codeList = Files.readAllLines(Paths.get(STOCK_PURE_CODE_FILE));
			codeList.stream().forEach(code -> {
				buildQuoteUrl(code).stream().forEach(url -> {
					try {
						Thread.sleep(ConnectionFactory.randMill());
						System.out.println(url);
						bw.write(function.apply(url));
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

	public void startScrawHistData() {
		Function<String, String> func = new Function<String, String>() {
			@Override
			public String apply(String t) {
				StringBuilder sb = new StringBuilder();
				try {
					HttpURLConnection connection = ConnectionFactory.getConnectionInst(new URL(t));
					BufferedReader reader = ConnectionFactory.getBufferReader(connection);
					String rdLine = Strings.EMPTY;
					while (StringUtils.isNotBlank(rdLine = reader.readLine())) {
						System.out.println(rdLine);
						if(rdLine.indexOf("\"total\":0")==-1) {
							sb.append(rdLine + System.lineSeparator());
						}
					}
					ConnectionFactory.disConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return sb.toString();
			}
		};
		ifFileNotExitThenCreate(STOCK_HISTDATA_FILE);
		startScrawHistData(STOCK_HISTDATA_FILE, func);
	}

	private boolean ifFileNotExitThenCreate(String fileName) {
		boolean isExist = Paths.get(fileName).toFile().exists();
		if (!isExist) {
			try {
				Paths.get(fileName).toFile().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isExist;
	}
	
}
