package com.lly.meeting;

import org.apache.log4j.Logger;

import com.lly.meeting.file.errors.InvalidFileFormatException;
import com.lly.meeting.file.errors.InvalidFormatException;

import static com.lly.meeting.fileValiation.FileParserParams.DEFAULT_INPUT_FILE;
import static com.lly.meeting.util.Helpers.isStringEmpty;

import java.io.IOException;
import java.util.Scanner;


public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InvalidFormatException, IOException, InvalidFileFormatException {
        LOGGER.info(" ----  Booking Meetings Started -----");
        String inputFileName = "";
        
        try(Scanner input = new Scanner(System.in);) {
        	  System.out.println("Enter file details : ");
              inputFileName = input.next();
        }
      
        final MeetingSlotScheduler meetingSlotScheduler = new MeetingSlotScheduler();
        meetingSlotScheduler.process(isStringEmpty(inputFileName) ? DEFAULT_INPUT_FILE : inputFileName);
    }
}



