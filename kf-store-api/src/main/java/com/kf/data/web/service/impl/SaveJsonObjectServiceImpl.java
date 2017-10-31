package com.kf.data.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kf.data.mybatis.entity.SqlAdapter;
import com.kf.data.mybatis.entity.TycBaseCompanyCrawler;
import com.kf.data.mybatis.entity.TycCompanyAbnormalOperationCrawlerWithBLOBs;
import com.kf.data.mybatis.entity.TycCompanyAdmPenaltyCrawler;
import com.kf.data.mybatis.entity.TycCompanyBranchCrawler;
import com.kf.data.mybatis.entity.TycCompanyBusinessCrawler;
import com.kf.data.mybatis.entity.TycCompanyCertificateCrawler;
import com.kf.data.mybatis.entity.TycCompanyChangeCrawler;
import com.kf.data.mybatis.entity.TycCompanyChattelMortgageCrawler;
import com.kf.data.mybatis.entity.TycCompanyCheckCrawler;
import com.kf.data.mybatis.entity.TycCompanyCommonstockChangeCrawler;
import com.kf.data.mybatis.entity.TycCompanyCommonstockCrawler;
import com.kf.data.mybatis.entity.TycCompanyCompetitorsCrawler;
import com.kf.data.mybatis.entity.TycCompanyCopyrightCrawler;
import com.kf.data.mybatis.entity.TycCompanyCoreTeamCrawler;
import com.kf.data.mybatis.entity.TycCompanyDomainRecordCrawler;
import com.kf.data.mybatis.entity.TycCompanyEquityPledgedCrawler;
import com.kf.data.mybatis.entity.TycCompanyExecutiveCrawler;
import com.kf.data.mybatis.entity.TycCompanyFinancingCrawler;
import com.kf.data.mybatis.entity.TycCompanyImExPortCrawler;
import com.kf.data.mybatis.entity.TycCompanyInvestOutsideCrawler;
import com.kf.data.mybatis.entity.TycCompanyPatentCrawlerWithBLOBs;
import com.kf.data.mybatis.entity.TycCompanyProductCrawler;
import com.kf.data.mybatis.entity.TycCompanyRecruitmentCrawler;
import com.kf.data.mybatis.entity.TycCompanySfpmCrawler;
import com.kf.data.mybatis.entity.TycCompanyShareholdersContributiveCrawler;
import com.kf.data.mybatis.entity.TycCompanySoftCopyrightCrawler;
import com.kf.data.mybatis.entity.TycCompanyTaxArrearsCrawler;
import com.kf.data.mybatis.entity.TycCompanyTaxRatingCrawler;
import com.kf.data.mybatis.entity.TycCompanyTrademarkCrawler;
import com.kf.data.mybatis.entity.TycCompanyWechatCrawler;
import com.kf.data.mybatis.entity.TycEventsInvestCrawler;
import com.kf.data.mybatis.mapper.SqlAdapterMapper;
import com.kf.data.mybatis.mapper.TycBaseCompanyCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyAbnormalOperationCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyAdmPenaltyCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyBranchCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyBusinessCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCertificateCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyChangeCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyChattelMortgageCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCheckCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCommonstockChangeCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCommonstockCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCompetitorsCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCopyrightCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCoreTeamCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyDomainRecordCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyEquityPledgedCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyExecutiveCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyFinancingCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyImExPortCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyInvestOutsideCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyPatentCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyProductCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyRecruitmentCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanySfpmCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyShareholdersContributiveCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanySoftCopyrightCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyTaxArrearsCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyTaxRatingCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyTrademarkCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyWechatCrawlerMapper;
import com.kf.data.mybatis.mapper.TycEventsInvestCrawlerMapper;
import com.kf.data.web.service.SaveJsonObjectService;

/****
 * 
 * @Title: SaveJsonObjectServiceImpl.java
 * @Package com.kf.data.web.service.impl
 * @Description: 保存传送的json数据
 * @author liangyt
 * @date 2017年10月11日 下午2:34:49
 * @version V1.0
 */
@Service
public class SaveJsonObjectServiceImpl implements SaveJsonObjectService {

	Gson gson = new GsonBuilder().create();

	@Autowired
	SqlAdapterMapper sqlAdapterMaper;

	@Autowired
	TycBaseCompanyCrawlerMapper tycBaseCompanyCrawlerMapper;

	@Autowired
	TycCompanyBranchCrawlerMapper tycCompanyBranchCrawlerMapper;

	@Autowired
	TycCompanyChangeCrawlerMapper tycCompanyChangeCrawlerMapper;

	@Autowired
	TycCompanyChattelMortgageCrawlerMapper tycCompanyChattelMortgageCrawlerMapper;

	@Autowired
	TycCompanyCopyrightCrawlerMapper tycCompanyCopyrightCrawlerMapper;

	@Autowired
	TycCompanyDomainRecordCrawlerMapper tycCompanyDomainRecordCrawlerMapper;

	@Autowired
	TycCompanyEquityPledgedCrawlerMapper tycCompanyEquityPledgedCrawlerMapper;

	@Autowired
	TycCompanyExecutiveCrawlerMapper tycCompanyExecutiveCrawlerMapper;

	@Autowired
	TycCompanyRecruitmentCrawlerMapper tycCompanyRecruitmentCrawlerMapper;

	@Autowired
	TycCompanyShareholdersContributiveCrawlerMapper tycCompanyShareholdersContributiveCrawlerMapper;

	@Autowired
	TycCompanyTrademarkCrawlerMapper tycCompanyTrademarkCrawlerMapper;

	@Autowired
	TycCompanyPatentCrawlerMapper tycCompanyPatentCrawlerMapper;

	@Autowired
	TycCompanyCommonstockCrawlerMapper tycCompanyCommonstockCrawlerMapper;

	@Autowired
	TycCompanyCommonstockChangeCrawlerMapper tycCompanyCommonstockChangeCrawlerMapper;

	@Autowired
	TycCompanyImExPortCrawlerMapper tycCompanyImExPortCrawlerMapper;

	@Autowired
	TycCompanySfpmCrawlerMapper tycCompanySfpmCrawlerMapper;

	@Autowired
	TycCompanyWechatCrawlerMapper tycCompanyWechatCrawlerMapper;

	// @Autowired
	// TycEventsInvestInvestorsCrawlerMapper
	// tycEventsInvestInvestorsCrawlerMapper;

	// 投资事件
	@Autowired
	TycEventsInvestCrawlerMapper tycEventsInvestCrawlerMapper;

	// 对外投资
	@Autowired
	TycCompanyInvestOutsideCrawlerMapper tycCompanyInvestOutsideCrawlerMapper;

	// 核心团队
	@Autowired
	TycCompanyCoreTeamCrawlerMapper tycCompanyCoreTeamCrawlerMapper;

	// 企业业务
	@Autowired
	TycCompanyBusinessCrawlerMapper tycCompanyBusinessCrawlerMapper;

	// 竞品信息
	@Autowired
	TycCompanyCompetitorsCrawlerMapper tycCompanyCompetitorsCrawlerMapper;

	// 融资历史
	@Autowired
	TycCompanyFinancingCrawlerMapper tycCompanyFinancingCrawlerMapper;

	// 经营异常
	@Autowired
	TycCompanyAbnormalOperationCrawlerMapper tycCompanyAbnormalOperationCrawlerMapper;
	// 行政处罚
	@Autowired
	TycCompanyAdmPenaltyCrawlerMapper tycCompanyAdmPenaltyCrawlerMapper;

	// 欠税公告
	@Autowired
	TycCompanyTaxArrearsCrawlerMapper tycCompanyTaxArrearsCrawlerMapper;

	// 税务评级
	@Autowired
	TycCompanyTaxRatingCrawlerMapper tycCompanyTaxRatingCrawlerMapper;

	// 抽查检查
	@Autowired
	TycCompanyCheckCrawlerMapper tycCompanyCheckCrawlerMapper;

	// 软件著作权
	@Autowired
	TycCompanySoftCopyrightCrawlerMapper tycCompanySoftCopyrightCrawlerMapper;

	// 资质证书
	@Autowired
	TycCompanyCertificateCrawlerMapper tycCompanyCertificateCrawlerMapper;

	// 产品信息
	@Autowired
	TycCompanyProductCrawlerMapper tycCompanyProductCrawlerMapper;

	/***
	 * 保存传送的json数据
	 * 
	 * type 是表名
	 * 
	 * json 数据
	 */
	public String parserJson(String json, String type) {
		try {
			if (type.equals("tyc_base_company")) {
				TycBaseCompanyCrawler tycBaseCompanyCrawler = gson.fromJson(json, TycBaseCompanyCrawler.class);
				tycBaseCompanyCrawlerMapper.insertSelective(tycBaseCompanyCrawler);
			} else if (type.equals("tyc_company_branch")) {
				TycCompanyBranchCrawler tycCompanyBranchCrawler = gson.fromJson(json, TycCompanyBranchCrawler.class);
				tycCompanyBranchCrawlerMapper.insertSelective(tycCompanyBranchCrawler);
			} else if (type.equals("tyc_company_change")) {
				TycCompanyChangeCrawler tycCompanyChangeCrawler = gson.fromJson(json, TycCompanyChangeCrawler.class);
				tycCompanyChangeCrawlerMapper.insertSelective(tycCompanyChangeCrawler);
			} else if (type.equals("tyc_company_chattel_mortgage")) {
				TycCompanyChattelMortgageCrawler tycCompanyChattelMortgageCrawler = gson.fromJson(json,
						TycCompanyChattelMortgageCrawler.class);
				tycCompanyChattelMortgageCrawlerMapper.insertSelective(tycCompanyChattelMortgageCrawler);
			} else if (type.equals("tyc_company_copyright")) {
				TycCompanyCopyrightCrawler tycCompanyCopyrightCrawler = gson.fromJson(json,
						TycCompanyCopyrightCrawler.class);
				tycCompanyCopyrightCrawlerMapper.insertSelective(tycCompanyCopyrightCrawler);
			} else if (type.equals("tyc_company_domain_record")) {
				TycCompanyDomainRecordCrawler tycCompanyDomainRecordCrawler = gson.fromJson(json,
						TycCompanyDomainRecordCrawler.class);
				tycCompanyDomainRecordCrawlerMapper.insertSelective(tycCompanyDomainRecordCrawler);
			} else if (type.equals("tyc_company_equity_pledged")) {
				TycCompanyEquityPledgedCrawler tycCompanyEquityPledgedCrawler = gson.fromJson(json,
						TycCompanyEquityPledgedCrawler.class);
				tycCompanyEquityPledgedCrawlerMapper.insertSelective(tycCompanyEquityPledgedCrawler);
			} else if (type.equals("tyc_company_executive")) {
				TycCompanyExecutiveCrawler tycCompanyExecutiveCrawler = gson.fromJson(json,
						TycCompanyExecutiveCrawler.class);
				tycCompanyExecutiveCrawlerMapper.insertSelective(tycCompanyExecutiveCrawler);
			} else if (type.equals("tyc_company_patent")) {
				TycCompanyPatentCrawlerWithBLOBs tycCompanyPatentCrawler = gson.fromJson(json,
						TycCompanyPatentCrawlerWithBLOBs.class);
				tycCompanyPatentCrawlerMapper.insertSelective(tycCompanyPatentCrawler);
			} else if (type.equals("tyc_company_recruitment")) {
				TycCompanyRecruitmentCrawler tycCompanyRecruitmentCrawler = gson.fromJson(json,
						TycCompanyRecruitmentCrawler.class);
				tycCompanyRecruitmentCrawlerMapper.insertSelective(tycCompanyRecruitmentCrawler);
			} else if (type.equals("tyc_company_shareholders_contributive")) {
				TycCompanyShareholdersContributiveCrawler tycCompanyShareholdersContributiveCrawler = gson
						.fromJson(json, TycCompanyShareholdersContributiveCrawler.class);
				tycCompanyShareholdersContributiveCrawlerMapper
						.insertSelective(tycCompanyShareholdersContributiveCrawler);
			} else if (type.equals("tyc_company_trademark")) {
				TycCompanyTrademarkCrawler tycCompanyTrademarkCrawler = gson.fromJson(json,
						TycCompanyTrademarkCrawler.class);
				tycCompanyTrademarkCrawlerMapper.insertSelective(tycCompanyTrademarkCrawler);
			} else if (type.equals("tyc_events_tender_bid")) {

			} else if (type.equals("tyc_company_commonstock")) {
				TycCompanyCommonstockCrawler tycCompanyCommonstockCrawler = gson.fromJson(json,
						TycCompanyCommonstockCrawler.class);
				tycCompanyCommonstockCrawlerMapper.insertSelective(tycCompanyCommonstockCrawler);
			} else if (type.equals("tyc_company_commonstock_change")) {
				TycCompanyCommonstockChangeCrawler tycCompanyCommonstockChangeCrawler = gson.fromJson(json,
						TycCompanyCommonstockChangeCrawler.class);
				tycCompanyCommonstockChangeCrawlerMapper.insertSelective(tycCompanyCommonstockChangeCrawler);
			} else if (type.equals("tyc_company_im_ex_port")) {
				TycCompanyImExPortCrawler tycCompanyImExPortCrawler = gson.fromJson(json,
						TycCompanyImExPortCrawler.class);
				tycCompanyImExPortCrawlerMapper.insertSelective(tycCompanyImExPortCrawler);
			} else if (type.equals("tyc_company_sfpm")) {
				TycCompanySfpmCrawler tycCompanySfpmCrawler = gson.fromJson(json, TycCompanySfpmCrawler.class);
				tycCompanySfpmCrawlerMapper.insertSelective(tycCompanySfpmCrawler);
			} else if (type.equals("tyc_company_wechat")) {
				TycCompanyWechatCrawler tycCompanyWechatCrawler = gson.fromJson(json, TycCompanyWechatCrawler.class);
				tycCompanyWechatCrawlerMapper.insertSelective(tycCompanyWechatCrawler);
			} else if (type.equals("tyc_company_invest_outside")) {
				// 对外投资
				TycCompanyInvestOutsideCrawler tycCompanyInvestOutsideCrawler = gson.fromJson(json,
						TycCompanyInvestOutsideCrawler.class);
				tycCompanyInvestOutsideCrawlerMapper.insertSelective(tycCompanyInvestOutsideCrawler);
			} else if (type.equals("tyc_company_core_team")) {
				// 核心团队
				TycCompanyCoreTeamCrawler tycCompanyCoreTeamCrawler = gson.fromJson(json,
						TycCompanyCoreTeamCrawler.class);
				tycCompanyCoreTeamCrawlerMapper.insertSelective(tycCompanyCoreTeamCrawler);
			} else if (type.equals("tyc_company_business")) {
				// 企业业务
				TycCompanyBusinessCrawler tycCompanyBusinessCrawler = gson.fromJson(json,
						TycCompanyBusinessCrawler.class);
				tycCompanyBusinessCrawlerMapper.insertSelective(tycCompanyBusinessCrawler);
			} else if (type.equals("tyc_company_competitors")) {
				// 竞品信息
				TycCompanyCompetitorsCrawler tycCompanyCompetitorsCrawler = gson.fromJson(json,
						TycCompanyCompetitorsCrawler.class);
				tycCompanyCompetitorsCrawlerMapper.insertSelective(tycCompanyCompetitorsCrawler);
			} else if (type.equals("tyc_events_invest")) {
				// 投资事件
				TycEventsInvestCrawler tycEventsInvestCrawler = gson.fromJson(json, TycEventsInvestCrawler.class);
				tycEventsInvestCrawlerMapper.insertSelective(tycEventsInvestCrawler);
			} else if (type.equals("tyc_company_financing")) {
				// 融资历史
				TycCompanyFinancingCrawler tycCompanyFinancingCrawler = gson.fromJson(json,
						TycCompanyFinancingCrawler.class);
				tycCompanyFinancingCrawlerMapper.insertSelective(tycCompanyFinancingCrawler);
			} else if (type.equals("tyc_company_abnormal_operation")) {
				// 经营异常
				TycCompanyAbnormalOperationCrawlerWithBLOBs tycCompanyAbnormalOperationCrawlerWithBLOBs = gson
						.fromJson(json, TycCompanyAbnormalOperationCrawlerWithBLOBs.class);
				tycCompanyAbnormalOperationCrawlerMapper.insertSelective(tycCompanyAbnormalOperationCrawlerWithBLOBs);
			} else if (type.equals("tyc_company_adm_penalty")) {
				// 行政处罚
				TycCompanyAdmPenaltyCrawler tycCompanyAdmPenaltyCrawler = gson.fromJson(json,
						TycCompanyAdmPenaltyCrawler.class);
				tycCompanyAdmPenaltyCrawlerMapper.insertSelective(tycCompanyAdmPenaltyCrawler);
			} else if (type.equals("tyc_company_tax_arrears")) {
				// 欠税公告
				TycCompanyTaxArrearsCrawler tycCompanyTaxArrearsCrawler = gson.fromJson(json,
						TycCompanyTaxArrearsCrawler.class);
				tycCompanyTaxArrearsCrawlerMapper.insertSelective(tycCompanyTaxArrearsCrawler);
			} else if (type.equals("tyc_company_tax_rating")) {
				// 税务评级
				TycCompanyTaxRatingCrawler tycCompanyTaxRatingCrawler = gson.fromJson(json,
						TycCompanyTaxRatingCrawler.class);
				tycCompanyTaxRatingCrawlerMapper.insertSelective(tycCompanyTaxRatingCrawler);
			} else if (type.equals("tyc_company_check")) {
				// 抽查检查
				TycCompanyCheckCrawler tycCompanyCheckCrawler = gson.fromJson(json, TycCompanyCheckCrawler.class);
				tycCompanyCheckCrawlerMapper.insertSelective(tycCompanyCheckCrawler);
			} else if (type.equals("tyc_company_soft_copyright")) {
				// 软件著作权
				TycCompanySoftCopyrightCrawler tycCompanySoftCopyrightCrawler = gson.fromJson(json,
						TycCompanySoftCopyrightCrawler.class);
				tycCompanySoftCopyrightCrawlerMapper.insertSelective(tycCompanySoftCopyrightCrawler);
			} else if (type.equals("tyc_company_certificate")) {
				// 软件著作权
				TycCompanyCertificateCrawler tycCompanyCertificateCrawler = gson.fromJson(json,
						TycCompanyCertificateCrawler.class);
				tycCompanyCertificateCrawlerMapper.insertSelective(tycCompanyCertificateCrawler);
			} else if (type.equals("tyc_company_product")) {
				// 产品信息
				TycCompanyProductCrawler tycCompanyProductCrawler = gson.fromJson(json, TycCompanyProductCrawler.class);
				tycCompanyProductCrawlerMapper.insertSelective(tycCompanyProductCrawler);
			} else {
				try {
					Map<String, Object> map = gson.fromJson(json, new TypeToken<HashMap<String, Object>>() {
					}.getType());
					String sql = "insert into " + type + " ";
					SqlAdapter sqlAdapter = new SqlAdapter();
					sqlAdapter.setSql(sql);
					sqlAdapter.setObj(map);
					sqlAdapterMaper.executeMapSQL(sqlAdapter);
					return "ok";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

}
