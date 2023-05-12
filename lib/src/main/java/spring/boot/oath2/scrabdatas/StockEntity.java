package spring.boot.oath2.scrabdatas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 存Stock的Entity
 */
@Data
@Entity
@Table(name = "STOCK_PRICE_AMOUNT")
public class StockEntity implements Serializable {

	/**
	 * 序列號
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 股票代碼
	 */
	@Id
	@Column(name = "")
	private String stockCode;

	/**
	 * 成交價(收盤價)
	 */
	@Column(name = "")
	private String finalPrice;

	/**
	 * 開盤價
	 */
	@Column(name = "")
	private String opening;

	/**
	 * 最高
	 */
	@Column(name = "")
	private String highestPrice;

	/**
	 * 最低
	 */
	@Column(name = "")
	private String lowestPrice;

	/**
	 * 平均價
	 */
	@Column(name = "")
	private String avgPrice;

	/**
	 * 成交金額
	 */
	@Column(name = "")
	private String totalFinalPrice;

	/**
	 * 昨收
	 */
	@Column(name = "")
	private String yestFinalPrice;

	/**
	 * 漲跌幅
	 */
	@Column(name = "")
	private String quoteChange;

	/**
	 * 漲跌
	 */
	@Column(name = "")
	private String upAndDown;

	/**
	 * 總量
	 */
	@Column(name = "")
	private String totalAmount;

	/**
	 * 昨量
	 */
	@Column(name = "")
	private String yestAmount;

	/**
	 * 振幅
	 */
	@Column(name = "")
	private String amplitude;

	/**
	 * 內盤
	 */
	@Column(name = "")
	private String inner;
	/**
	 * 內盤比
	 */
	@Column(name = "")
	private String innerPercent;
	/**
	 * 外盤
	 */
	@Column(name = "")
	private String external;
	/**
	 * 外盤比
	 */
	@Column(name = "")
	private String externalPercent;

	@Column(name = "")
	private String dateTime;
}
