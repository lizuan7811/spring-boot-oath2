package spring.boot.oath2.scrabdatas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.boot.oath2.scrabdatas.entity.StockHistEntity;
import spring.boot.oath2.scrabdatas.entity.pk.StockHistEntityPk;
import spring.boot.oath2.scrabdatas.exception.FindHistDataException;
import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.persistent.SelectType;
import spring.boot.oath2.scrabdatas.persistent.StockHistRepo;
import spring.boot.oath2.scrabdatas.request.HistStockRequest;
import spring.boot.oath2.scrabdatas.service.HistStockDataService;

@Service
public class HistStockDataServiceImpl implements HistStockDataService {

	@Autowired
	private StockHistRepo stockHistRepo;

	@Override
	public List<StockHistModel> selectHists(@Valid HistStockRequest histStockRequest) throws FindHistDataException {
		List<StockHistModel> respList = new ArrayList<StockHistModel>();
		String selectStr = histStockRequest.getSelectType();

		SelectType selectType = SelectType.getSelectType(selectStr);
		String stockCode=histStockRequest.getStockCode();
		String startDt=histStockRequest.getStartDt();
		String endDt=histStockRequest.getEndDt();
		
		switch (selectType) {
//			查一支一天
			case STKCODE_ONEDAY:
				if(StringUtils.isBlank(stockCode)) {
					throw new FindHistDataException("Stock code is blank");
				}
				respList=findStkOneADay(histStockRequest.getStockCode(),startDt);
				break;
//			查一支多天
			case STKCODE_MOREDAY:
				respList=findStkMoreDay(histStockRequest.getStockCode(), histStockRequest.getStartDt(),histStockRequest.getEndDt());
				break;
//			查一支所有天
			case STKCODE_ALL:
				respList=findStkAll(histStockRequest.getStockCode());
				break;
//			所有支一天
			case ALLCODE_ONEDAY:
				if(!startDt.equals(endDt)) {
//					throw new FindHistDataException("");
				}
				respList=findStkallOneDay(histStockRequest.getStartDt());
			    break;
		}
		return respList;
	}

	/**
	 * 查one code a day
	 */
	private List<StockHistModel> findStkOneADay(String stockCode, String aDt) {
		Optional<StockHistEntity> result = stockHistRepo.findById(new StockHistEntityPk(stockCode, aDt));
		List<StockHistModel> respList = new ArrayList<StockHistModel>();
		StockHistModel respModel = new StockHistModel();
		if (!result.isEmpty()) {
			System.out.println(">>>" + result.get());
			BeanUtils.copyProperties(result.get(), respModel);
			respList.add(respModel);
		}
		return respList;
	}
	
	/**
	 * 查one code range day
	 */
	private List<StockHistModel> findStkMoreDay(String stockCode, String startDt,String endDt) {
		List<StockHistModel> respList = new ArrayList<StockHistModel>();
		return respList;
	}

	private List<StockHistModel> findStkallOneDay(String aDt){
		List<StockHistModel> respList = new ArrayList<StockHistModel>();
		return respList;
	}
	
	private List<StockHistModel> findStkAll(String stockCode){
		List<StockHistModel> respList = new ArrayList<StockHistModel>();
		return respList;
	}

	
}
