package com.kf.data.elasticsearch.entity;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/****
 * 
 * @Title: EsNews.java
 * @Package com.kf.data.clent.web.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年8月8日 上午9:54:22
 * @version V1.0
 */
public class EsNews {

	private int status;
	private String content;
	private String title;
	private String relate_industry_id;
	private String author;
	private String if_unfavourable;
	private String news_id;
	private String source;
	private String cont_trim;
	private String summary;
	private String real_cource;
	private String category_id;
	private Date release_date;
	private String relate_industry;
	private String category_name;
	private String filelist;
	private String images_list;
	private int isplaying;
	private int isrecommend;

	public String getFilelist() {
		return filelist;
	}

	public void setFilelist(String filelist) {
		this.filelist = filelist;
	}

	public String getImages_list() {
		return images_list;
	}

	public void setImages_list(String images_list) {
		this.images_list = images_list;
	}

	public int getIsplaying() {
		return isplaying;
	}

	public void setIsplaying(int isplaying) {
		this.isplaying = isplaying;
	}

	public int getIsrecommend() {
		return isrecommend;
	}

	public void setIsrecommend(int isrecommend) {
		this.isrecommend = isrecommend;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelate_industry_id() {
		return relate_industry_id;
	}

	public void setRelate_industry_id(String relate_industry_id) {
		this.relate_industry_id = relate_industry_id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIf_unfavourable() {
		return if_unfavourable;
	}

	public void setIf_unfavourable(String if_unfavourable) {
		this.if_unfavourable = if_unfavourable;
	}

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCont_trim() {
		return cont_trim;
	}

	public void setCont_trim(String cont_trim) {
		this.cont_trim = cont_trim;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getReal_cource() {
		return real_cource;
	}

	public void setReal_cource(String real_cource) {
		this.real_cource = real_cource;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getRelate_industry() {
		return relate_industry;
	}

	public void setRelate_industry(String relate_industry) {
		this.relate_industry = relate_industry;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String toJsonString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}
