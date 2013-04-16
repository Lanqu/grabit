package com.kotoblog.grabit.persist.service;

import java.util.Collection;

import com.kotoblog.grabit.beans.Article;
import com.kotoblog.grabit.beans.Site;

public interface IEntityService {

	<T> void saveEntity(T entity);

	Site loadSiteByUrl(String url);

	Collection<Article> loadArticlesWithoutContent();

	Collection<Article> loadArticlesNotSpinned();

}
