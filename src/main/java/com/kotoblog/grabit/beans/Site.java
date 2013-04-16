package com.kotoblog.grabit.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.springframework.stereotype.Component;

@Entity
@Component
@XmlAccessorType(XmlAccessType.FIELD)
public class Site implements Serializable {

	private static final long serialVersionUID = 7923961601994313645L;

	// site url
	@Id
	String url;

	// Clear anchors?
	boolean stripLinks = true;

	// what keywords site is building around
	@OneToMany(targetEntity = Keyword.class, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@NotNull
	Set<Keyword> keywords;

	@OneToMany(targetEntity = ArticlesDirectory.class, fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@NotNull
	Set<ArticlesDirectory> articlesDirectories;

	// How many download
	int count;

	// how many links found
	int collected;

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getStripLinks() {
		return this.stripLinks;
	}

	public void setStripLinks(boolean stripLinks) {
		this.stripLinks = stripLinks;
	}

	public Set<ArticlesDirectory> getArticlesDirectories() {
		if (this.articlesDirectories == null) {
			this.articlesDirectories = new HashSet<ArticlesDirectory>();
		}
		return this.articlesDirectories;
	}

	public void setArticlesDirectories(Set<ArticlesDirectory> articlesDirectories) {
		this.articlesDirectories = articlesDirectories;
	}

	public Set<Keyword> getKeywords() {
		if (this.keywords == null) {
			this.keywords = new HashSet<Keyword>();
		}
		return this.keywords;
	}

	public void setKeywords(Set<Keyword> keywords) {
		this.keywords = keywords;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCollected() {
		return this.collected;
	}

	public void setCollected(int collected) {
		this.collected = collected;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.url == null) ? 0 : this.url.hashCode());
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
		Site other = (Site) obj;
		if (this.url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!this.url.equals(other.url)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Site [url=" + this.url + ", keywords=" + this.keywords + ", articlesDirectories=" + this.articlesDirectories + ", count=" + this.count
				+ ", collected=" + this.collected + "]";
	}

}
