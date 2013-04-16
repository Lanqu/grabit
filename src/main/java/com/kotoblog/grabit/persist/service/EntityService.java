package com.kotoblog.grabit.persist.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kotoblog.grabit.beans.Article;
import com.kotoblog.grabit.beans.Site;
import com.kotoblog.grabit.persist.dao.IEntityDao;

@Service
@Transactional
public class EntityService implements IEntityService {

	@Autowired
	private IEntityDao dao;

	@Override
	@Transactional
	public <T> void saveEntity(T entity) {
		this.dao.saveEntity(entity);
	}

	public void setDao(IEntityDao dao) {
		this.dao = dao;
	}

	@Override
	@Transactional
	public Site loadSiteByUrl(String url) {
		return this.dao.loadSiteByUrl(url);
	}

	@Override
	@Transactional
	public Collection<Article> loadArticlesWithoutContent() {
		return this.dao.loadArticlesWithoutContent();
	}

	@Override
	@Transactional
	public Collection<Article> loadArticlesNotSpinned() {
		return this.dao.loadArticlesNotSpinned();
	}

}
