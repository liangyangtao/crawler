package com.kf.data.pdfparser.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfCodeLink;
import com.kf.data.mybatis.entity.PdfCodeLinkExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfCodeLinkMapper;

/**
 * @Title: PdfCodeLinkWriter.java
 * @Package com.kf.data.jdbc.pdfCode.write
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月8日 下午2:42:15
 * @version V1.0
 */
public class PdfCodeLinkWriter {

	public void updatePdfCodeRankById(int id, int rank) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfCodeLinkMapper pdfCodeLinkMapper = sqlSession.getMapper(PdfCodeLinkMapper.class);
			PdfCodeLinkExample example = new PdfCodeLinkExample();
			example.or().andIdEqualTo(id);
			PdfCodeLink record = new PdfCodeLink();
			record.setId(id);
			record.setRank(rank);
			pdfCodeLinkMapper.updateByExampleSelective(record, example);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}

	}

	public boolean savePdfCodeLink(PdfCodeLink pdfCodeLink) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfCodeLinkMapper pdfCodeLinkMapper = sqlSession.getMapper(PdfCodeLinkMapper.class);
			pdfCodeLinkMapper.insertSelective(pdfCodeLink);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return false;
	}

	public boolean checkExit(PdfCodeLink pdfCodeLink) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		List<PdfCodeLink> results = null;
		try {
			PdfCodeLinkMapper pdfCodeLinkMapper = sqlSession.getMapper(PdfCodeLinkMapper.class);
			PdfCodeLinkExample example = new PdfCodeLinkExample();
			example.or().andUnmdEqualTo(pdfCodeLink.getUnmd());
			results = pdfCodeLinkMapper.selectByExample(example);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		if (results.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
