package com.lly.meeting.file.errors;

public class InvalidDateFormatException extends InvalidFormatException {

	private static final long serialVersionUID = 1L;

	public InvalidDateFormatException() {
    }

    public InvalidDateFormatException(final String message) {
        super(message);
    }

}
