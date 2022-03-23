package com.lly.meeting.fileValiation;

import javax.annotation.Nonnull;

import com.lly.meeting.file.errors.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public interface FileValidator {

    @Nonnull
    boolean validateFileFormat(@Nonnull File file) throws IOException, InvalidFormatException;
}
