package com.kf.data.pdf2Elasticsearch;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.kf.data.fetcher.tools.DocumentSimpler;
import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.fetcher.tools.Md5Tools;
import com.kf.data.mybatis.entity.NeeqCompanyNotice;
import com.kf.data.mybatis.entity.NeeqNotice;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdf2Elasticsearch.entity.PdfLinkEsEntity;
import com.kf.data.pdf2Elasticsearch.jdbc.EsEntitySaver;
import com.kf.data.pdf2Elasticsearch.jdbc.NeeqCompanyNoticeReader;
import com.kf.data.pdf2Elasticsearch.jdbc.NeeqNoticeReader;
import com.kf.data.pdf2Elasticsearch.jdbc.PdfReportLinksReader;

/**
 * Hello world!
 *
 */
public class App {

	static final String filePath = "F:\\oss\\sync\\neeq\\";

	public static void main(String[] args) {
		KfConstant.init();

		NeeqCompanyNoticeReader neeqCompanyNoticeReader = new NeeqCompanyNoticeReader();
		NeeqNoticeReader neeqNoticeReader = new NeeqNoticeReader();
		EsEntitySaver esEntitySaver = new EsEntitySaver();
		PdfReportLinksReader pdfReportLinksReader = new PdfReportLinksReader();
		File input = null;
		Long index = 2610841l;
		while (true) {
			try {
				List<NeeqCompanyNotice> neeqCompanyNotices = neeqCompanyNoticeReader
						.readNeeqCompanyNoticesByReportDate("2017-06-30", index);
				for (NeeqCompanyNotice neeqCompanyNotice : neeqCompanyNotices) {
					String publishDate = neeqCompanyNotice.getPublishdate();
					Long id = neeqCompanyNotice.getId();
					index = neeqCompanyNotice.getId();
					StringBuffer canshu = new StringBuffer();
					canshu.append(
							"select id, downurl as pdf_link ,concat('https://static.kaifengdata.com/neeq/',md5,'/',htmlfilepath) as link ,pdftitle as title from pdf2html_storm_message_queue where ");
					canshu.append(" neeq_company_notice_id  = ");
					canshu.append(id);
					List<NeeqNotice> neeqNotices = neeqNoticeReader.readNeeqNotice(canshu.toString());
					if (neeqNotices.size() > 0) {
						for (NeeqNotice neeqNotice : neeqNotices) {
							PdfLinkEsEntity pdfCodeLink = new PdfLinkEsEntity();
							pdfCodeLink.setCompanyCode(neeqCompanyNotice.getCompanycd());
							pdfCodeLink.setSourceId(neeqNotice.getId());
							pdfCodeLink.setCompanyName(neeqCompanyNotice.getCompanyname());
							// 将html值
							pdfCodeLink.setNoticeId(Integer.parseInt(neeqCompanyNotice.getId() + ""));
							pdfCodeLink.setTitle(neeqNotice.getTitle());
							if (neeqCompanyNotice.getReportDate() == null) {
								pdfCodeLink.setReportDate(new Date().getTime());
							} else {
								pdfCodeLink.setReportDate(neeqCompanyNotice.getReportDate().getTime());
							}
							pdfCodeLink.setPublishDate(publishDate);
							String path = neeqNotice.getPdf_link();
							pdfCodeLink.setPdfLink(path);
							if (!path.startsWith("http")) {
								path = "http:" + path;
							}
							path = path.replace(".htm", "");
							path = path.replace(".html", "");
							pdfCodeLink.setUnmd(Md5Tools.GetMD5Code(path));
							pdfCodeLink.setCompanyName(neeqCompanyNotice.getCompanyname());
							pdfCodeLink.setLink(neeqNotice.getLink());
							String link = pdfCodeLink.getLink();
							if (link == null || link.isEmpty()) {
								continue;
							}
							if (!link.startsWith("http")) {
								link = "http:" + link;
							}
							System.out.println(link);
							String filename = link.replace("https://static.kaifengdata.com/neeq/", "");
							input = new File(filePath + filename);
							try {
								Element cotent = new DocumentSimpler().simpleDocument(Jsoup.parse(input, "gbk"));
								pdfCodeLink.setContent(cotent.toString());
								esEntitySaver.saveEsEntity(pdfCodeLink);
								cotent = null;
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
							try {
								PdfReportLinks pdfReportLinks = new PdfReportLinks();
								pdfReportLinks.setCompanyId(Integer.parseInt(neeqCompanyNotice.getCompanycd()));
								pdfReportLinks.setCompanyName(neeqCompanyNotice.getCompanyname());
								// pdfReportLinks.setId(id);
								pdfReportLinks.setLink(link);
								pdfReportLinks.setNoticeId(Integer.parseInt(neeqCompanyNotice.getId() + ""));
								pdfReportLinks.setPdfLink(neeqNotice.getPdf_link());
								pdfReportLinks.setPdfType("半年报");
								pdfReportLinks.setPublishDate(neeqCompanyNotice.getPublishdate());
								pdfReportLinks.setRank(0);
								pdfReportLinks.setReportDate(neeqCompanyNotice.getReportDate());
								pdfReportLinks.setTitle(neeqNotice.getTitle());
								pdfReportLinksReader.savePdfReportLinks(pdfReportLinks);
								pdfReportLinks = null;
							} catch (Exception e) {
								e.printStackTrace();
							}
							neeqNotice = null;
							pdfCodeLink = null;
						}

					} else {
					}
					neeqNotices.clear();
					neeqNotices = null;
				}
				neeqCompanyNotices.clear();
				neeqCompanyNotices = null;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} finally {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
