package com.kf.data.mybatis.entity;

public class TycCompanyPatentCrawlerWithBLOBs extends TycCompanyPatentCrawler {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_patent.address
	 * @mbggenerated  Fri Sep 29 13:59:37 CST 2017
	 */
	private String address;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_patent.summary
	 * @mbggenerated  Fri Sep 29 13:59:37 CST 2017
	 */
	private String summary;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_patent.address
	 * @return  the value of tyc_company_patent.address
	 * @mbggenerated  Fri Sep 29 13:59:37 CST 2017
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_patent.address
	 * @param address  the value for tyc_company_patent.address
	 * @mbggenerated  Fri Sep 29 13:59:37 CST 2017
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_patent.summary
	 * @return  the value of tyc_company_patent.summary
	 * @mbggenerated  Fri Sep 29 13:59:37 CST 2017
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_patent.summary
	 * @param summary  the value for tyc_company_patent.summary
	 * @mbggenerated  Fri Sep 29 13:59:37 CST 2017
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
}