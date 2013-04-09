package com.kotoblog.grabit.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@XmlRootElement
@Component
public class Config {

	public List<Site> sites;

	public List<Site> getSites() {
		if (this.sites == null) {
			this.sites = new ArrayList<Site>();
		}
		return this.sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	@Override
	public String toString() {
		return "Config [sites=" + this.sites + "]";
	}

}
