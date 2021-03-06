package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.PdfClassifyKeywordsCrawler;
import com.kf.data.mybatis.entity.PdfClassifyKeywordsCrawlerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PdfClassifyKeywordsCrawlerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int countByExample(PdfClassifyKeywordsCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int deleteByExample(PdfClassifyKeywordsCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int insert(PdfClassifyKeywordsCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int insertSelective(PdfClassifyKeywordsCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    List<PdfClassifyKeywordsCrawler> selectByExample(PdfClassifyKeywordsCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    PdfClassifyKeywordsCrawler selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int updateByExampleSelective(@Param("record") PdfClassifyKeywordsCrawler record, @Param("example") PdfClassifyKeywordsCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int updateByExample(@Param("record") PdfClassifyKeywordsCrawler record, @Param("example") PdfClassifyKeywordsCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int updateByPrimaryKeySelective(PdfClassifyKeywordsCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_classify_keywords
     *
     * @mbggenerated Tue Oct 17 14:11:51 CST 2017
     */
    int updateByPrimaryKey(PdfClassifyKeywordsCrawler record);
}