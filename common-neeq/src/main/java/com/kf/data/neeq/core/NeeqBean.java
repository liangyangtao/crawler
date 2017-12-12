package com.kf.data.neeq.core;

import java.util.List;

public class NeeqBean {
	
	public List<InfoBean> info;
	
	

	public String getNodeName() {
		return nodeName;
	}



	public List<InfoBean> getInfo() {
		return info;
	}



	public void setInfo(List<InfoBean> info) {
		this.info = info;
	}



	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}



	public String getNumber() {
		return number;
	}



	public void setNumber(String number) {
		this.number = number;
	}



	public String nodeName;
	
	public String number;
	
}
