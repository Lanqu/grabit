package com.kotoblog.grabit.persist.dao;

import java.util.Collection;

import com.kotoblog.grabit.beans.Article;
import com.kotoblog.grabit.beans.Site;

public interface IEntityDao {

	<T> void saveEntity(T entity);

	Site loadSiteByUrl(String url);

	Collection<Article> loadArticlesWithoutContent();

}
