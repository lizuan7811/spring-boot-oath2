package spring.boot.oath2.scrabdatas.service;

import java.util.Date;
import java.util.List;

import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.request.HistStockRequest;

public interface HistStockDataService {
	
	public List<StockHistModel> selectHists();

	public List<StockHistModel> selectHists(String stockCode,String date);

	public List<StockHistModel> selectHists(String stockCode,String startDt,String endDt);

	public List<StockHistModel> selectHists(HistStockRequest histStockRequest);

}
