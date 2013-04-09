package com.kotoblog.grabit.client;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
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
		Page page = res.getEnclosedPage();

		if (page != null) {
			HtmlImage blankGif = ((DomNode) page).getFirstByXPath("//img[@src[contains(.,'/search/blank.gif')]]");
			if (blankGif != null) {
				try {
					blankGif.getImageReader();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

}
