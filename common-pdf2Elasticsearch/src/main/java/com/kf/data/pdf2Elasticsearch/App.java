package com.kf.data.pdf2Elasticsearch;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.fetcher.tools.Md5Tools;
import com.kf.data.mybatis.entity.NeeqCompanyNotice;
import com.kf.data.mybatis.entity.NeeqNotice;
import com.kf.data.mybatis.entity.PdfCodeChapter;
import com.kf.data.mybatis.entity.PdfReportLinkText;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdf2Elasticsearch.entity.PdfLinkEsEntity;
import com.kf.data.pdf2Elasticsearch.entity.TitleTreeNode;
import com.kf.data.pdf2Elasticsearch.jdbc.EsEntitySaver;
import com.kf.data.pdf2Elasticsearch.jdbc.NeeqCompanyNoticeReader;
import com.kf.data.pdf2Elasticsearch.jdbc.NeeqNoticeReader;
import com.kf.data.pdf2Elasticsearch.jdbc.PdfCodeChapterWriter;
import com.kf.data.pdf2Elasticsearch.jdbc.PdfReportLinkTextReader;
import com.kf.data.pdf2Elasticsearch.jdbc.PdfReportLinksReader;
import com.kf.data.pdf2Elasticsearch.titleParser.PdfTitleParser;

/**
 * Hello world!
 *
 */
public class App {

	static final String filePath = "F:\\oss\\sync\\neeq\\";

	public static void main(String[] args) {
		KfConstant.init();
		long index = 2624467;
		while (true) {
			List<NeeqCompanyNotice> neeqCompanyNotices = new NeeqCompanyNoticeReader()
					.readNeeqCompanyNoticesByReportDate("2017-06-30", index);
			if (neeqCompanyNotices.size() == 0) {
				break;
			}
			for (NeeqCompanyNotice neeqCompanyNotice : neeqCompanyNotices) {
				index = neeqCompanyNotice.getId();
				String publishDate = neeqCompanyNotice.getPublishdate();
				Long id = neeqCompanyNotice.getId();
				StringBuffer canshu = new StringBuffer();
				canshu.append(
						"select id, downurl as pdf_link ,concat('https://static.kaifengdata.com/neeq/',md5,'/',htmlfilepath) as link ,pdftitle as title from pdf2html_storm_message_queue where ");
				canshu.append(" neeq_company_notice_id  = ");
				canshu.append(id);
				List<NeeqNotice> neeqNotices = new NeeqNoticeReader().readNeeqNotice(canshu.toString());
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
						// if (link.endsWith(".html")) {
						// link = link.replace(".html", ".htm");
						// }
						System.out.println(link);
						String filename = link.replace("https://static.kaifengdata.com/neeq/", "");
						File input = new File(filePath + filename);
						String html = null;
						try {
							html = Jsoup.parse(input, "gbk").toString();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (html == null) {
							continue;
						}
						if (html.contains("<Code>") && html.contains("<Message>")) {
							continue;
						}
						try {
							pdfCodeLink.setContent(html);
							new EsEntitySaver().saveEsEntity(pdfCodeLink);
						} catch (Exception e) {
							e.printStackTrace();
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
							int pdfreportId = new PdfReportLinksReader().savePdfReportLinks(pdfReportLinks);
							// if (pdfreportId > 0) {
							// try {
							// PdfReportLinkText pdfReportLinkText = new
							// PdfReportLinkText();
							// pdfReportLinkText.setId(pdfreportId);
							// pdfReportLinkText.setText(html);
							// new
							// PdfReportLinkTextReader().savePdfReportLinkText(pdfReportLinkText);
							// } catch (Exception e) {
							// e.printStackTrace();
							// }
							// }

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} else {
				}
			}

		}

	}

	public static String changeHanzi(String url) {
		char[] tp = url.toCharArray();
		String now = "";
		for (char ch : tp) {
			if (ch >= 0x4E00 && ch <= 0x9FA5) {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == '[') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ']') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ' ') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				now += ch;
			}

		}
		return now;
	}
}
