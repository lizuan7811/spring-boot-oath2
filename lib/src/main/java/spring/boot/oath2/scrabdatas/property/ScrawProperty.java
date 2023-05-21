package spring.boot.oath2.scrabdatas.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
@Data
@ConfigurationProperties(prefix="scraw")
public class ScrawProperty {
	private String stockHistFqdn;
	private String stockPureCodeFile;
	private String stockHistdataFile;
	private String host;
	private String port;
	private Integer startYear;
	private Integer baseRandTime;
	private Integer randTime;

}
