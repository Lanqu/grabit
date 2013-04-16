package com.kotoblog.grabit.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.stereotype.Component;

@Entity
@Component
public class SpinnedArticle implements Serializable {

	private static final long serialVersionUID = -7317494701206965210L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	Integer originalId;

	@Lob
	String spinnedContent;

	public SpinnedArticle() {
		super();
	}

	public SpinnedArticle(String spinnedContent) {
		super();
		this.spinnedContent = spinnedContent;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpinnedContent() {
		return this.spinnedContent;
	}

	public void setSpinnedContent(String spinnedContent) {
		this.spinnedContent = spinnedContent;
	}

	public Integer getOriginalId() {
		return this.originalId;
	}

	public void setOriginalId(Integer originalId) {
		this.originalId = originalId;
	}

	@Override
	public String toString() {
		return "SpinnedArticle [id=" + this.id + ", originalId=" + this.originalId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		result = (prime * result) + ((this.originalId == null) ? 0 : this.originalId.hashCode());
		result = (prime * result) + ((this.spinnedContent == null) ? 0 : this.spinnedContent.hashCode());
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
		SpinnedArticle other = (SpinnedArticle) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.originalId == null) {
			if (other.originalId != null) {
				return false;
			}
		} else if (!this.originalId.equals(other.originalId)) {
			return false;
		}
		if (this.spinnedContent == null) {
			if (other.spinnedContent != null) {
				return false;
			}
		} else if (!this.spinnedContent.equals(other.spinnedContent)) {
			return false;
		}
		return true;
	}

}
