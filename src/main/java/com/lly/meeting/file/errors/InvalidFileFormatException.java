package com.lly.meeting.file.errors;

public class InvalidFileFormatException extends Exception {

 
    private static final long serialVersionUID = 3260110753766142493L;


	public InvalidFileFormatException() {
    }

  
    public InvalidFileFormatException(final String message) {
        super(message);
    }
}
