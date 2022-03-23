package com.lly.meeting.meetingSchedule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.lly.meeting.model.EmployeeMeetingSchedule;

import java.util.LinkedHashSet;

public interface MeetingScheduleInterval {

    /**
     * Adds an interval of meeting schedule to the com.lly.booking.model. It also checks for the overlap of interval.
     * If there is any overlap then it ignores it
     *
     * @param employeeMeetingSchedule contains all the details of meeting schedule
     */
    void add(@Nonnull EmployeeMeetingSchedule employeeMeetingSchedule);

    /**
     * Traverse over all the intervals and return the lists of intervals as inorder pattern.
     *
     * @param rootNode the root node of the com.lly.booking.model
     */
    LinkedHashSet<EmployeeMeetingSchedule> traversal(@Nullable ScheduleIntervalNode rootNode);

    @Nullable
    ScheduleIntervalNode getRoot();
}
