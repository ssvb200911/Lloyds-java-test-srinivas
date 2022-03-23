package com.lly.meeting.file.errors;

public class InvalidFormatException extends Exception {

	private static final long serialVersionUID = 2528468213581846033L;

	public InvalidFormatException() {
    }

    public InvalidFormatException(final String message) {
        super(message);
    }
}
