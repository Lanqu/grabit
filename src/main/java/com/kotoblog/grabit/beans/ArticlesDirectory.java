package com.kotoblog.grabit.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Entity
@Component
public class ArticlesDirectory implements Serializable {

	public ArticlesDirectory(String directoryUrl) {
		super();
		this.directoryUrl = directoryUrl;
	}

	public ArticlesDirectory() {
		super();
	}

	private static final long serialVersionUID = -4429647752277154385L;

	// articles directory link
	@Id
	String directoryUrl;

	// contentXpath for pulling out text
	String contentXpath;

	// button to get pressed after keyword enter
	String searchXpath;

	// next button or link
	String listerXpath;

	// if need customize next button
	@ElementCollection(fetch = FetchType.EAGER)
	Map<String, String> mutators;

	// xpath for selecting links on the search page
	String linksXpath;

	// keyword field contentXpath
	String keywordFieldXpath;

	// titleXpath of article
	String titleXpath;

	// List of excluders that can be removed by contentXpath
	@ElementCollection(fetch = FetchType.EAGER)
	Set<String> excluders;

	public String getContentXpath() {
		return this.contentXpath;
	}

	public void setContentXpath(String contentXpath) {
		this.contentXpath = contentXpath;
	}

	public String getSearchXpath() {
		return this.searchXpath;
	}

	public void setSearchXpath(String searchXpath) {
		this.searchXpath = searchXpath;
	}

	public String getListerXpath() {
		return this.listerXpath;
	}

	public void setListerXpath(String listerXpath) {
		this.listerXpath = listerXpath;
	}

	public String getLinksXpath() {
		return this.linksXpath;
	}

	public void setLinksXpath(String linksXpath) {
		this.linksXpath = linksXpath;
	}

	public String getKeywordFieldXpath() {
		return this.keywordFieldXpath;
	}

	public void setKeywordFieldXpath(String keywordFieldXpath) {
		this.keywordFieldXpath = keywordFieldXpath;
	}

	public String getTitleXpath() {
		return this.titleXpath;
	}

	public void setTitleXpath(String titleXpath) {
		this.titleXpath = titleXpath;
	}

	public Set<String> getExcluders() {
		if (this.excluders == null) {
			this.excluders = new HashSet<String>();
		}
		return this.excluders;
	}

	public void setExcluders(Set<String> excluders) {
		this.excluders = excluders;
	}

	public String getDirectoryUrl() {
		return this.directoryUrl;
	}

	public void setDirectoryUrl(String directoryUrl) {
		this.directoryUrl = directoryUrl;
	}

	public Map<String, String> getMutators() {
		if (this.mutators == null) {
			this.mutators = new HashMap<String, String>();
		}
		return this.mutators;
	}

	public void setMutators(Map<String, String> listerHrefMutator) {
		this.mutators = listerHrefMutator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.directoryUrl == null) ? 0 : this.directoryUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ArticlesDirectory other = (ArticlesDirectory) obj;
		if (this.directoryUrl == null) {
			if (other.directoryUrl != null) {
				return false;
			}
		} else if (!this.directoryUrl.equals(other.directoryUrl)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ArticlesDirectory [directoryUrl=" + this.directoryUrl + "]";
	}
}
