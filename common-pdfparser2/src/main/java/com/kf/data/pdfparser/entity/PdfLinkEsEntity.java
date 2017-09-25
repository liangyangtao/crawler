package com.kf.data.pdfparser.entity;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PdfLinkEsEntity implements Serializable {

	private static final long serialVersionUID = -5229941868811295565L;
	// private Integer id;
	private String link;
	private Integer sourceId;
	private String pdfLink;
	private String title;
	private String companyName;
	private Integer noticeId;
	private String unmd;
	private long reportDate;
	private String content;
	private String companyCode;
	private String publishDate;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getPdfLink() {
		return pdfLink;
	}

	public void setPdfLink(String pdfLink) {
		this.pdfLink = pdfLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	public String getUnmd() {
		return unmd;
	}

	public void setUnmd(String unmd) {
		this.unmd = unmd;
	}

	public long getReportDate() {
		return reportDate;
	}

	public void setReportDate(long reportDate) {
		this.reportDate = reportDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String toJsonString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}
