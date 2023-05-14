package spring.boot.oath2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import spring.boot.oath2.scrabdatas.util.CrawHistStockData;
import spring.boot.oath2.scrabdatas.util.CrawingStockDatas;

@SpringBootApplication(scanBasePackages="spring.boot.oath2.scrabdatas")
public class ExecCraw {
	public static void main(String[] args) {
//		SpringApplication.run(ExecCraw.class, args);
//		CrawingStockDatas scrab=new CrawingStockDatas();
//		scrab.initStockCodeFile();
//		scrab.jsoupPrint();
//		scrab.getURL();
//		scrab.parseHtml();
		CrawHistStockData crawHistStockData=new CrawHistStockData();
		crawHistStockData.startScrawHistData();
	}

}
