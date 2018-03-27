package com.kf.data.pdfparser.jdbc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.mybatis.entity.PdfReportLinkText;
import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportLinkTextMapper;

public class PdfReportLinksTextReader {

	public String readHtmlByid(PdfReportLinks pdfReportLinks) {
		String html = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinkTextMapper pdfReportLinkTextMapper = sqlSession.getMapper(PdfReportLinkTextMapper.class);
			PdfReportLinkText pdfReportLinkTexts = pdfReportLinkTextMapper.selectByPrimaryKey(pdfReportLinks.getId());
			if (pdfReportLinkTexts != null) {
				html = pdfReportLinkTexts.getText();
			} else {
				String link = pdfReportLinks.getLink();
				link = changeHanzi(link);
				String html2 = Fetcher.getInstance().get(link);
				if (html2 == null) {
					return null;
				}
				if (html2.contains("<Code>") && html2.contains("<Message>")) {
					return null;
				}

				try {
					PdfReportLinkText pdfReportLinkText = new PdfReportLinkText();
					pdfReportLinkText.setId(pdfReportLinks.getId());
					pdfReportLinkText.setText(html);
					savePdfReportLinkText(pdfReportLinkText);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (html2 != null) {
					return html2;
				}

			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return html;

	}

	public static String changeHanzi(String url) {
		char[] tp = url.toCharArray();
		String now = "";
		for (char ch : tp) {
			if (ch >= 0x4E00 && ch <= 0x9FA5) {
				try {
					now += URLEncoder.encode(ch + "", "gbk");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == '[') {
				try {
					now += URLEncoder.encode(ch + "", "gbk");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ']') {
				try {
					now += URLEncoder.encode(ch + "", "gbk");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ' ') {
				try {
					now += URLEncoder.encode(ch + "", "gbk");
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

	public void savePdfReportLinkText(PdfReportLinkText pdfReportLinkText) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinkTextMapper pdfReportLinkTextMapper = sqlSession.getMapper(PdfReportLinkTextMapper.class);
			if (!linkExit(pdfReportLinkTextMapper, pdfReportLinkText)) {
				pdfReportLinkTextMapper.insertSelective(pdfReportLinkText);
			} else {
				pdfReportLinkTextMapper.updateByPrimaryKeyWithBLOBs(pdfReportLinkText);
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

	private boolean linkExit(PdfReportLinkTextMapper pdfReportLinkTextMapper, PdfReportLinkText pdfReportLinkText) {
		if (pdfReportLinkTextMapper.selectByPrimaryKey(pdfReportLinkText.getId()) != null) {
			return true;
		}
		return false;
	}
}
