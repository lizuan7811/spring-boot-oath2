package spring.boot.oath2.scrabdatas.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HistStockRequest {
	
	@JsonProperty("selectType")
	@NotNull
	private String selectType;
	@JsonProperty("stockCode")
	private String stockCode;
	@JsonProperty("startDt")
	@NotNull
	private String startDt;
	@JsonProperty("endDt")
	@NotNull
	private String endDt;
}
