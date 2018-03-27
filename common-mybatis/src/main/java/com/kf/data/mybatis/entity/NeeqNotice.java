package com.kf.data.mybatis.entity;

/**
 * @Title: NeeqNotice.java
 * @Package com.kf.data.mybatis.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月19日 下午5:40:25
 * @version V1.0
 */
public class NeeqNotice {
	private int id;
	private String notice_id;
	private String company_name;
	private String title;
	private String link;
	private String pdf_link;
	private String publish_date;

	public String getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPdf_link() {
		return pdf_link;
	}

	public void setPdf_link(String pdf_link) {
		this.pdf_link = pdf_link;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
