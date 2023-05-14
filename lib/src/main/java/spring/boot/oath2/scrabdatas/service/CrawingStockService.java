package spring.boot.oath2.scrabdatas.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import spring.boot.oath2.scrabdatas.model.StockModel;

public interface CrawingStockService {
	
	public List<String> quoteStockContents();
	
	public String quoteStockContentByCode(String stockCode);

	public List<Map<String,StockModel>> tranStockContent();
	
	public List<Map<String,StockModel>> addAndUpdateContent();
	
	public void updateContents();
	
	public void saveOneContents();
	
}
