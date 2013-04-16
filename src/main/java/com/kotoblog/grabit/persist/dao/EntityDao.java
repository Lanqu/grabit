package com.kotoblog.grabit.persist.dao;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.kotoblog.grabit.beans.Article;
import com.kotoblog.grabit.beans.Site;

@Repository
public class EntityDao implements IEntityDao {

	@Resource
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public <T> void saveEntity(T entity) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

	@Override
	public Site loadSiteByUrl(String url) {
		@SuppressWarnings("unchecked")
		List<Site> q = this.sessionFactory.getCurrentSession().createQuery("from Site site where site.url like :url").setString("url", url).list();
		if ((q != null) && (q.size() == 1)) {
			return q.get(0);
		}
		return null;
	}

	@Override
	public Collection<Article> loadArticlesWithoutContent() {
		@SuppressWarnings("unchecked")
		List<Article> q = this.sessionFactory.getCurrentSession()
		.createQuery("from Article article where (article.content is null) or (length(article.content) < 5)").list();
		return q;
	}

	@Override
	public Collection<Article> loadArticlesNotSpinned() {
		@SuppressWarnings("unchecked")
		List<Article> q = this.sessionFactory.getCurrentSession().createQuery("from Article article where article.spinnedArticles is empty").list();
		return q;
	}

}
