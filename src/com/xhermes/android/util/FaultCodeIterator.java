package com.xhermes.android.util;

public class FaultCodeIterator {
	private String index;
	private String faultDetail;
	private String classify;
	private String priorty;
	private String solution;
	private String assortment;
	public String getAssortment() {
		return assortment;
	}
	public void setAssortment(String assortment) {
		this.assortment = assortment;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getPriorty() {
		return priorty;
	}
	public void setPriorty(String priorty) {
		this.priorty = priorty;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getFaultDetail() {
		return faultDetail;
	}
	public void setFaultDetail(String faultDetail) {
		this.faultDetail = faultDetail;
	}
}

