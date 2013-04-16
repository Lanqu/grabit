package com.kotoblog.grabit;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.kotoblog.grabit.beans.Article;
import com.kotoblog.grabit.beans.ArticlesDirectory;
import com.kotoblog.grabit.beans.Config;
import com.kotoblog.grabit.beans.Keyword;
import com.kotoblog.grabit.beans.Site;
import com.kotoblog.grabit.beans.SpinnedArticle;
import com.kotoblog.grabit.captcha.ManualCaptchaSolver;
import com.kotoblog.grabit.client.GrabitClient;
import com.kotoblog.grabit.exceptions.CaptchaGrabitException;
import com.kotoblog.grabit.exceptions.GrabitException;
import com.kotoblog.grabit.exceptions.SpinnerGrabitException;
import com.kotoblog.grabit.persist.service.IEntityService;
import com.kotoblog.grabit.spinner.AbstractSpinner;
import com.kotoblog.grabit.spinner.FreeArticlesSpinner;

/**
 * Hello world!
 * 
 */
public class GrabIt {

	private static IEntityService persister;

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException, GrabitException,
	ClassNotFoundException, SQLException {
		System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");

		ClassPathXmlApplicationContext applicationContext = null;
		try {

			applicationContext = initPersister();

			GrabitClient browser = getBrowser();

			processSitesFromConfig(browser);

			// links collected. Lets download it

			Collection<Article> articlesToDownload = loadArticlesWithoutContent();

			if (articlesToDownload != null) {
				downloadArticles(browser, articlesToDownload);
			}

			// articles downloaded. Lets spin them

			Collection<Article> articlesToSpin = loadArticlesNotSpinned();

			spinArticles(articlesToSpin);

		} finally {
			closeContext(applicationContext);
		}
	}

	private static void spinArticles(Collection<Article> articlesToDownload) throws SpinnerGrabitException {
		AbstractSpinner spinner = new FreeArticlesSpinner("lanquu@gmail.com", "lanqumix");

		for (Article article : articlesToDownload) {
			String spinnedContent = spinner.spin(article.getContent());
			article.addSpinnedArticle(new SpinnedArticle(spinnedContent));

			persister.saveEntity(article);
		}
	}

	private static Collection<Article> loadArticlesWithoutContent() {
		return persister.loadArticlesWithoutContent();
	}

	private static Collection<Article> loadArticlesNotSpinned() {
		return persister.loadArticlesNotSpinned();
	}

	private static void processSitesFromConfig(GrabitClient browser) throws IOException, MalformedURLException, InterruptedException, GrabitException {
		for (Site site : getConfig("http://kotoblog.com").getSites()) {

			if (site.getCount() <= site.getCollected()) {
				continue;
			}

			processDirectoriesForSite(browser, site);

			persister.saveEntity(site);
		}
	}

	private static void closeContext(ClassPathXmlApplicationContext applicationContext) {
		if (applicationContext != null) {
			applicationContext.close();
		}
	}

	private static void downloadArticles(GrabitClient browser, Collection<Article> articlesToDownload) throws IOException, MalformedURLException,
	InterruptedException {
		for (Article article : articlesToDownload) {
			HtmlPage page = browser.getPage(article.getUrl());
			page = checkForCaptcha(page);

			String contentStr = scrapeArticleFromPage(article, page);
			String title = page.getFirstByXPath(article.getArticlesDirectory().getTitleXpath());

			article.setContent(contentStr);
			article.setTitle(title);

			persister.saveEntity(article);

		}
	}

	@SuppressWarnings("unchecked")
	private static String scrapeArticleFromPage(Article article, HtmlPage page) {
		List<DomElement> content = (List<DomElement>) page.getByXPath(article.getArticlesDirectory().getContentXpath());

		StringBuffer contentStr = new StringBuffer();

		for (DomElement el : content) {
			contentStr.append(el.asXml());
		}
		return contentStr.toString();
	}

	private static ClassPathXmlApplicationContext initPersister() {
		ClassPathXmlApplicationContext applicationContext;
		// Initialisation block
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		persister = applicationContext.getBean(IEntityService.class);
		return applicationContext;
	}

	private static GrabitClient getBrowser() {
		GrabitClient browser = new GrabitClient(BrowserVersion.INTERNET_EXPLORER_7);
		browser.getOptions().setThrowExceptionOnFailingStatusCode(false);
		return browser;
	}

	private static void processDirectoriesForSite(GrabitClient browser, Site site) throws IOException, MalformedURLException, InterruptedException,
	GrabitException {
		for (ArticlesDirectory dir : site.getArticlesDirectories()) {

			for (Keyword keyword : site.getKeywords()) {

				HtmlPage page = openStartPage(browser, dir, keyword);

				page = checkForCaptcha(page);

				page = fillSearchBoxIfNeeded(dir, keyword, page);

				HtmlElement lister = extractLister(dir, page);

				listThroughDirectoryTillCollected(site, dir, keyword, page, lister);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void listThroughDirectoryTillCollected(Site site, ArticlesDirectory dir, Keyword keyword, HtmlPage page, HtmlElement lister)
			throws MalformedURLException, IOException, InterruptedException {
		loop: do {
			for (HtmlAnchor a : (List<HtmlAnchor>) page.getByXPath(dir.getLinksXpath())) {

				URL u = makeFullUrlToArticle(dir, a);

				Article article = createArticle(dir, u);

				collectArticleIfNeeded(site, keyword, article);

				// TODO count all links in all dirs
				if (site.getCount() <= site.getCollected()) {
					break loop;
				}
			}

			page = goToNextPage(dir, lister);

			lister = page.getFirstByXPath(dir.getListerXpath());
		} while (lister != null);
	}

	private static HtmlPage goToNextPage(ArticlesDirectory dir, HtmlElement lister) throws IOException, InterruptedException {
		HtmlPage page;
		lister.setAttribute("href", mutateUrl(dir.getMutators(), lister.getAttribute("href")));
		page = lister.click();
		page = checkForCaptcha(page);
		return page;
	}

	private static void collectArticleIfNeeded(Site site, Keyword keyword, Article article) {
		if (!keyword.getArticles().contains(article)) {
			keyword.addArticle(article);
			site.setCollected(site.getCollected() + 1);
		}
	}

	private static Article createArticle(ArticlesDirectory dir, URL u) {
		Article article = new Article();
		article.setUrl(u.toString());
		article.setArticlesDirectory(dir);
		return article;
	}

	private static URL makeFullUrlToArticle(ArticlesDirectory dir, HtmlAnchor a) throws MalformedURLException {
		URL url = new URL(dir.getDirectoryUrl());
		URL u = new URL(url.getProtocol(), url.getHost(), -1, a.getHrefAttribute());
		return u;
	}

	private static HtmlElement extractLister(ArticlesDirectory dir, HtmlPage page) throws GrabitException {
		HtmlElement lister = page.getFirstByXPath(dir.getListerXpath());

		if (lister == null) {
			throw new GrabitException("Lister is not found even for the first time", page.asXml());
		}
		return lister;
	}

	private static HtmlPage fillSearchBoxIfNeeded(ArticlesDirectory dir, Keyword keyword, HtmlPage page) throws IOException {
		if (dir.getSearchXpath() != null) {
			HtmlInput keywordField = page.getFirstByXPath(dir.getKeywordFieldXpath());
			keywordField.type(keyword.getKeyword());

			HtmlInput searchBtn = page.getFirstByXPath(dir.getSearchXpath());
			page = searchBtn.click();
		}
		return page;
	}

	private static HtmlPage openStartPage(GrabitClient browser, ArticlesDirectory dir, Keyword keyword) throws IOException, MalformedURLException {
		HtmlPage page = browser.getPage(mutateUrl(dir.getMutators(), dir.getDirectoryUrl(), keyword.getKeyword().replace(' ', '+')));
		return page;
	}

	public static HtmlPage checkForCaptcha(HtmlPage page) throws IOException, InterruptedException {
		HtmlPage resultPage = page;
		boolean solved = false;
		DomAttr captchaImgSrc = (DomAttr) resultPage.getFirstByXPath("//*[@id='recaptcha_image']/img/@src");
		while ((captchaImgSrc != null) && !solved) {
			try {
				String captcha = new ManualCaptchaSolver(captchaImgSrc.getValue()).solve();
				if (captcha == null) {
					continue;
				}
				HtmlTextInput input = resultPage.getFirstByXPath("//input[@id='recaptcha_response_field']");
				input.type(captcha);
				resultPage = (HtmlPage) input.type('\n');
				captchaImgSrc = (DomAttr) resultPage.getFirstByXPath("//*[@id='recaptcha_image']/img/@src");
				if (captchaImgSrc == null) {
					solved = true;
				}
			} catch (CaptchaGrabitException e) {
				solved = false;
			}
		}

		return resultPage;
	}

	public static String mutateUrl(Map<String, String> mutators, String url, String keyword) {
		return mutateUrl(mutators, url).replace("${keyword}", keyword);
	}

	public static String mutateUrl(Map<String, String> mutators, String url) {
		String result = url;

		for (Map.Entry<String, String> entry : mutators.entrySet()) {
			if (result.contains(entry.getKey())) {
				result = result.replace(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	public static Site loadSiteByUrl(String url) {
		Site site = persister.loadSiteByUrl(url);

		return site;
	}

	private static Config getConfig(String url) {

		Site s = loadSiteByUrl(url);

		Config config = new Config();

		if (s == null) {

			ArticlesDirectory articlesDirectory = new ArticlesDirectory("http://goarticles.com/search/?type=&q=${keyword}");
			articlesDirectory.getExcluders().add("//abcbbc[1]/21323");
			articlesDirectory.getExcluders().add("//excluder");
			articlesDirectory.setContentXpath("id('article-content')//div[@class='article']//following-sibling::p[following-sibling::h3]");
			articlesDirectory.setLinksXpath("//a[@class=\"article_title_link\"]");
			articlesDirectory.setListerXpath("//a[text()='Next']");
			articlesDirectory.setTitleXpath("id('article-content')//h1/text()");
			articlesDirectory.getMutators().put("limit=10", "limit=5");

			Site site = new Site();
			site.setUrl("http://kotoblog.com");
			site.setStripLinks(true);
			site.getKeywords().add(new Keyword("credit card rates"));
			site.setCount(5);
			site.getArticlesDirectories().add(articlesDirectory);

			config.getSites().add(site);

			JAXB.marshal(config, new File("config.xml"));

		} else {

			config.getSites().add(s);

		}

		return config;
	}
}
