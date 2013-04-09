package com.kotoblog.grabit.captcha;

import org.junit.Test;

import com.kotoblog.grabit.exceptions.CaptchaGrabitException;

public class ManualCaptchaTest {

	@Test
	public void testManual() throws CaptchaGrabitException {
		ManualCaptchaSolver solver = new ManualCaptchaSolver("http://chart.finance.yahoo.com/z?s=GOOG&t=6m&q=l");
		solver.solve();
	}

}
