package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.NeeqCompanyMainBusinessOnline;
import com.kf.data.mybatis.entity.NeeqCompanyMainBusinessOnlineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NeeqCompanyMainBusinessOnlineMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int countByExample(NeeqCompanyMainBusinessOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int deleteByExample(NeeqCompanyMainBusinessOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int insert(NeeqCompanyMainBusinessOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int insertSelective(NeeqCompanyMainBusinessOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    List<NeeqCompanyMainBusinessOnline> selectByExampleWithBLOBs(NeeqCompanyMainBusinessOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    List<NeeqCompanyMainBusinessOnline> selectByExample(NeeqCompanyMainBusinessOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    NeeqCompanyMainBusinessOnline selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int updateByExampleSelective(@Param("record") NeeqCompanyMainBusinessOnline record, @Param("example") NeeqCompanyMainBusinessOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int updateByExampleWithBLOBs(@Param("record") NeeqCompanyMainBusinessOnline record, @Param("example") NeeqCompanyMainBusinessOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int updateByExample(@Param("record") NeeqCompanyMainBusinessOnline record, @Param("example") NeeqCompanyMainBusinessOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int updateByPrimaryKeySelective(NeeqCompanyMainBusinessOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(NeeqCompanyMainBusinessOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company_main_business
     *
     * @mbggenerated Tue Dec 19 10:37:56 CST 2017
     */
    int updateByPrimaryKey(NeeqCompanyMainBusinessOnline record);
}