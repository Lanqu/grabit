package com.kotoblog.grabit.spinner;

import com.kotoblog.grabit.exceptions.SpinnerGrabitException;

public abstract class AbstractSpinner {

	public abstract String spin(String content) throws SpinnerGrabitException;

}
