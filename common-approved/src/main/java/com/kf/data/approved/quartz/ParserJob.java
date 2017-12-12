package com.kf.data.approved.quartz;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kf.data.approved.parser.CompanyNameParser;
import com.kf.data.approved.sender.SendMail;
import com.kf.data.approved.store.PdfReportLinksReader;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mybatis.entity.PdfReportLinks;

/****
 * 
 * @Title: ParserJob.java
 * @Package com.kf.data.approved.quartz
 * @Description: 任务
 * @author liangyt
 * @date 2017年12月12日 上午10:28:21
 * @version V1.0
 */
@DisallowConcurrentExecution
public class ParserJob implements Job {

	PdfReportLinksReader pdfReportLinksReader = new PdfReportLinksReader();

	CompanyNameParser companyNameParser = new CompanyNameParser();

	SendMail sendMail = new SendMail();

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			doparser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doparser() {
		List<PdfReportLinks> pdfReportLinks = pdfReportLinksReader.readPdfLinkByRank(0);
		for (PdfReportLinks pdfReportLink : pdfReportLinks) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				String html = null;
				String chagelink = changeHanzi(pdfReportLink.getLink());
				if (chagelink.endsWith("/")) {
				} else {
					html = Fetcher.getInstance().get(chagelink, "gbk");
					if (html == null || html.contains("MirrorFailed") || html.contains("NoSuchKey")) {
					} else {
						String result = companyNameParser.parserCompanyName(html);
						if (result == null || result.isEmpty()) {
						} else {
							result = result.replace("#", "");
							if (result.endsWith("公司")) {
								map.put("company_name", result);
							}
						}
					}
				}
				map.put("notice_id", pdfReportLink.getNoticeId() + "");
				map.put("link", pdfReportLink.getLink());
				map.put("source_id", pdfReportLink.getCompanyId() + "");
				map.put("propertyName", pdfReportLink.getCompanyName());
				map.put("report_date", pdfReportLink.getPublishDate());
				map.put("up_time", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
				sendJson(map, "pdf_company_approved");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pdfReportLink.setRank(2);
				pdfReportLinksReader.updatePdfReportLinkByid(pdfReportLink);
			}

		}

	}

	/***
	 * 
	 * 
	 * @param object
	 * @param type
	 */
	public void sendJson(Object object, String type) {
		String url = KfConstant.saveJsonIp;
		Map<String, String> params = new HashMap<String, String>();
		Gson gson = new GsonBuilder().create();
		params.put("json", gson.toJson(object));
		params.put("type", type);
		Fetcher.getInstance().postSave(url, params, null, "utf-8");
	}

	public String changeHanzi(String url) {
		char[] tp = url.toCharArray();
		String now = "";
		for (char ch : tp) {
			if (ch >= 0x4E00 && ch <= 0x9FA5) {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (ch == '[') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (ch == ']') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (ch == ' ') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				now += ch;
			}

		}
		return now;
	}

}
