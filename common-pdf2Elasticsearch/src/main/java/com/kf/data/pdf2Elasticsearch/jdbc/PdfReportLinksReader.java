package com.kf.data.pdf2Elasticsearch.jdbc;

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

	/****
	 * 保存公告链接
	 * 
	 * @param pdfReportLinks
	 * @return
	 */
	public int savePdfReportLinks(PdfReportLinks pdfReportLinks) {
		int id = 0;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			id = linkExit(pdfReportLinksMapper, pdfReportLinks);
			if (id == 0) {
				pdfReportLinksMapper.insertSelective(pdfReportLinks);
				id = pdfReportLinks.getId();
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return id;
	}

	/***
	 * 判断链接是否存在
	 * 
	 * @param pdfReportLinksMapper
	 * @param pdfReportLinks
	 * @return
	 */
	private int linkExit(PdfReportLinksMapper pdfReportLinksMapper, PdfReportLinks pdfReportLinks) {
		PdfReportLinksExample example = new PdfReportLinksExample();
		example.or().andNoticeIdEqualTo(pdfReportLinks.getNoticeId());
		List<PdfReportLinks> pdfReportLinkses = pdfReportLinksMapper.selectByExample(example);

		if (pdfReportLinkses.size() > 0) {
			return pdfReportLinkses.get(0).getId();
		}
		return 0;
	}

	/***
	 * 读取没有链接的公告
	 * 
	 * @return
	 */
	public List<PdfReportLinks> readNoLink() {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		List<PdfReportLinks> pdfReportLinks = null;
		try {
			PdfReportLinksMapper pdfReportLinksMapper = sqlSession.getMapper(PdfReportLinksMapper.class);
			PdfReportLinksExample example = new PdfReportLinksExample();
			example.or().andLinkEqualTo("https://static.kaifengdata.com/neeq//");
			example.setOrderByClause("id limit 10");
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
