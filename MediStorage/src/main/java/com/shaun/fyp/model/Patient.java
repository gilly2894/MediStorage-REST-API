package com.shaun.fyp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

/*
 * This is the Patient POJO
 */
public class Patient {
	
	@Id
	private String _id;
	private String name;
	private Address address;
	private String illness;
	private List<Link> links = new ArrayList<Link>();
	
	public Patient()  {  }
	
	public Patient(String _id, String name, Address address, String illness)
	{
		this._id = _id;
		this.name = name;
		this.address = address;
		this.illness = illness;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getIllness() {
		return illness;
	}

	public void setIllness(String illness) {
		this.illness = illness;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	/*
	 * This creates a new Link object and sets it with url and rel
	 * Then adds the new link to the List<Links>
	 */
	public void addLink(String url, String rel)
	{
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		links.add(link);
	}

}
