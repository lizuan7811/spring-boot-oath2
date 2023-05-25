package spring.boot.oath2.scrabdatas.request;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HistStockRequest {
	
	@JsonProperty("stockCode")
	private String stockCode;
	@JsonProperty("startDt")
	private String startDt;
	@JsonProperty("endDt")
	private String endDt;
}
