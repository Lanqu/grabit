package com.kotoblog.grabit.spinner;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.kotoblog.grabit.exceptions.SpinnerGrabitException;
import com.kotoblog.grabit.exceptions.WrongLoginGrabitException;

public class FreeArticlesSpinner extends AbstractSpinner {

	private static final String NOT_LOGGED_IN = "again as your session has expired";
	private final static String URL = "http://www.freearticlespinner.com/member.area.dashboard.php";
	private final static String LOGIN_URL = "http://www.freearticlespinner.com/login.php";
	private final static String INVALID_LOGIN = "Your login is invalid, please try again.";
	private final static String PLEASE_WAIT = "Rewriting Article... Please Wait a few seconds..";

	private final static String START_BTN = "//*[@value='Rewrite Article Now']";
	private final static String LOGIN_BTN = "id('form1')//*[@value='Login']";
	private final static String EMAIL_INPT = "id('email')";
	private final static String PWN_INPT = "id('password')";
	private final static String ARTICLE_AREA = "id('Article')";

	private final String user;
	private final String password;
	private final WebClient client = new WebClient();

	public FreeArticlesSpinner(String user, String password) {
		super();
		this.user = user;
		this.password = password;
		this.client.getOptions().setThrowExceptionOnFailingStatusCode(false);
	}

	@Override
	public String spin(String content) throws SpinnerGrabitException {
		HtmlPage page = (HtmlPage) this.client.getCurrentWindow().getEnclosedPage();
		try {
			if ((page == null) || (page.getFirstByXPath(ARTICLE_AREA) == null)) {
				page = this.client.getPage(URL);
			}

			if (page.asText().contains(NOT_LOGGED_IN)) {
				page = doLogin();
			}

			String result = doSpin(page, content);

			return result;

		} catch (FailingHttpStatusCodeException e) {
			new SpinnerGrabitException(e);
		} catch (MalformedURLException e) {
			new SpinnerGrabitException(e);
		} catch (IOException e) {
			new SpinnerGrabitException(e);
		} catch (InterruptedException e) {
			new SpinnerGrabitException(e);
		}

		throw new SpinnerGrabitException("No result");
	}

	private String doSpin(HtmlPage page, String content) throws IOException, InterruptedException, SpinnerGrabitException {
		HtmlTextArea textArea = page.getFirstByXPath(ARTICLE_AREA);
		textArea.type(content);

		HtmlInput startBtn = page.getFirstByXPath(START_BTN);

		HtmlPage p = startBtn.click();

		long timeout = waitTillSpinned(p);

		throwIfTimeouted(timeout);

		textArea = p.getFirstByXPath(ARTICLE_AREA);

		return textArea.getText();

	}

	private long waitTillSpinned(HtmlPage p) throws InterruptedException {
		long timeout = 30000;
		while (((HtmlTextArea) p.getFirstByXPath(ARTICLE_AREA)).getText().equals(PLEASE_WAIT) && (timeout > 0)) {
			Thread.sleep(100);
			timeout -= 100;
		}
		return timeout;
	}

	private HtmlPage doLogin() throws IOException, InterruptedException, SpinnerGrabitException {
		HtmlPage p;
		boolean logged = false;
		do {
			p = this.client.getPage(LOGIN_URL);
			HtmlInput email = p.getFirstByXPath(EMAIL_INPT);
			email.type(this.user);

			HtmlInput pwd = p.getFirstByXPath(PWN_INPT);
			pwd.type(this.password);

			HtmlInput login = p.getFirstByXPath(LOGIN_BTN);
			p = login.click();

			logged = sleepTillLogged(p, START_BTN);
		} while (!logged);

		return p;
	}

	private boolean sleepTillLogged(HtmlPage result, String startBtn) throws InterruptedException, SpinnerGrabitException {
		long timeout = 30000;
		while ((result.getFirstByXPath(startBtn) == null) && (timeout > 0)) {
			Thread.sleep(100);
			timeout -= 100;

			if (wrongLogin(result)) {
				throw new WrongLoginGrabitException("Wrong login or password");
			}
		}

		throwIfTimeouted(timeout);

		return true;
	}

	private void throwIfTimeouted(long timeout) throws SpinnerGrabitException {
		if (timeout <= 0) {
			throw new SpinnerGrabitException("Timeout");
		}
	}

	private boolean wrongLogin(HtmlPage result) {
		return result.asText().contains(INVALID_LOGIN);
	}

}
