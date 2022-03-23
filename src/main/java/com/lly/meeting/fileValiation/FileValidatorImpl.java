package com.lly.meeting.fileValiation;

import org.apache.log4j.Logger;

import com.lly.meeting.file.errors.InvalidFormatException;

import javax.annotation.Nonnull;

import static com.lly.meeting.util.Helpers.getFileAsList;
import static com.lly.meeting.util.Validator.checkNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileValidatorImpl implements FileValidator {

    private final static Logger LOGGER = Logger.getLogger(FileValidatorImpl.class);

    @Nonnull
    public boolean validateFileFormat(@Nonnull final File file) throws IOException, InvalidFormatException {
        checkNull(file, "file");

        final List<String> fileInputs = getFileAsList(file);
        if (Boolean.FALSE.equals(checkLengthOfInputs(fileInputs))) {
            LOGGER.error("Inputs should be atleast 3 lines and then followed by every 2 lines");
            throw new InvalidFormatException("Inputs should be atleast 3 lines and then followed by every 2 lines");
        }

        final FileParser fileParser = new FileParserImpl();

        // Check 1st line for of the input text represents the company office hours, in 24 hour clock format
        if (Boolean.FALSE.equals(fileParser.validateCompanyOfficeHoursFormat(fileInputs.get(0))))
            return false;

        // meetingStartTime from the 2nd line
        for (int index = 1; index < fileInputs.size(); index++) {

            // validate every 2nd line which contains individual booking requests
            if (index % 2 != 0) {
                if (Boolean.FALSE.equals(fileParser.validateBookingRequestFormat(fileInputs.get(index))))
                    return false;
            }

            // validate every 3rd line which contains meeting meetingStartTime time
            if (index % 2 == 0) {
                if (Boolean.FALSE.equals(fileParser.validateMeetingScheduleFormat(fileInputs.get(index))))
                    return false;
            }
        }

        return true;
    }

    /**
     * Helpers method
     */

    /**
     * Check the length of input file. The file should contains atleast 3 lines and the number of lines should be odd number.
     * If it validates then return true otherwise false.
     *
     * @param fileInputs File lines to be checked
     * @return true | false
     */
    @Nonnull
    private boolean checkLengthOfInputs(@Nonnull final List<String> fileInputs) {
        checkNull(fileInputs, "fileInputs");

        return fileInputs.size() > 3 && fileInputs.size() % 2 != 0;
    }
}
