package com.lly.meeting.meetingSchedule;

import org.joda.time.LocalDateTime;

import com.lly.meeting.model.EmployeeMeetingSchedule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.lly.meeting.util.Helpers.*;
import static com.lly.meeting.util.Validator.checkNull;

import java.util.LinkedHashSet;
import java.util.Stack;

public class MeetingScheduleIntervalModel implements MeetingScheduleInterval {

    private ScheduleIntervalNode rootNode;

    public void add(@Nonnull final EmployeeMeetingSchedule employeeMeetingSchedule) {
        checkNull(employeeMeetingSchedule, "employeeMeetingSchedule");
        checkNull(employeeMeetingSchedule.getMeeting().getMeetingStartTime(), "startTime");
        checkNull(employeeMeetingSchedule.getMeeting().getMeetingEndTime(), "endTime");

        final LocalDateTime startTime = employeeMeetingSchedule.getMeeting().getMeetingStartTime();
        final LocalDateTime endTime = employeeMeetingSchedule.getMeeting().getMeetingEndTime();

        if (isGreaterThanOrEqual(startTime, endTime))
            throw new IllegalArgumentException("The meetingEndTime " + endTime +
                    " should be greater than meetingStartTime " + startTime);

        ScheduleIntervalNode scheduleIntervalNode = rootNode;

        while (scheduleIntervalNode != null) {
            // Check the overlap and ignore it
            if (overlap(startTime, endTime))
                return;

            scheduleIntervalNode.maxMeetingEndTime = (isGreaterThan(endTime, scheduleIntervalNode.maxMeetingEndTime)) ? endTime : scheduleIntervalNode.maxMeetingEndTime;

            if (isLessThan(startTime, scheduleIntervalNode.meetingStartTime)) {
                if (scheduleIntervalNode.leftNode == null) {
                    scheduleIntervalNode.leftNode = new ScheduleIntervalNode(null, startTime, endTime, endTime, null, employeeMeetingSchedule);
                    return;
                }
                scheduleIntervalNode = scheduleIntervalNode.leftNode;
            } else {
                if (scheduleIntervalNode.rightNode == null) {
                    scheduleIntervalNode.rightNode = new ScheduleIntervalNode(null, startTime, endTime, endTime, null, employeeMeetingSchedule);
                    return;
                }
                scheduleIntervalNode = scheduleIntervalNode.rightNode;
            }
        }
        rootNode = new ScheduleIntervalNode(null, startTime, endTime, endTime, null, employeeMeetingSchedule);
    }

    @Override
    public LinkedHashSet<EmployeeMeetingSchedule> traversal(@Nullable ScheduleIntervalNode rootNode) {
        final LinkedHashSet<EmployeeMeetingSchedule> employeeMeetingSchedules = new LinkedHashSet<>();

        if (rootNode == null) {
            return employeeMeetingSchedules;
        }

        final Stack<ScheduleIntervalNode> stack = new Stack<>();
        stack.push(rootNode);

        while (!stack.isEmpty()) {
            final ScheduleIntervalNode top = stack.peek();
            if (top.leftNode != null) {
                stack.push(top.leftNode);
                top.leftNode = null;
            } else {
                employeeMeetingSchedules.add(top.employeeMeetingSchedule);
                stack.pop();
                if (top.rightNode != null) {
                    stack.push(top.rightNode);
                }
            }
        }
        return employeeMeetingSchedules;
    }

    @Nullable
    @Override
    public ScheduleIntervalNode getRoot() {
        return this.rootNode;
    }


    /**
     * Check if the input meeting timing intervals are overlapping with the existing intervals.
     *
     * @param startTime the meetingStartTime of the interval
     * @param endTime   the meetingEndTime of the interval
     *                  return           true if overlap, else false.
     */
    public boolean overlap(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        if (isGreaterThanOrEqual(startTime, endTime))
            throw new IllegalArgumentException("The meetingEndTime " + endTime
                    + " should be greater than meetingStartTime " + startTime);

        ScheduleIntervalNode scheduleIntervalNode = rootNode;

        while (scheduleIntervalNode != null) {
            if (validateIntersection(startTime, endTime, scheduleIntervalNode.meetingStartTime,
                    scheduleIntervalNode.meetingEndTime))
                return true;

            if (goToLeftNode(startTime, endTime, scheduleIntervalNode.leftNode)) {
                scheduleIntervalNode = scheduleIntervalNode.leftNode;
            } else {
                scheduleIntervalNode = scheduleIntervalNode.rightNode;
            }
        }
        return false;
    }

    /**
     * Returns true if there is any validateIntersection in the two time intervals
     * Two intervals such that one of the points coincide:
     * eg: [2022-08-22 15:00, 2022-08-22 16:00] and [2022-08-22 16:00, 2022-08-22 17:00] are NOT considered as intersecting.
     * eg: [2022-08-22 15:00, 2022-08-22 16:00] and [2022-08-22 15:30, 2022-08-22 17:00] are considered as intersecting.
     */
    private boolean validateIntersection(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime,
                                         @Nonnull final LocalDateTime intervalStartTime,
                                         @Nonnull final LocalDateTime intervalEndTime) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");
        checkNull(intervalStartTime, "intervalStartTime");
        checkNull(intervalEndTime, "intervalEndTime");

        return isLessThan(startTime, intervalEndTime) && isGreaterThan(endTime, intervalStartTime);
    }

    private boolean goToLeftNode(@Nonnull final LocalDateTime startTime, @Nonnull final LocalDateTime endTime,
                                 @Nullable final ScheduleIntervalNode intervalLeftSubtree) {
        checkNull(startTime, "startTime");
        checkNull(endTime, "endTime");

        return intervalLeftSubtree != null && isGreaterThan(intervalLeftSubtree.maxMeetingEndTime, startTime);
    }

}