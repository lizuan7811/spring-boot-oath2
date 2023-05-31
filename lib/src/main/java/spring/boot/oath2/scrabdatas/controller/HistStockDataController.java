package spring.boot.oath2.scrabdatas.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import spring.boot.oath2.scrabdatas.exception.FindHistDataException;
import spring.boot.oath2.scrabdatas.model.StockHistModel;
import spring.boot.oath2.scrabdatas.request.HistStockRequest;
import spring.boot.oath2.scrabdatas.response.HistStockResponse;
import spring.boot.oath2.scrabdatas.service.HistStockDataService;

/**
 * 查詢歷史Stock Data的Controller
 */
@Slf4j
@RestController
@RequestMapping(value = "/findhist")
public class HistStockDataController {

	private final HistStockDataService histStockDataService;

	@Autowired
	public HistStockDataController(HistStockDataService histStockDataService) {
		this.histStockDataService = histStockDataService;
	}

	/**
	 * 查一個code一段時間
	 */
	@PostMapping(value = "/stkcode")
	public HistStockResponse selectHists(@RequestBody @Validated HistStockRequest histStockRequest) {
		HistStockResponse histStockResponse = new HistStockResponse();
		log.debug(">>> selectHists! Request: {}!", histStockRequest);
		try {
			List<StockHistModel> resultList = histStockDataService.selectHists(histStockRequest);
			histStockResponse.setData(resultList);
			System.out.println(resultList);
		} catch (FindHistDataException fhde) {
			fhde.printStackTrace();
		}
		return histStockResponse;
	}
}
