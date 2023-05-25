package spring.boot.oath2.scrabdatas.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.request.HistStockRequest;
import spring.boot.oath2.scrabdatas.response.HistStockResponse;
import spring.boot.oath2.scrabdatas.service.HistStockDataService;

@RestController
@RequestMapping(value = "/histstock")
public class HistStockDataController {

	private final HistStockDataService histStockDataService;

	@Autowired
	public HistStockDataController(HistStockDataService histStockDataService) {
		this.histStockDataService = histStockDataService;
	}

//	歷史資料在這個Controller中只能被查詢，不增加insert以及delete方法
	@PostMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object selectHists() {

		return null;
	}

	@PostMapping(value = "/stkcode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HistStockResponse selectHists(@RequestBody HistStockRequest histStockRequest) {
		HistStockResponse histStockResponse = new HistStockResponse();
		System.out.println(">>> stkcode !");
		System.out.println(histStockRequest);
		histStockResponse.setData(
				histStockDataService.selectHists(histStockRequest.getStockCode(), histStockRequest.getStartDt()));
		return histStockResponse;
	}

	@PostMapping(value = "/dtrange/stkcode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object selectHists(String stockCode, Date startDt, Date endDt) {

		return null;
	}

//	@PostMapping(value = "/dtrange/model", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public Object selectHists(HistStockRequest histStockRequest) {
//
//		return null;
//	}

}
