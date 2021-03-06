package com.kf.data.mybatis.mapper;

import com.kf.data.mybatis.entity.PdfReportLinks;
import com.kf.data.mybatis.entity.PdfReportLinksExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PdfReportLinksMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int countByExample(PdfReportLinksExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int deleteByExample(PdfReportLinksExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int insert(PdfReportLinks record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int insertSelective(PdfReportLinks record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	List<PdfReportLinks> selectByExample(PdfReportLinksExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	PdfReportLinks selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int updateByExampleSelective(@Param("record") PdfReportLinks record,
			@Param("example") PdfReportLinksExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int updateByExample(@Param("record") PdfReportLinks record, @Param("example") PdfReportLinksExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int updateByPrimaryKeySelective(PdfReportLinks record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table pdf_report_links
	 * @mbggenerated  Thu Sep 07 17:30:56 CST 2017
	 */
	int updateByPrimaryKey(PdfReportLinks record);
}