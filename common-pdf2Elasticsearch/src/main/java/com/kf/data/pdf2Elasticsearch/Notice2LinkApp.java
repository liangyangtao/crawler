package com.kf.data.pdf2Elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.mybatis.entity.NeeqCompanyNotice;
import com.kf.data.mybatis.entity.NeeqNotice;
import com.kf.data.mybatis.entity.PdfClassifyKeywordsCrawler;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.pdf2Elasticsearch.jdbc.NeeqCompanyNoticeReader;
import com.kf.data.pdf2Elasticsearch.jdbc.NeeqNoticeReader;
import com.kf.data.pdf2Elasticsearch.jdbc.PdfClassifyKeywordsReader;
import com.kf.data.pdf2Elasticsearch.jdbc.PdfReportLinksReader;

/***
 * 
 * @Title: Notice2LinkApp.java
 * @Package com.kf.data.pdf2Elasticsearch
 * @Description: Notic 获取链接 到 pdf_report_link
 * @author liangyt
 * @date 2017年10月17日 下午1:37:17
 * @version V1.0
 */
public class Notice2LinkApp {

	private static NeeqCompanyNoticeReader neeqCompanyNoticeReader = new NeeqCompanyNoticeReader();
	private static NeeqNoticeReader neeqNoticeReader = new NeeqNoticeReader();
	private static PdfReportLinksReader pdfReportLinksReader = new PdfReportLinksReader();

	public static void main(String[] args) {
		KfConstant.init();
		Long id = null;
		try {
			String path = Notice2LinkApp.class.getClassLoader().getResource("").toURI().getPath();
			File file = new File(path + File.separator + "id.txt");
			if (file.exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
				String line = "";
				line = br.readLine();
				id = Long.parseLong(line);
				br.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			id = 0L;
		}
		while (true) {
			try {
				List<NeeqCompanyNotice> neeqCompanyNotices = neeqCompanyNoticeReader.readNeeqCompanyNoticesByIndex(id);
				if (neeqCompanyNotices.size() > 0) {
					for (NeeqCompanyNotice neeqCompanyNotice : neeqCompanyNotices) {
						id = neeqCompanyNotice.getId();
						String title = neeqCompanyNotice.getDisclosuretitle();
						Date date = neeqCompanyNotice.getReportDate();

						String pdfType = null;
						if (date != null) {
							// 如果是年报和半年报直接查找
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date);
							int month = calendar.get(Calendar.MONTH) + 1;
							if (month == 12) {
								pdfType = "年报";
							} else if (month == 6) {
								pdfType = "半年报";
							}
							fillPdfLink(neeqCompanyNotice, pdfType);
						} else {
							List<PdfClassifyKeywordsCrawler> pdftypeRules = new PdfClassifyKeywordsReader()
									.readerPdfClassifyKeywords();
							for (PdfClassifyKeywordsCrawler pdfClassifyKeywordsCrawler : pdftypeRules) {
								if (pdfClassifyKeywordsCrawler.getClassificationName().contains("年报")) {
									continue;
								}
								String mustKeyword = pdfClassifyKeywordsCrawler.getKeyWordsAnd();
								boolean isFill = false;
								if (mustKeyword != null && !mustKeyword.isEmpty()) {
									String temp[] = mustKeyword.split(",");
									for (String string : temp) {
										if (title.contains(string)) {
											isFill = true;
										} else {
											isFill = false;
											break;
										}
									}
								}
								if (isFill) {
									String noNeedKeyword = pdfClassifyKeywordsCrawler.getKeyWordsExclude();
									if (noNeedKeyword != null && !noNeedKeyword.isEmpty()) {
										String temp[] = noNeedKeyword.split(",");
										for (String string : temp) {
											if (title.contains(string)) {
												isFill = false;
												break;
											} else {
												isFill = true;
											}
										}

									}
									if (isFill) {
										String anyKeyword = pdfClassifyKeywordsCrawler.getKeyWordsOr();
										if (anyKeyword != null && !anyKeyword.isEmpty()) {
											String temp[] = anyKeyword.split(",");
											for (String string : temp) {
												if (title.contains(string)) {
													pdfType = pdfClassifyKeywordsCrawler.getClassificationName();
													fillPdfLink(neeqCompanyNotice, pdfType);
													break;

												}
											}

										}
									}

								}

							}

						}

					}
					neeqCompanyNotices.clear();
					neeqCompanyNotices = null;
				} else {
					try {
						Thread.sleep(6 * 60 * 60 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} finally {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/****
	 * 从 pdf2html_storm_message_queue 读取pdflink 到pdfreport link
	 * 
	 * @param neeqCompanyNotice
	 */
	private static void fillPdfLink(NeeqCompanyNotice neeqCompanyNotice, String pdfType) {
		StringBuffer canshu = new StringBuffer();
		canshu.append(
				"select id, downurl as pdf_link ,concat('https://static.kaifengdata.com/neeq/',md5,'/',htmlfilepath) as link ,pdftitle as title from pdf2html_storm_message_queue where   bucket='neeq' and ");
		canshu.append(" neeq_company_notice_id  = ");
		canshu.append(neeqCompanyNotice.getId());
		List<NeeqNotice> neeqNotices = neeqNoticeReader.readNeeqNotice(canshu.toString());
		if (neeqNotices.size() > 0) {
			for (NeeqNotice neeqNotice : neeqNotices) {
				String link = neeqNotice.getLink();
				if (link == null || link.isEmpty()) {
					continue;
				}
				if (!link.startsWith("http")) {
					link = "http:" + link;
				}
				System.out.println(link);
				try {
					PdfReportLinks pdfReportLinks = new PdfReportLinks();
					pdfReportLinks.setCompanyId(Integer.parseInt(neeqCompanyNotice.getCompanycd()));
					pdfReportLinks.setCompanyName(neeqCompanyNotice.getCompanyname());
					// pdfReportLinks.setId(id);
					pdfReportLinks.setLink(link);
					pdfReportLinks.setNoticeId(Integer.parseInt(neeqCompanyNotice.getId() + ""));
					pdfReportLinks.setPdfLink(neeqNotice.getPdf_link());
					pdfReportLinks.setPdfType(pdfType);
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
			}

		}
		neeqNotices.clear();
		neeqNotices = null;

	}

}
