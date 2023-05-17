package spring.boot.oath2.scrabdatas.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import spring.boot.oath2.scrabdatas.service.CrawingStockService;
import spring.boot.oath2.scrabdatas.service.StockHistDataService;
import twitter4j.HttpResponse;

@RestController
@RequestMapping("/stock")
@Slf4j
public class CrawingController {

	private final CrawingStockService crawingStockService;
	private final StockHistDataService stockHistDataService;

	@Autowired
	public CrawingController(CrawingStockService crawingStockService, StockHistDataService stockHistDataService) {
		this.crawingStockService = crawingStockService;
		this.stockHistDataService = stockHistDataService;
	}

	@GetMapping(value = "/quoteStock", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getStockContent(HttpServletRequest httpRequest) {
		List<String> respList = crawingStockService.quoteStockContents();
		return respList;
	}

	@GetMapping(value = "/quoteStock/{stockCode}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getStockContent(HttpServletRequest httpRequest, @PathParam("stockCode") String stockCode) {
		String respString = crawingStockService.quoteStockContentByCode(stockCode);
		return respString;
	}

	@GetMapping(value = "/quoteStock/update")
	public String updateStockContent() {
		log.debug(">>> Update Stock Content!");
		crawingStockService.updateContents();
		return "update";
	}

	@GetMapping(value = "/scrawinghist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String startToScrawStockHistData(HttpServletRequest httpRequest, boolean saveToDb) {
		log.debug(">>> start to scrawing hist data!");
		new Thread(new Runnable() {
			@Override
			public void run() {
				stockHistDataService.startToScrawHistData(saveToDb);
			}
		}).start();

		return "start to scrawing hist data";
	}

}
