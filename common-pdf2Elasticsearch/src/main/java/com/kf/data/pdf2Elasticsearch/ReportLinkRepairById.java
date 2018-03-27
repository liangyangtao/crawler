package com.kf.data.pdf2Elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mybatis.entity.NeeqNotice;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdf2Elasticsearch.jdbc.NeeqNoticeReader;
import com.kf.data.pdf2Elasticsearch.jdbc.PdfReportLinksReader;

/****
 * 
 * @Title: ReportLinkRepair.java
 * @Package com.kf.data.pdf2Elasticsearch
 * @Description: 补充没有的链接
 * @author liangyt
 * @date 2017年10月23日 下午4:38:45
 * @version V1.0
 */
public class ReportLinkRepairById {

	private static PdfReportLinksReader pdfReportLinksReader = new PdfReportLinksReader();
	private static NeeqNoticeReader neeqNoticeReader = new NeeqNoticeReader();

	public static void main(String[] args) {
		KfConstant.init();
		List<Integer> noticeids = new ArrayList<Integer>();
		try {
			String path = ReportLinkRepairById.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(path + File.separator + "ids.txt");
			if (file.exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
				String line = "";
				while ((line = br.readLine()) != null) {
					noticeids.add(Integer.parseInt(line));
				}
				br.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(noticeids.size());
		for (int id : noticeids) {
			List<PdfReportLinks> pdfReportLinks = pdfReportLinksReader.readLinkByNoticeid(id);
			if (pdfReportLinks.size() == 0) {
				continue;
			}
			for (PdfReportLinks pdfReportLink : pdfReportLinks) {
				System.out.println(pdfReportLink.getId());
				StringBuffer canshu = new StringBuffer();
				canshu.append(
						"select id, downurl as pdf_link ,concat('https://static.kaifengdata.com/neeq/',md5,'/',htmlfilepath) as link ,pdftitle as title from pdf2html_storm_message_queue where   bucket='neeq' and ");
				canshu.append(" neeq_company_notice_id  = ");
				canshu.append(pdfReportLink.getNoticeId());
				List<NeeqNotice> neeqNotices = neeqNoticeReader.readNeeqNotice(canshu.toString());
				if (neeqNotices.size() > 0) {
					for (NeeqNotice neeqNotice : neeqNotices) {
						String link = neeqNotice.getLink();
						if (link.endsWith("/")) {

						} else {
							System.out.println(link);
							pdfReportLink.setLink(link);
						}
						pdfReportLink.setRank(2);
						pdfReportLinksReader.updatePdfReportLinkByid(pdfReportLink);

					}
				} else {
					pdfReportLink.setRank(2);
					pdfReportLinksReader.updatePdfReportLinkByid(pdfReportLink);
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
