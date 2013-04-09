package com.kotoblog.grabit.exceptions;

@SuppressWarnings("serial")
public class GrabitException extends Exception {

	public GrabitException() {
		super();
	}

	public GrabitException(Exception ex) {
		super(ex);
	}

	public GrabitException(String message) {
		super(message);
	}

	public GrabitException(String message, String page) {
		this(message + "\n" + page);
	}

}
