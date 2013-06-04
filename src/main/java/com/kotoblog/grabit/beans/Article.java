package com.kotoblog.grabit.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

@Component
public class Article implements Serializable  {

	private static final long serialVersionUID = 7451707216636719185L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@NotNull
	ArticlesDirectory articlesDirectory;

	@Column(length = 500)
	String url;

	@Column(length = 500)
	String title;

	@Lob
	String content;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "originalId", targetEntity = SpinnedArticle.class)
	Set<SpinnedArticle> spinnedArticles;

	@ManyToMany(targetEntity = Keyword.class, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	Set<Keyword> keywords;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Keyword> getKeywords() {
		if (this.keywords == null) {
			this.keywords = new HashSet<Keyword>();
		}
		return this.keywords;
	}

	public void addKeyword(Keyword keyword) {
		getKeywords().add(keyword);
		keyword.getArticles().add(this);
	}

	public void setKeywords(Set<Keyword> keywords) {
		this.keywords = keywords;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.content == null) ? 0 : this.content.hashCode());
		result = (prime * result) + ((this.title == null) ? 0 : this.title.hashCode());
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
		Article other = (Article) obj;
		if (this.content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!this.content.equals(other.content)) {
			return false;
		}
		if (this.title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!this.title.equals(other.title)) {
			return false;
		}
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
		return "Article [id=" + this.id + ", title=" + this.title + ", keywords=" + this.keywords + "]";
	}

	public ArticlesDirectory getArticlesDirectory() {
		return this.articlesDirectory;
	}

	public void setArticlesDirectory(ArticlesDirectory articlesDirectory) {
		this.articlesDirectory = articlesDirectory;
	}

	public Set<SpinnedArticle> getSpinnedArticles() {
		if (this.spinnedArticles == null) {
			this.spinnedArticles = new HashSet<SpinnedArticle>();
		}
		return this.spinnedArticles;
	}

	public void setSpinnedArticles(Set<SpinnedArticle> spinnedArticles) {
		this.spinnedArticles = spinnedArticles;
	}

	public void addSpinnedArticle(SpinnedArticle spinnedArticle) {
		getSpinnedArticles().add(spinnedArticle);
		spinnedArticle.setOriginalId(this.getId());
	}

}
