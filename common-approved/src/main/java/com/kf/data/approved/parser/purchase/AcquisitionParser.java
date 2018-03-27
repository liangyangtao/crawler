package com.kf.data.approved.parser.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.approved.parser.BaseParser;
import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.mybatis.entity.PdfReportLinks;

/****
 * 
 * @Title: AcquisitionParser.java
 * @Package com.kf.data.approved.parser.purchase
 * @Description: 收购基本信息
 * @author liangyt
 * @date 2018年1月5日 上午10:26:44
 * @version V1.0
 */
public class AcquisitionParser extends BaseParser {

	PurchaseCompanyInfoParser purchaseCompanyInfoParser = new PurchaseCompanyInfoParser();

	PurchasePersonInfoParser purchasePersonInfoParser = new PurchasePersonInfoParser();

	PurchaseShareholderParser purchaseShareholderParser = new PurchaseShareholderParser();

	PurchaseMajorClientParser purchaseMajorClientParser = new PurchaseMajorClientParser();

	PurchaseMajorSupplierParser purchaseMajorSupplierParser = new PurchaseMajorSupplierParser();

	PurchaseTradeDetailParser purchaseTradeDetailParser = new PurchaseTradeDetailParser();

	PurchaseCaseParser purchaseCaseParser = new PurchaseCaseParser();

	PurchaseProfitParser purchaseProfitParser = new PurchaseProfitParser();

	PurchaseLiabilitiesParser purchaseLiabilitiesParser = new PurchaseLiabilitiesParser();

	public void parserAcquisition(String html, PdfReportLinks pdfReportLink) {

		Document document = Jsoup.parse(html);
		document = new DocumentSimpler().simpleDocument(document);
		// 解析个人 多人
		Map<String, String> personMap = purchasePersonInfoParser.getResult(document);
		Iterator<String> iterator = personMap.keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			String person_post = personMap.get(name);
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("person_name", name);
				map.put("person_post", person_post);
				// map.put("stock_code", pdfReportLink.getCompanyId() + "");
				map.put("report_date", pdfReportLink.getPublishDate());
				map.put("link", pdfReportLink.getLink());
				sendJson(map, "purchase_person");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("收购公司");
		// 解析公司
		Elements tableElements = document.select("table");
		for (Element tableElement : tableElements) {
			// 统一社会信用代码
			if (tableElement.toString().contains("信用代码")) {
				// 单个公司
				Map<String, Object> companyMap = new HashMap<>();
				purchaseCompanyInfoParser.parserInfo(companyMap, tableElement.text());
				try {
					String companyName = (String) companyMap.get("cn_name");
					if (companyName == null || companyName.equals("基本情况") || companyName.isEmpty()) {
						continue;
					}
					// companyMap.put("stock_code", pdfReportLink.getCompanyId()
					// + "");
					companyMap.put("report_date", pdfReportLink.getPublishDate());
					companyMap.put("link", pdfReportLink.getLink());
					sendJson(companyMap, "purchase_company_info");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		String text = document.text();

		// 被收购方名称
		String acquired_nm = parserAcquiredNm(text);
		// 交易对手
		String counterparty_nm = parserCounterpartyNm(text);
		if (counterparty_nm.equals("转让方")) {
			counterparty_nm = "";
		}
		// 收购方式
		String mode_acqu = parserModeAcqu(text);
		// 收购时间
		String dt_acquire = parserDtAcquire(text);
		// 收购价格
		String price_acqu = parserPriceAcqu(text);
		// 收购股份数
		String stock_acqu = parserStockAcqu(text);
		// 收购金额
		String amount_acqu = parserAmountAcqu(text);
		// 是否关联交易
		String is_related = null;
		if (text.contains("不存在交易的情形") || text.contains("不存在交易") || text.contains("关联方未与") || text.contains("未发生任何交易")
				|| text.contains("本次收购不属于关联交易")) {
			is_related = "否";

		} else if (text.contains("收购人及其关联方与公众公司发生交易")) {
			is_related = "是";
		}

		// 收购类型
		String type_acqu = null;
		// 收购股份比例
		String rate_acqu = null;

		// 收购前后比例 、
		List<Map<String, Object>> beforeSholders = purchaseShareholderParser.paserBeforeSholder(document);
		List<Map<String, Object>> afterSholders = purchaseShareholderParser.paserAfterSholder(document);
		// 收购人
		String acquirer_nm = null;
		for (Map<String, Object> beforeMap : beforeSholders) {
			String beforeShareholder = (String) beforeMap.get("shareholder");
			// 收购前股份
			String pre_stock_acqu = null;
			// 收购前股份比例
			String pre_rate_acqu = null;
			// 收购后股份数
			String aft_stock_acqu = null;
			// 收购后股份比例
			String aft_rate_acqu = null;
			acquirer_nm = beforeShareholder;
			boolean hasAfter = false;
			for (Map<String, Object> afterMap : afterSholders) {
				String afterShareholder = (String) afterMap.get("shareholder");
				if (beforeShareholder.equals(afterShareholder)) {
					hasAfter = true;
					pre_stock_acqu = (String) beforeMap.get("quantity");
					pre_rate_acqu = (String) beforeMap.get("ratio");
					aft_stock_acqu = (String) afterMap.get("quantity");
					aft_rate_acqu = (String) afterMap.get("ratio");
					break;
				}
			}
			if (!hasAfter) {
				pre_stock_acqu = (String) beforeMap.get("quantity");
				pre_rate_acqu = (String) beforeMap.get("ratio");
			}
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("acquirer_nm", acquirer_nm);
				map.put("dt_acquire", dt_acquire);
				map.put("acquired_nm", acquired_nm);
				map.put("counterparty_nm", counterparty_nm);
				map.put("type_acqu", type_acqu);
				map.put("mode_acqu", mode_acqu);
				map.put("is_related", is_related);
				map.put("price_acqu", price_acqu);
				map.put("stock_acqu", stock_acqu);
				map.put("rate_acqu", rate_acqu);
				map.put("amount_acqu", amount_acqu);
				map.put("pre_stock_acqu", pre_stock_acqu);
				map.put("pre_rate_acqu", pre_rate_acqu);
				map.put("aft_stock_acqu", aft_stock_acqu);
				map.put("aft_rate_acqu", aft_rate_acqu);
				map.put("remark", "");
				// map.put("stock_code", pdfReportLink.getCompanyId() + "");
				map.put("report_date", pdfReportLink.getPublishDate());
				map.put("link", pdfReportLink.getLink());
				sendJson(map, "purchase_acquisition");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		////////////////////////////////////////////////////
		for (Map<String, Object> afterMap : afterSholders) {
			String afterShareholder = (String) afterMap.get("shareholder");
			// 收购前股份
			String pre_stock_acqu = null;
			// 收购前股份比例
			String pre_rate_acqu = null;
			// 收购后股份数
			String aft_stock_acqu = null;
			// 收购后股份比例
			String aft_rate_acqu = null;
			boolean hasBefore = false;
			acquirer_nm = afterShareholder;
			for (Map<String, Object> beforeMap : beforeSholders) {
				String beforeShareholder = (String) beforeMap.get("shareholder");
				if (beforeShareholder.equals(afterShareholder)) {
					hasBefore = true;
					pre_stock_acqu = (String) beforeMap.get("quantity");
					pre_rate_acqu = (String) beforeMap.get("ratio");
					aft_stock_acqu = (String) afterMap.get("quantity");
					aft_rate_acqu = (String) afterMap.get("ratio");
					break;
				}
			}
			if (!hasBefore) {
				pre_stock_acqu = (String) afterMap.get("quantity");
				pre_rate_acqu = (String) afterMap.get("ratio");
			}
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("acquirer_nm", acquirer_nm);
				map.put("dt_acquire", dt_acquire);
				map.put("acquired_nm", acquired_nm);
				map.put("counterparty_nm", counterparty_nm);
				map.put("type_acqu", type_acqu);
				map.put("mode_acqu", mode_acqu);
				map.put("is_related", is_related);
				map.put("price_acqu", price_acqu);
				map.put("stock_acqu", stock_acqu);
				map.put("rate_acqu", rate_acqu);
				map.put("amount_acqu", amount_acqu);
				map.put("pre_stock_acqu", pre_stock_acqu);
				map.put("pre_rate_acqu", pre_rate_acqu);
				map.put("aft_stock_acqu", aft_stock_acqu);
				map.put("aft_rate_acqu", aft_rate_acqu);
				map.put("remark", "");
				// map.put("stock_code", pdfReportLink.getCompanyId() + "");
				map.put("report_date", pdfReportLink.getPublishDate());
				map.put("link", pdfReportLink.getLink());
				sendJson(map, "purchase_acquisition");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		////////////////////////////////////////////////////////

		if (beforeSholders.size() == 0 && afterSholders.size() == 0) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("acquirer_nm", acquirer_nm);
				map.put("dt_acquire", dt_acquire);
				map.put("acquired_nm", acquired_nm);
				map.put("counterparty_nm", counterparty_nm);
				map.put("type_acqu", type_acqu);
				map.put("mode_acqu", mode_acqu);
				map.put("is_related", is_related);
				map.put("price_acqu", price_acqu);
				map.put("stock_acqu", stock_acqu);
				map.put("rate_acqu", rate_acqu);
				map.put("amount_acqu", amount_acqu);
				// map.put("pre_stock_acqu", pre_stock_acqu);
				// map.put("pre_rate_acqu", pre_rate_acqu);
				// map.put("aft_stock_acqu", aft_stock_acqu);
				// map.put("aft_rate_acqu", aft_rate_acqu);
				map.put("remark", "");
				// map.put("stock_code", pdfReportLink.getCompanyId() + "");
				map.put("report_date", pdfReportLink.getPublishDate());
				map.put("link", pdfReportLink.getLink());
				sendJson(map, "purchase_acquisition");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 关联销售
		List<Map<String, Object>> clients = purchaseMajorClientParser.paserMajorClient(document);
		for (Map<String, Object> map : clients) {
			map.put("stock_code", pdfReportLink.getCompanyId() + "");
			map.put("report_date", pdfReportLink.getPublishDate());
			map.put("link", pdfReportLink.getLink());
			sendJson(map, "purchase_major_client");
		}

		// 关联购买
		List<Map<String, Object>> supplies = purchaseMajorSupplierParser.paserMajorSupplier(document);
		for (Map<String, Object> map : supplies) {
			map.put("stock_code", pdfReportLink.getCompanyId() + "");
			map.put("report_date", pdfReportLink.getPublishDate());
			map.put("link", pdfReportLink.getLink());
			sendJson(map, "purchase_major_supplier");
		}

		// 前六个月买卖股票的情况
		List<Map<String, Object>> tradeDetails = purchaseTradeDetailParser.paserTradeDetail(document);
		for (Map<String, Object> map : tradeDetails) {
			map.put("stock_code", pdfReportLink.getCompanyId() + "");
			map.put("report_date", pdfReportLink.getPublishDate());
			map.put("link", pdfReportLink.getLink());
			sendJson(map, "purchase_trade_detail");
		}

		String acquirerName = parserAcquirerNm(text);
		// 现金表
		List<Map<String, Object>> cases = purchaseCaseParser.getCaseResult(document);
		for (Map<String, Object> map : cases) {
			map.put("company_name", acquirerName);
			map.put("report_date", pdfReportLink.getPublishDate());
			map.put("link", pdfReportLink.getLink());
			sendJson(map, "purchase_cash");
		}

		// 负债表
		List<Map<String, Object>> liabilities = purchaseLiabilitiesParser.getLiabilitiesResult(document);

		for (Map<String, Object> map : liabilities) {
			map.put("company_name", acquirerName);
			map.put("report_date", pdfReportLink.getPublishDate());
			map.put("link", pdfReportLink.getLink());
			sendJson(map, "purchase_liabilities");
		}

		// 利润表
		List<Map<String, Object>> profits = purchaseProfitParser.getProfitResult(document);
		for (Map<String, Object> map : profits) {
			map.put("company_name", acquirerName);
			map.put("report_date", pdfReportLink.getPublishDate());
			map.put("link", pdfReportLink.getLink());
			sendJson(map, "purchase_profit");
		}

	}

	/****
	 * 交易金额
	 * 
	 * @param text
	 * @return
	 */
	public String parserAmountAcqu(String text) {
		List<String> source = new ArrayList<>();
		List<String> toltalPrice = getStrByReg("收购价款总额为", "元", text);
		toltalPrice.addAll(getStrByReg("转让价款总额为", "元", text));
		toltalPrice.addAll(getStrByReg("交易金额总计", "元", text));
		toltalPrice.addAll(getStrByReg("转让款", "元", text));
		toltalPrice.addAll(getStrByReg("出资", "元", text));
		toltalPrice.addAll(getStrByReg("认购金额总额为", "元", text));
		for (String string : toltalPrice) {
			if (string.length() > 20) {
				continue;
			}
			if (string.contains("%") || string.contains("（") || string.contains("％")) {
				continue;
			}
			source.add(string);
		}
		return getMinStr(source);
	}

	/***
	 * 交易标的 （多少股）
	 * 
	 * @param text
	 * @return
	 */
	public String parserStockAcqu(String text) {
		List<String> source = new ArrayList<>();
		List<String> stockNums = getStrByReg("转让共计", "股", text);
		stockNums.addAll(getStrByReg("合计受让", "股", text));
		stockNums.addAll(getStrByReg("合计持有", "股", text));
		stockNums.addAll(getStrByReg("买入", "股", text));
		stockNums.addAll(getStrByReg("发行的", "股", text));
		stockNums.addAll(getStrByReg("认购的股票数量为", "股", text));
		stockNums.addAll(getStrByReg("直接持有", "股", text));
		for (String string : stockNums) {
			if (string.contains("%") || string.contains("％")) {
				continue;
			}
			source.add(getNum(string));
		}
		return getMinStr(source);

	}

	/****
	 * 每股价格
	 * 
	 * @param text
	 * @return
	 */
	private String parserPriceAcqu(String text) {
		List<String> source = new ArrayList<>();
		List<String> prices = getStrByReg("以每股", "的价格", text);
		prices.addAll(getStrByReg("成交均价", "元/股", text));
		prices.addAll(getStrByReg("转让价格为", "元/股", text));
		prices.addAll(getStrByReg("每股认购价格为", "元", text));
		prices.addAll(getStrByReg("每股转让价格", "元", text));

		// prices.addAll(getStrByReg("每股", "元", text));
		for (String string : prices) {
			if (string.contains("%") || string.contains("％")) {
				continue;
			}
			string = string.replace("·", "");
			source.add(string);
		}
		return getMinStr(source);
	}

	/****
	 * 交易日期
	 * 
	 * @param text
	 * @return
	 */
	public String parserDtAcquire(String text) {
		// 收购日期
		List<String> source = new ArrayList<>();
		List<String> dt_acquires = getStrByReg("本次收购相关协议的主要内容", "，", text);
		for (String string : dt_acquires) {
			if (string.contains("%") || string.contains("“")) {
				continue;
			}
			if (string.length() > 20) {
				continue;
			}
			if (string.contains("年")) {
				source.add(string);
			}
		}
		return getMinStr(source);
	}

	/****
	 * 交易方式
	 * 
	 * @param text
	 * @return
	 */
	private String parserModeAcqu(String text) {
		List<String> source = new ArrayList<>();
		List<String> purchaseTypes = getStrByReg("通过", "的方式收购", text);
		purchaseTypes.addAll(getStrByReg("通过", "的方式购买", text));
		purchaseTypes.addAll(getStrByReg("通过", "的方式买入", text));
		purchaseTypes.addAll(getStrByReg("通过", "方式完成", text));
		purchaseTypes.addAll(getStrByReg("采用", "认购", text));
		for (String string : purchaseTypes) {
			if (string.contains("%") || string.contains("，") || string.contains("（") || string.contains("。")) {

				continue;
			}
			source.add(string);
		}
		return getMinStr(source);
	}

	/****
	 * 交易对手
	 * 
	 * @param text
	 * @return
	 */
	public String parserCounterpartyNm(String text) {
		List<String> source = new ArrayList<>();
		// List<String> counterpartys = getStrByReg("。", "通过协议转让的方式减持", text);
		// counterpartys.addAll(getStrByReg("分别向", "购买", text));
		// counterpartys.addAll(getStrByReg("购买", "持有的", text));
		// counterpartys.addAll(getStrByReg("受让", "持有的", text));
		// counterpartys.addAll(getStrByReg("收购", "持有的", text));
		// for (String string : counterpartys) {
		// if (string.length() > 100 || string.length() < 3) {
		// continue;
		// }
		// if (string.contains("%") || string.contains("《") ||
		// string.contains("》") || string.contains("％")) {
		// continue;
		// }
		//
		// source.add(string);
		// }
		return "";

		// return getMinStr(source);

	}

	/****
	 * 被收购方
	 * 
	 * @param text
	 * @return
	 */
	public String parserAcquiredNm(String text) {
		// 被收购方
		List<String> acquiredNms = getStrByReg("公司名称：", "挂牌地点", text);
		acquiredNms.addAll(getStrByReg("公司名称：", "股票挂牌地点", text));
		acquiredNms.addAll(getStrByReg("公司名称：", "上市地点", text));
		acquiredNms.addAll(getStrByReg("公司名称：", "英文名称", text));
		acquiredNms.addAll(getStrByReg("公司名称：", "挂牌公司地点", text));

		String result = null;
		if (acquiredNms.size() > 0) {
			result = acquiredNms.get(0);
			result = result.replace("：", "");
			for (String string : acquiredNms) {
				if (string.contains("%") || string.contains("，") || string.contains("。") || string.contains("、")) {
					continue;
				}
				if (string.length() > 100) {
					continue;
				}
				string = string.replace("：", "");
				if (result.length() > string.length()) {
					result = string;
				}

			}
		}
		return result;

	}

	/****
	 * 收购方
	 * 
	 * @param text
	 * @return
	 */
	public String parserAcquirerNm(String text) {
		// 收购方
		List<String> acquiredNms = getStrByReg("收购人名称：", "住所", text);
		acquiredNms.addAll(getStrByReg("收购人：", "注册地址", text));
		acquiredNms.addAll(getStrByReg("收购人：", "地址", text));
		acquiredNms.addAll(getStrByReg("收购人：", "住所", text));
		acquiredNms.addAll(getStrByReg("收购人：", "收购人通讯地址", text));
		acquiredNms.addAll(getStrByReg("收购人：", "通讯地址", text));
		acquiredNms.addAll(getStrByReg("收购方：", "注册地址", text));
		acquiredNms.addAll(getStrByReg("收购方：", "地址", text));
		acquiredNms.addAll(getStrByReg("收购方：", "住所", text));
		acquiredNms.addAll(getStrByReg("收购方：", "通讯地址", text));
		acquiredNms.addAll(getStrByReg("收购方：", "被收购方：", text));
		acquiredNms.addAll(getStrByReg("收购人基本情况公司名称", "公司类型", text));
		acquiredNms.addAll(getStrByReg("收购人基本情况公司名称", "统一社会信用代码", text));

		String result = null;
		if (acquiredNms.size() > 0) {
			result = acquiredNms.get(0);
			result = result.replace("：", "");
			
			for (String string : acquiredNms) {
				if (string.contains("%") || string.contains("，") || string.contains("。") || string.contains("、")) {
					continue;
				}
				if (string.length() > 100 || string.length() < 6) {
					continue;
				}
				string = string.replace("：", "");
				string = string.replace(":", "");
				string = string.replaceAll("\\d+", "");
				if (result.length() > string.length()) {

					result = string;
				}

			}
		}
		return result;

	}

	/****
	 * 选择可能的结果
	 * 
	 * @param source
	 * @return
	 */
	public String getMinStr(List<String> source) {
		if (source.size() == 0) {
			return "";
		}
		String result = source.get(0);
		// for (String string : source) {
		// if (result.length() > string.length()) {
		// result = string;
		// }
		// }
		return result;

	}

}
