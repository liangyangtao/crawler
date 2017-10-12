package com.kf.data.pdfparser.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.mybatis.entity.PdfReportLinksExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportLinksMapper;

/****
 * 
 * @Title: PdfReportLinksReader.java
 * @Package com.kf.data.pdfparser.jdbc
 * @Description: 读取要解析的链接
 * @author liangyt
 * @date 2017年10月12日 上午10:54:21
 * @version V1.0
 */
public class PdfReportLinksReader {

	/****
	 * 读取链接根据pdftype 和rank
	 * 
	 * @param pdftype
	 * @param rank
	 * @return
	 */
	public List<PdfReportLinks> readerPdfCodeLinkByTypeAndRank(String pdftype, int rank) {
		List<PdfReportLinks> pdfReportLinks = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			PdfReportLinksExample example = new PdfReportLinksExample();
			if (pdftype.contains("_")) {
				pdftype = pdftype.split("_")[0];
			}
			example.or().andPdfTypeEqualTo(pdftype).andRankEqualTo(rank);
			example.setOrderByClause("id desc limit 32");
			pdfReportLinks = pdfReportLinksMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfReportLinks;

	}

	/****
	 * 读取pdfLink 根据rank
	 * 
	 * @param rank
	 * @return
	 */
	public List<PdfReportLinks> readerPdfCodeLinkByRank(int rank) {
		List<PdfReportLinks> pdfReportLinks = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			PdfReportLinksExample example = new PdfReportLinksExample();
			example.or().andRankEqualTo(rank);
			example.setOrderByClause("id desc limit 32");
			pdfReportLinks = pdfReportLinksMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfReportLinks;

	}

}
