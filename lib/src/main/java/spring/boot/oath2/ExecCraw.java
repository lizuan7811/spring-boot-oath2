package spring.boot.oath2;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spring.boot.oath2.scrabdatas.model.HistJsonModel;
import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.model.StockModel;
import spring.boot.oath2.scrabdatas.util.ConnectionFactory;
import spring.boot.oath2.scrabdatas.util.CrawHistStockData;
import spring.boot.oath2.scrabdatas.util.CrawingStockDatas;
import spring.boot.oath2.scrabdatas.util.NormalUtils;

@SpringBootApplication(scanBasePackages="spring.boot.oath2.scrabdatas")
public class ExecCraw {
	public static void main(String[] args) {
		
		System.setProperty("http.proxyHost", "159.223.102.4");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("https.proxyHost", "159.223.102.4");
		System.setProperty("https.proxyPort", "8080");
		SpringApplication.run(ExecCraw.class, args);
//		CrawingStockDatas scrab=new CrawingStockDatas(true);
//		scrab.initStockCodeFile();
//		scrab.jsoupPrint();
//		scrab.getURL();
//		scrab.parseHtml();
//		CrawHistStockData crawHistStockData=new CrawHistStockData();
//		crawHistStockData.startScrawHistData();
		
//		String jsonString="{\"stat\":\"OK\",\"date\":\"20190601\",\"title\":\"108年06月 1102 亞泥             各日成交資訊\",\"fields\":[\"日期\",\"成交股數\",\"成交金額\",\"開盤價\",\"最高價\",\"最低價\",\"收盤價\",\"漲跌價差\",\"成交筆數\"],\"data\":[[\"108/06/03\",\"8,447,524\",\"383,600,066\",\"45.00\",\"45.75\",\"44.40\",\"45.50\",\"+0.40\",\"4,697\"],[\"108/06/04\",\"8,100,557\",\"369,179,917\",\"45.80\",\"46.05\",\"45.30\",\"45.30\",\"-0.20\",\"3,875\"],[\"108/06/05\",\"7,397,972\",\"337,351,562\",\"45.50\",\"45.90\",\"45.40\",\"45.65\",\"+0.35\",\"4,187\"],[\"108/06/06\",\"7,837,392\",\"358,910,560\",\"45.50\",\"46.00\",\"45.40\",\"45.85\",\"+0.20\",\"4,521\"],[\"108/06/10\",\"6,276,958\",\"287,568,913\",\"45.85\",\"46.00\",\"45.60\",\"45.90\",\"+0.05\",\"3,800\"],[\"108/06/11\",\"10,459,321\",\"485,332,452\",\"45.90\",\"46.75\",\"45.90\",\"46.75\",\"+0.85\",\"4,576\"],[\"108/06/12\",\"6,919,221\",\"322,687,018\",\"46.50\",\"46.95\",\"46.20\",\"46.75\",\" 0.00\",\"3,399\"],[\"108/06/13\",\"8,768,569\",\"404,987,754\",\"46.50\",\"46.70\",\"45.80\",\"46.15\",\"-0.60\",\"3,594\"],[\"108/06/14\",\"6,032,211\",\"278,886,776\",\"45.80\",\"46.40\",\"45.80\",\"46.35\",\"+0.20\",\"3,344\"],[\"108/06/17\",\"4,284,109\",\"200,190,160\",\"46.20\",\"47.00\",\"46.10\",\"46.75\",\"+0.40\",\"2,258\"],[\"108/06/18\",\"6,689,189\",\"313,202,124\",\"46.80\",\"47.00\",\"46.60\",\"47.00\",\"+0.25\",\"3,822\"],[\"108/06/19\",\"7,811,394\",\"368,043,956\",\"47.00\",\"47.40\",\"46.80\",\"47.40\",\"+0.40\",\"4,391\"],[\"108/06/20\",\"7,078,176\",\"336,888,582\",\"47.50\",\"48.00\",\"47.20\",\"47.65\",\"+0.25\",\"3,837\"],[\"108/06/21\",\"10,972,177\",\"516,517,529\",\"47.65\",\"47.80\",\"46.65\",\"46.65\",\"-1.00\",\"3,708\"],[\"108/06/24\",\"7,433,617\",\"350,480,285\",\"46.80\",\"47.50\",\"46.70\",\"47.45\",\"+0.80\",\"3,307\"],[\"108/06/25\",\"4,932,850\",\"231,796,026\",\"47.05\",\"47.30\",\"46.80\",\"47.30\",\"-0.15\",\"3,308\"],[\"108/06/26\",\"5,042,827\",\"238,643,481\",\"47.40\",\"47.55\",\"47.10\",\"47.35\",\"+0.05\",\"2,518\"],[\"108/06/27\",\"13,640,617\",\"659,340,299\",\"47.50\",\"48.90\",\"47.45\",\"48.90\",\"+1.55\",\"5,393\"],[\"108/06/28\",\"10,677,565\",\"511,680,844\",\"48.35\",\"48.50\",\"47.55\",\"47.55\",\"-1.35\",\"4,107\"]],\"notes\":[\"符號說明:+/-/X表示漲/跌/不比價\",\"當日統計資訊含一般、零股、盤後定價、鉅額交易，不含拍賣、標購。\",\"ETF證券代號第六碼為K、M、S、C者，表示該ETF以外幣交易。\"],\"total\":19}";
//		
//		ObjectMapper objMap=new ObjectMapper();
//		try {
//			HistJsonModel histJsonModel=objMap.readValue(jsonString, HistJsonModel.class);
//			System.out.println(histJsonModel.toString());
//			histJsonModel.getData().forEach(obj->{
//				System.out.println(NormalUtils.transToObject(StockHistModel.class,obj));
//			});
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
		
//		  159.223.102.4:8080
	
		
//		String testS="https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20100101&stockNo=2330";
//		try {
//			ConnectionFactory.getConnectionInst(new URL(testS));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

}
