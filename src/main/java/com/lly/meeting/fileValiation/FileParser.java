package com.lly.meeting.fileValiation;

import javax.annotation.Nonnull;

import com.lly.meeting.file.errors.InvalidFormatException;

public interface FileParser {

    @Nonnull
    boolean validateCompanyOfficeHoursFormat(@Nonnull String time) throws InvalidFormatException;
   
    @Nonnull
    boolean validateBookingRequestFormat(@Nonnull String bookingRequestFormat) throws InvalidFormatException;

    @Nonnull
    boolean validateMeetingScheduleFormat(@Nonnull String meetingScheduleFormat) throws InvalidFormatException;

}
