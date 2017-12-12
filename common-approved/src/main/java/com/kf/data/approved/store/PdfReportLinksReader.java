package com.kf.data.approved.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.mybatis.entity.PdfReportLinksExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfReportLinksMapper;

/****
 * 
 * @Title: PdfReportLinksReader.java
 * @Package com.kf.data.pdf2Elasticsearch.jdbc
 * @Description: 公告链接操作类
 * @author liangyt
 * @date 2017年10月23日 下午5:03:07
 * @version V1.0
 */
public class PdfReportLinksReader {
	/***
	 * 读取没有链接的公告
	 * 
	 * @return
	 */
	public List<PdfReportLinks> readPdfLinkByRank(int rank) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		List<PdfReportLinks> pdfReportLinks = null;
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			PdfReportLinksExample example = new PdfReportLinksExample();
			example.or().andRankEqualTo(rank).andPdfTypeEqualTo("批准挂牌公司");
			example.setOrderByClause("id desc limit 100");
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
	 * 更新没有链接的公告
	 * 
	 * @param pdfReportLink
	 */
	public void updatePdfReportLinkByid(PdfReportLinks pdfReportLink) {

		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			pdfReportLinksMapper.updateByPrimaryKeySelective(pdfReportLink);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}
}
