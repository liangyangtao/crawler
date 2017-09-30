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
import com.kf.data.mybatis.entity.TycCompanyBranchCrawler;
import com.kf.data.mybatis.entity.TycCompanyChangeCrawler;
import com.kf.data.mybatis.entity.TycCompanyChattelMortgageCrawler;
import com.kf.data.mybatis.entity.TycCompanyCommonstockChangeCrawler;
import com.kf.data.mybatis.entity.TycCompanyCommonstockCrawler;
import com.kf.data.mybatis.entity.TycCompanyCopyrightCrawler;
import com.kf.data.mybatis.entity.TycCompanyDomainRecordCrawler;
import com.kf.data.mybatis.entity.TycCompanyEquityPledgedCrawler;
import com.kf.data.mybatis.entity.TycCompanyExecutiveCrawler;
import com.kf.data.mybatis.entity.TycCompanyImExPortCrawler;
import com.kf.data.mybatis.entity.TycCompanyPatentCrawlerWithBLOBs;
import com.kf.data.mybatis.entity.TycCompanyRecruitmentCrawler;
import com.kf.data.mybatis.entity.TycCompanySfpmCrawler;
import com.kf.data.mybatis.entity.TycCompanyShareholdersContributiveCrawler;
import com.kf.data.mybatis.entity.TycCompanyTrademarkCrawler;
import com.kf.data.mybatis.entity.TycCompanyWechatCrawler;
import com.kf.data.mybatis.entity.TycEventsInvestCrawler;
import com.kf.data.mybatis.entity.TycEventsInvestInvestorsCrawler;
import com.kf.data.mybatis.mapper.SqlAdapterMapper;
import com.kf.data.mybatis.mapper.TycBaseCompanyCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyBranchCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyChangeCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyChattelMortgageCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCommonstockChangeCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCommonstockCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyCopyrightCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyDomainRecordCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyEquityPledgedCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyExecutiveCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyImExPortCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyPatentCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyRecruitmentCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanySfpmCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyShareholdersContributiveCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyTrademarkCrawlerMapper;
import com.kf.data.mybatis.mapper.TycCompanyWechatCrawlerMapper;
import com.kf.data.mybatis.mapper.TycEventsInvestCrawlerMapper;
import com.kf.data.mybatis.mapper.TycEventsInvestInvestorsCrawlerMapper;
import com.kf.data.web.service.SaveJsonObjectService;

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

	@Autowired
	TycEventsInvestInvestorsCrawlerMapper tycEventsInvestInvestorsCrawlerMapper;

	@Autowired
	TycEventsInvestCrawlerMapper tycEventsInvestCrawlerMapper;

	@Override
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
			} else if (type.equals("tyc_events_invest_investors")) {
				TycEventsInvestInvestorsCrawler tycEventsInvestInvestorsCrawler = gson.fromJson(json,
						TycEventsInvestInvestorsCrawler.class);
				tycEventsInvestInvestorsCrawlerMapper.insertSelective(tycEventsInvestInvestorsCrawler);
			} else if (type.equals("tyc_events_invest")) {
				TycEventsInvestCrawler tycEventsInvestCrawler = gson.fromJson(json, TycEventsInvestCrawler.class);
				tycEventsInvestCrawlerMapper.insertSelective(tycEventsInvestCrawler);
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
