package com.bosic.bean;

public enum Chess {
	BLACK("●"), WHITE("○"), NO("□");
	private String icon;

	private Chess(String icon){
		this.icon = icon;
	}
	public String getIcon() {
		return icon;
	}
}
