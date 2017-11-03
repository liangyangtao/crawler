package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.TycCompanyCaseCrawler;
import com.kf.data.mybatis.entity.TycCompanyCaseCrawlerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TycCompanyCaseCrawlerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int countByExample(TycCompanyCaseCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int deleteByExample(TycCompanyCaseCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int insert(TycCompanyCaseCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int insertSelective(TycCompanyCaseCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    List<TycCompanyCaseCrawler> selectByExampleWithBLOBs(TycCompanyCaseCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    List<TycCompanyCaseCrawler> selectByExample(TycCompanyCaseCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    TycCompanyCaseCrawler selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int updateByExampleSelective(@Param("record") TycCompanyCaseCrawler record, @Param("example") TycCompanyCaseCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int updateByExampleWithBLOBs(@Param("record") TycCompanyCaseCrawler record, @Param("example") TycCompanyCaseCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int updateByExample(@Param("record") TycCompanyCaseCrawler record, @Param("example") TycCompanyCaseCrawlerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int updateByPrimaryKeySelective(TycCompanyCaseCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(TycCompanyCaseCrawler record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tyc_company_case
     *
     * @mbggenerated Wed Nov 01 10:41:50 CST 2017
     */
    int updateByPrimaryKey(TycCompanyCaseCrawler record);
}