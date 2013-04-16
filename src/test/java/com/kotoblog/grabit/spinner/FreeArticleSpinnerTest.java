package com.kotoblog.grabit.spinner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.kotoblog.grabit.exceptions.SpinnerGrabitException;
import com.kotoblog.grabit.exceptions.WrongLoginGrabitException;

public class FreeArticleSpinnerTest {

	AbstractSpinner spinner;
	private static final String text = "My first article that was written about 6000 years ago about credit card rates";

	@Test
	public void test() throws SpinnerGrabitException {
		this.spinner = new FreeArticlesSpinner("lanquu@gmail.com", "lanqumix");
		String result = this.spinner.spin("My first article that was written about 6000 years ago about credit card rates");
		assertThat(result, notNullValue());
		assertThat(text, not(equalTo(result)));
	}

	@Test
	public void testWrongLogin() throws SpinnerGrabitException {
		this.spinner = new FreeArticlesSpinner("lanquu@gmail.com", "lanqumix1");
		try {
			this.spinner.spin("My first article that was written about 6000 years ago about credit card rates");
		} catch (WrongLoginGrabitException e) {
			return;
		}

		fail();
	}

}
