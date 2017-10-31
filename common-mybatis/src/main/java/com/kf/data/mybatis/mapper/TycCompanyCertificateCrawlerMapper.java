package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.TycCompanyCertificateCrawler;
import com.kf.data.mybatis.entity.TycCompanyCertificateCrawlerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TycCompanyCertificateCrawlerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int countByExample(TycCompanyCertificateCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int deleteByExample(TycCompanyCertificateCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int insert(TycCompanyCertificateCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int insertSelective(TycCompanyCertificateCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    List<TycCompanyCertificateCrawler> selectByExample(TycCompanyCertificateCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    TycCompanyCertificateCrawler selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int updateByExampleSelective(@Param("record") TycCompanyCertificateCrawler record, @Param("example") TycCompanyCertificateCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int updateByExample(@Param("record") TycCompanyCertificateCrawler record, @Param("example") TycCompanyCertificateCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int updateByPrimaryKeySelective(TycCompanyCertificateCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_certificate
     *
     * @mbggenerated Tue Oct 31 17:09:08 CST 2017
     */
    int updateByPrimaryKey(TycCompanyCertificateCrawler record);
}