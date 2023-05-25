package spring.boot.oath2.scrabdatas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.boot.oath2.scrabdatas.entity.StockHistEntity;
import spring.boot.oath2.scrabdatas.entity.pk.StockHistEntityPk;
import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.persistent.StockHistRepo;
import spring.boot.oath2.scrabdatas.request.HistStockRequest;
import spring.boot.oath2.scrabdatas.service.HistStockDataService;
@Service
public class HistStockDataServiceImpl implements HistStockDataService{

	@Autowired
	private StockHistRepo stockHistRepo;
	
	@Override
	public List<StockHistModel> selectHists() {
		return null;
	}

	@Override
	public List<StockHistModel> selectHists(String stockCode,String date) {
		Optional<StockHistEntity> result=stockHistRepo.findById(new StockHistEntityPk(stockCode,date));
		List<StockHistModel> respList=new ArrayList<StockHistModel>();
		StockHistModel respModel=new StockHistModel();
		if(!result.isEmpty()) {
			System.out.println(">>>"+result.get());
			BeanUtils.copyProperties(result.get(), respModel);
			respList.add(respModel);
		}
		return respList;
	}

	@Override
	public List<StockHistModel> selectHists(String stockCode, String startDt, String endDt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StockHistModel> selectHists(HistStockRequest histStockRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
