package spring.boot.oath2.iexcloud;

import java.util.List;


import pl.zankowski.iextrading4j.api.marketdata.TOPS;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;
import pl.zankowski.iextrading4j.client.sse.manager.SseRequest;
import pl.zankowski.iextrading4j.client.sse.request.marketdata.TopsSseRequestBuilder;

public class IexCloud {
	public static void main(String[] args) {
		// 设置你的 IEX Cloud API token
//		String apiKey = "YOUR_API_KEY";

		// 使用這個API需要註冊網頁帳號，七天內免費，超過七天要收費。
		try {
			final IEXCloudClient cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_STABLE,
					new IEXCloudTokenBuilder().withPublishableToken("pk_3860683192c74d6d954b637464d30c74")
							.withSecretToken("sk_03279f572b734cc6bee03347bc195e83").build());
			final Quote quote = cloudClient.executeRequest(new QuoteRequestBuilder().withSymbol("TSM").build());
			System.out.println(quote);

//			final Consumer<List<TOPS>> TOPS_CONSUMER = System.out::println;
//			final SseRequest<List<TOPS>> request = new TopsSseRequestBuilder()
//			        .withSymbol("AAPL")
//			        .build();
//			final BatchStocks result = cloudClient.executeRequest(new BatchStocksRequestBuilder()
//			        .withSymbol("TSM")
//			        .addType(BatchStocksType.LARGEST_TRADES)
//			        .addType(BatchStocksType.PRICE_TARGET)
//			        .addType(BatchStocksType.QUOTE)
//			        .build());
//			System.out.println(result);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
//	private static void getYahooSymbol() {
//        YahooFinanceAPI yahooFinanceAPI = new YahooFinanceAPI();
//
//        String yqlQuery = new YqlQueryBuilder()
//                .setFinanceModules(FinanceModules.SYMBOLS)
//                .build();
//
//        YahooFinanceRequest request = new YahooFinanceRequest(YahooFinanceURL.QUERY, yqlQuery);
//
//        try {
//            YahooFinanceData response = yahooFinanceAPI.getRequest(request);
//
//            if (response != null) {
//                YahooFinanceModule symbolsModule = response.getFinanceModule(FinanceModules.SYMBOLS);
//                List<YahooFinanceSymbol> symbols = symbolsModule.getSymbols();
//
//                for (YahooFinanceSymbol symbol : symbols) {
//                    System.out.println(symbol.getSymbol());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//	}
}
