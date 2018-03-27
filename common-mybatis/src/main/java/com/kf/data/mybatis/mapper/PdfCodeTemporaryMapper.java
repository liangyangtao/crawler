package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.PdfCodeTemporary;
import com.kf.data.mybatis.entity.PdfCodeTemporaryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PdfCodeTemporaryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int countByExample(PdfCodeTemporaryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int deleteByExample(PdfCodeTemporaryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int insert(PdfCodeTemporary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int insertSelective(PdfCodeTemporary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    List<PdfCodeTemporary> selectByExample(PdfCodeTemporaryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    PdfCodeTemporary selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int updateByExampleSelective(@Param("record") PdfCodeTemporary record, @Param("example") PdfCodeTemporaryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int updateByExample(@Param("record") PdfCodeTemporary record, @Param("example") PdfCodeTemporaryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int updateByPrimaryKeySelective(PdfCodeTemporary record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pdf_code_temporary
     *
     * @mbggenerated Mon Sep 18 16:39:19 CST 2017
     */
    int updateByPrimaryKey(PdfCodeTemporary record);
}