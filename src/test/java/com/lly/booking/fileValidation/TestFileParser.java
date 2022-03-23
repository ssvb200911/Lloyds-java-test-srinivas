package com.lly.booking.fileValidation;

import org.junit.Assert;
import org.junit.Test;

import com.lly.meeting.file.errors.InvalidFormatException;
import com.lly.meeting.fileValiation.FileParser;
import com.lly.meeting.fileValiation.FileParserImpl;
import com.lly.meeting.meetingSchedule.MeetingScheduleInterval;
import com.lly.meeting.meetingSchedule.MeetingScheduleIntervalModel;
import com.lly.meeting.model.EmployeeMeetingSchedule;

import javax.annotation.Nonnull;

import static com.lly.meeting.util.Helpers.*;
import static com.lly.meeting.util.Validator.checkNull;

import java.io.*;
import java.util.*;

public class TestFileParser {

    @Test
    public void testCompanyOfficeHoursFormat_WhenInputIsValid() throws IOException, InvalidFormatException {
        final String inputFileName = "validInputToCheckCompanyOfficeHoursFormat.txt";

        final List<String> companyOfficeHours = fileParse(inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertTrue(fileParser.validateCompanyOfficeHoursFormat(companyOfficeHour));
        }
    }

    @Test(expected = InvalidFormatException.class)
    public void testCompanyOfficeHoursFormat_WhenInputIsNotValid() throws IOException, InvalidFormatException {
        final String inputFileName = "inValidInputToCheckCompanyOfficeHoursFormat.txt";

        final List<String> companyOfficeHours = fileParse(inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            fileParser.validateCompanyOfficeHoursFormat(companyOfficeHour);
        }
    }

    @Test
    public void testBookingRequestFormat_WhenInputIsValid() throws IOException, InvalidFormatException {
        final String inputFileName = "validInputToCheckBookingRequestFormat.txt";

        final List<String> companyOfficeHours = fileParse(inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertTrue(fileParser.validateBookingRequestFormat(companyOfficeHour));
        }
    }

    @Test(expected = InvalidFormatException.class)
    public void testBookingRequestFormat_WhenInputIsNotValid() throws IOException, InvalidFormatException {
        final String inputFileName = "inValidInputToCheckBookingRequestFormat.txt";

        final List<String> companyOfficeHours = fileParse(inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            fileParser.validateBookingRequestFormat(companyOfficeHour);
        }
    }

    @Test
    public void testMeetingScheduleFormat_WhenInputIsValid() throws IOException, InvalidFormatException {
        final String inputFileName = "validInputToCheckMeetingScheduleFormat.txt";

        final List<String> companyOfficeHours = fileParse(inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertTrue(fileParser.validateMeetingScheduleFormat(companyOfficeHour));
        }
    }

    @Test(expected = InvalidFormatException.class)
    public void testMeetingScheduleFormat_WhenInputIsNotValid() throws IOException, InvalidFormatException {
        final String inputFileName = "inValidInputToCheckMeetingScheduleFormat.txt";

        final List<String> companyOfficeHours = fileParse(inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            fileParser.validateMeetingScheduleFormat(companyOfficeHour);
        }
    }

    @Test
    public void testMeetingScheduleIntervalFormatOverlapping() throws IOException, InvalidFormatException {
        final String inputFileName = "inputValid.txt";

        TestFileParser testFileParser = new TestFileParser();

        final List<String> inputFileContent = testFileParser.fileParse(inputFileName);
        final List<EmployeeMeetingSchedule> inputFileContentModel = convertListToModel(inputFileContent);

        final MeetingScheduleInterval meetingScheduleIntervalModel = new MeetingScheduleIntervalModel();

        for (final EmployeeMeetingSchedule employeeMeetingSchedule : inputFileContentModel) {
            meetingScheduleIntervalModel.add(employeeMeetingSchedule);
        }

        final LinkedHashSet<EmployeeMeetingSchedule> employeeMeetingSchedules =
                meetingScheduleIntervalModel.traversal(meetingScheduleIntervalModel.getRoot());

        final Iterator itr = employeeMeetingSchedules.iterator();
        final List<String> employeeList = new LinkedList<>();
        while (itr.hasNext()) {
            final EmployeeMeetingSchedule employeeMeetingSchedule = (EmployeeMeetingSchedule) itr.next();
            employeeList.add(employeeMeetingSchedule.getEmployeeId());
        }

        // since overlapped timings are discarded, EMP001 will not be there
        Assert.assertTrue(employeeMeetingSchedules.size() == 3);

        Assert.assertTrue(employeeList.contains("EMP003"));
        Assert.assertTrue(employeeList.contains("EMP002"));
        Assert.assertTrue(employeeList.contains("EMP004"));

        // records are sorted by the request submission time, EMP003 will be the root
        Assert.assertTrue(meetingScheduleIntervalModel.getRoot().
                employeeMeetingSchedule.getEmployeeId().equals("EMP003"));
    }

    /**
     * Helpers method
     */
    @Nonnull
    public List<String> fileParse(@Nonnull final String fileName) throws IOException {
        checkNull(fileName, "fileName");

        final List<String> inputs = new ArrayList<String>();

        //Get file from resources folder
        final ClassLoader classLoader = getClass().getClassLoader();
        File file = null;
        if (classLoader.getResource(fileName) != null)
            file = new File(classLoader.getResource(fileName).getFile());
        else
            throw new FileNotFoundException(fileName + " file not found.");

        final BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
        String line = "";
        while (Boolean.FALSE.equals(isStringEmpty(line = reader.readLine()))) {
            inputs.add(line);
        }

        return isListEmpty(inputs) ? Collections.<String>emptyList() : inputs;
    }

}
