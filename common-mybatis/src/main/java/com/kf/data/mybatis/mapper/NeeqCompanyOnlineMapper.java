package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.NeeqCompanyOnline;
import com.kf.data.mybatis.entity.NeeqCompanyOnlineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NeeqCompanyOnlineMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int countByExample(NeeqCompanyOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int deleteByExample(NeeqCompanyOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int insert(NeeqCompanyOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int insertSelective(NeeqCompanyOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    List<NeeqCompanyOnline> selectByExampleWithBLOBs(NeeqCompanyOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    List<NeeqCompanyOnline> selectByExample(NeeqCompanyOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    NeeqCompanyOnline selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int updateByExampleSelective(@Param("record") NeeqCompanyOnline record, @Param("example") NeeqCompanyOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int updateByExampleWithBLOBs(@Param("record") NeeqCompanyOnline record, @Param("example") NeeqCompanyOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int updateByExample(@Param("record") NeeqCompanyOnline record, @Param("example") NeeqCompanyOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int updateByPrimaryKeySelective(NeeqCompanyOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(NeeqCompanyOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_company
     *
     * @mbggenerated Tue Dec 19 10:53:09 CST 2017
     */
    int updateByPrimaryKey(NeeqCompanyOnline record);
}