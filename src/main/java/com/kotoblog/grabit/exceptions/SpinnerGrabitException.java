package com.kotoblog.grabit.exceptions;

public class SpinnerGrabitException extends GrabitException {

	private static final long serialVersionUID = 3130681879527174055L;

	public SpinnerGrabitException() {
		super();
	}

	public SpinnerGrabitException(Exception ex) {
		super(ex);
	}

	public SpinnerGrabitException(String message, String page) {
		super(message, page);
	}

	public SpinnerGrabitException(String message) {
		super(message);
	}

}
