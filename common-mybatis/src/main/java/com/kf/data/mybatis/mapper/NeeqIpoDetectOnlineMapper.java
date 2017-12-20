package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.NeeqIpoDetectOnline;
import com.kf.data.mybatis.entity.NeeqIpoDetectOnlineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NeeqIpoDetectOnlineMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int countByExample(NeeqIpoDetectOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int deleteByExample(NeeqIpoDetectOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int insert(NeeqIpoDetectOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int insertSelective(NeeqIpoDetectOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    List<NeeqIpoDetectOnline> selectByExample(NeeqIpoDetectOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    NeeqIpoDetectOnline selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int updateByExampleSelective(@Param("record") NeeqIpoDetectOnline record, @Param("example") NeeqIpoDetectOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int updateByExample(@Param("record") NeeqIpoDetectOnline record, @Param("example") NeeqIpoDetectOnlineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int updateByPrimaryKeySelective(NeeqIpoDetectOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neeq_ipo_detect
     *
     * @mbggenerated Tue Dec 19 10:52:23 CST 2017
     */
    int updateByPrimaryKey(NeeqIpoDetectOnline record);
}