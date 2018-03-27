package com.kf.data.mybatis.entity;

import java.util.Date;

public class TycCompanyTaxArrearsCrawler {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.id
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.taxpayer_identifier
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String taxpayerIdentifier;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.dt_effective
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String dtEffective;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.tax_arrears_amount
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String taxArrearsAmount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.tax_authority
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String taxAuthority;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.status
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private Byte status;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.created_at
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private Date createdAt;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.updated_at
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private Date updatedAt;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.tax_category
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String taxCategory;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.company_id
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String companyId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.company_name
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String companyName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column tyc_company_tax_arrears.tax_balance_amount
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	private String taxBalanceAmount;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.id
	 * @return  the value of tyc_company_tax_arrears.id
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.id
	 * @param id  the value for tyc_company_tax_arrears.id
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.taxpayer_identifier
	 * @return  the value of tyc_company_tax_arrears.taxpayer_identifier
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getTaxpayerIdentifier() {
		return taxpayerIdentifier;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.taxpayer_identifier
	 * @param taxpayerIdentifier  the value for tyc_company_tax_arrears.taxpayer_identifier
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setTaxpayerIdentifier(String taxpayerIdentifier) {
		this.taxpayerIdentifier = taxpayerIdentifier;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.dt_effective
	 * @return  the value of tyc_company_tax_arrears.dt_effective
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getDtEffective() {
		return dtEffective;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.dt_effective
	 * @param dtEffective  the value for tyc_company_tax_arrears.dt_effective
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setDtEffective(String dtEffective) {
		this.dtEffective = dtEffective;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.tax_arrears_amount
	 * @return  the value of tyc_company_tax_arrears.tax_arrears_amount
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getTaxArrearsAmount() {
		return taxArrearsAmount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.tax_arrears_amount
	 * @param taxArrearsAmount  the value for tyc_company_tax_arrears.tax_arrears_amount
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setTaxArrearsAmount(String taxArrearsAmount) {
		this.taxArrearsAmount = taxArrearsAmount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.tax_authority
	 * @return  the value of tyc_company_tax_arrears.tax_authority
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getTaxAuthority() {
		return taxAuthority;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.tax_authority
	 * @param taxAuthority  the value for tyc_company_tax_arrears.tax_authority
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setTaxAuthority(String taxAuthority) {
		this.taxAuthority = taxAuthority;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.status
	 * @return  the value of tyc_company_tax_arrears.status
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.status
	 * @param status  the value for tyc_company_tax_arrears.status
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.created_at
	 * @return  the value of tyc_company_tax_arrears.created_at
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.created_at
	 * @param createdAt  the value for tyc_company_tax_arrears.created_at
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.updated_at
	 * @return  the value of tyc_company_tax_arrears.updated_at
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.updated_at
	 * @param updatedAt  the value for tyc_company_tax_arrears.updated_at
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.tax_category
	 * @return  the value of tyc_company_tax_arrears.tax_category
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getTaxCategory() {
		return taxCategory;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.tax_category
	 * @param taxCategory  the value for tyc_company_tax_arrears.tax_category
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setTaxCategory(String taxCategory) {
		this.taxCategory = taxCategory;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.company_id
	 * @return  the value of tyc_company_tax_arrears.company_id
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.company_id
	 * @param companyId  the value for tyc_company_tax_arrears.company_id
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.company_name
	 * @return  the value of tyc_company_tax_arrears.company_name
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.company_name
	 * @param companyName  the value for tyc_company_tax_arrears.company_name
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column tyc_company_tax_arrears.tax_balance_amount
	 * @return  the value of tyc_company_tax_arrears.tax_balance_amount
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public String getTaxBalanceAmount() {
		return taxBalanceAmount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column tyc_company_tax_arrears.tax_balance_amount
	 * @param taxBalanceAmount  the value for tyc_company_tax_arrears.tax_balance_amount
	 * @mbggenerated  Tue Oct 31 15:25:27 CST 2017
	 */
	public void setTaxBalanceAmount(String taxBalanceAmount) {
		this.taxBalanceAmount = taxBalanceAmount;
	}
}