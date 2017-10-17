package com.kf.data.pdf2Elasticsearch.jdbc;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.kf.data.mybatis.entity.PdfClassifyKeywordsCrawler;
import com.kf.data.mybatis.entity.PdfClassifyKeywordsCrawlerExample;
import com.kf.data.mybatis.factory.DynamicConnectionFactory;
import com.kf.data.mybatis.mapper.PdfClassifyKeywordsCrawlerMapper;

/***
 * 
 * @Title: PdfClassifyKeywordsReader.java
 * @Package com.kf.data.pdf2Elasticsearch.jdbc
 * @Description: 读取分类条件
 * @author liangyt
 * @date 2017年10月17日 下午2:15:51
 * @version V1.0
 */
public class PdfClassifyKeywordsReader {

	/***
	 * 从数据库读取分类
	 * 
	 * @return
	 */
	public List<PdfClassifyKeywordsCrawler> readerPdfClassifyKeywords() {
		List<PdfClassifyKeywordsCrawler> pdfClassifyKeywordsCrawler = null;
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfClassifyKeywordsCrawlerMapper PdfClassifyKeywordsCrawlerMapper = sqlSession
					.getMapper(PdfClassifyKeywordsCrawlerMapper.class);
			PdfClassifyKeywordsCrawlerExample example = new PdfClassifyKeywordsCrawlerExample();
			pdfClassifyKeywordsCrawler = PdfClassifyKeywordsCrawlerMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
		return pdfClassifyKeywordsCrawler;

	}

	/****
	 * 更新分类
	 * 
	 * @param id
	 * @param indexId
	 */
	public void updatePdfClassifyKeywordsIndexById(int id, int indexId) {
		SqlSession sqlSession = DynamicConnectionFactory.getInstanceSessionFactory("crawlerMysql").openSession();
		try {
			PdfClassifyKeywordsCrawlerMapper pdfClassifyKeywordsCrawlerMapper = sqlSession
					.getMapper(PdfClassifyKeywordsCrawlerMapper.class);
			PdfClassifyKeywordsCrawlerExample example = new PdfClassifyKeywordsCrawlerExample();
			example.or().andIdEqualTo(id);
			PdfClassifyKeywordsCrawler pdfclassifyKeyword = new PdfClassifyKeywordsCrawler();
			pdfclassifyKeyword.setId(id);
			pdfclassifyKeyword.setIndexId(indexId);
			pdfClassifyKeywordsCrawlerMapper.updateByExampleSelective(pdfclassifyKeyword, example);
			sqlSession.commit(true);
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback(true);
		} finally {
			sqlSession.close();
		}
	}

}
