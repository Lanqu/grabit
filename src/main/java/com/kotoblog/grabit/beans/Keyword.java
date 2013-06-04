package com.kotoblog.grabit.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.stereotype.Component;

@Component
public class Keyword implements Serializable {

	private static final long serialVersionUID = 5575605647590980806L;

	@Id
	String keyword;

	@ManyToMany(targetEntity = Article.class, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	Set<Article> articles;

	public Keyword() {
	}

	public Keyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Set<Article> getArticles() {
		if (this.articles == null) {
			this.articles = new HashSet<Article>();
		}
		return this.articles;
	}

	public void addArticle(Article article) {
		getArticles().add(article);
		article.getKeywords().add(this);
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.keyword == null) ? 0 : this.keyword.hashCode());
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
		Keyword other = (Keyword) obj;
		if (this.keyword == null) {
			if (other.keyword != null) {
				return false;
			}
		} else if (!this.keyword.equals(other.keyword)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Keyword [keyword=" + this.keyword + ", articles=" + getArticles().size() + "]";
	}

}
