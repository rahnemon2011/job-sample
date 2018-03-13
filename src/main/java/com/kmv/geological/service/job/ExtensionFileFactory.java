package com.kmv.geological.service.job;

import com.kmv.geological.domain.enums.JobType;
import org.springframework.stereotype.Component;

@Component
public class ExtensionFileFactory {

    private ExtensionFileFactory() {
    }

    public static JobType getJobType(String extension) {
        switch (extension.toLowerCase()) {
            case "xls":
            case "xlsx":
                return JobType.EXCEL_FILE_READING;
            case "xml":
                return JobType.XML_FILE_READING;
        }
        throw new UnsupportedOperationException("File extension is no supported!");
    }
} 