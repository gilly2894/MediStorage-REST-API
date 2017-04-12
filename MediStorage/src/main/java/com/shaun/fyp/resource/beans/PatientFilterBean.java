package com.shaun.fyp.resource.beans;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/*
 * This is a Filter bean.
 * It is used so that there wont be many annotations in the GET patient method in PatientResouce
 */
public class PatientFilterBean {
	
	private @QueryParam("name")@DefaultValue("") String name;
	private @QueryParam("start")@DefaultValue("0") int start; 
	private @QueryParam("start")@DefaultValue("0") int size;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
