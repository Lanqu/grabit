package com.kotoblog.grabit.client;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlImage;

public class GrabitClient extends WebClient {

	private static final long serialVersionUID = 6920058260721253681L;

	public GrabitClient() {
		super();
	}

	public GrabitClient(BrowserVersion browserVersion, String proxyHost, int proxyPort) {
		super(browserVersion, proxyHost, proxyPort);
	}

	public GrabitClient(BrowserVersion browserVersion) {
		super(browserVersion);
	}

	@Override
	public WebWindow getCurrentWindow() {
		WebWindow res = super.getCurrentWindow();
		requestBlankImageIfExists(res.getEnclosedPage());
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P extends Page> P getPage(String url) throws IOException, FailingHttpStatusCodeException, MalformedURLException {
		return (P) requestBlankImageIfExists(super.getPage(url));
	}

	private <P extends Page> P requestBlankImageIfExists(P page) {
		if (page != null) {
			HtmlImage blankGif = ((DomNode) page).getFirstByXPath("//img[@src[contains(.,'/blank.gif?v=')]]");
			if (blankGif != null) {
				try {
					blankGif.getImageReader();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return page;
	}

}
