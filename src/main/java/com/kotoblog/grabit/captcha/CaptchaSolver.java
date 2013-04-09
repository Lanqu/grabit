package com.kotoblog.grabit.captcha;

import com.kotoblog.grabit.exceptions.CaptchaGrabitException;

public abstract class CaptchaSolver {
	public abstract String solve() throws CaptchaGrabitException;
}

