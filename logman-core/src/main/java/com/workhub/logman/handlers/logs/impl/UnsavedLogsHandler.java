package com.workhub.logman.handlers.logs.impl;

import com.workhub.commons.utils.inet.InetAddressUtil;
import com.workhub.logman.data.LogData;
import com.workhub.logman.handlers.logs.IUnsavedLogsHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * UnsavedLogsHandler
 * Saves incomming logs to local
 * files, in case writing to the db failed
 *
 * @since 1.0.0 | 08.02.2021
 * @author alexkirillov
 */
@Slf4j
public class UnsavedLogsHandler implements IUnsavedLogsHandler {

    private static final long MAX_FILE_SIZE = 1048576; // ~1MB

    private int fileCount;
    private File currentFile;

    private Path unsavedLogsFolderPath = Path.of(System.getProperty("logman.unsaved.logs.path", "unsaved-logs/"));

    @Override
    public void writeUnsavedLogs(List<LogData> logList) {
        try {
            File file = obtainWritableFile();
            PrintWriter writer = new PrintWriter(new FileWriter(file, true));
            logList.stream().forEach(writer::println);
            writer.close();
            writer.flush();
        } catch (Exception e) {
            log.error("Failed to save into local unsaved logs file! We lost 'em :( . {}",e.getMessage(), e);
        }
    }
    
    private File obtainWritableFile() throws IOException {
        updateFileCount();
        if (currentFile == null) {
            if (checkLogFolder()) {
                currentFile = unsavedLogsFolderPath.toFile().listFiles()[fileCount - 1];
            } else {
                currentFile = createUnsavedLogsFile();
            }
        } else if (!isAcceptableFileCapacity(currentFile) || !currentFile.canWrite()) {
            log.debug("Last current file reached max size, locking and creating a new file.");
            currentFile.setReadOnly();
            currentFile = createUnsavedLogsFile();
        }
        return currentFile;
    }

    private File createUnsavedLogsFile() throws IOException {
        StringBuilder fileName = new StringBuilder();
        fileName.append("logman-unsaved-logs.").append(InetAddressUtil.getSERVER_HOST())
                .append(".").append(LocalDate.now()).append("_").append(fileCount).append(".txt");
        File logFile = new File(unsavedLogsFolderPath.toString().concat("/").concat(fileName.toString()));
        logFile.createNewFile();

        return logFile;

    }

    public boolean checkLogFolder() throws IOException {
        // check if unsaved-logs dir exists if not -> create
        if (!Files.exists(unsavedLogsFolderPath)) {
            Files.createDirectory(unsavedLogsFolderPath);
            return false;
        } else {
            return true;
        }
    }

    private boolean isAcceptableFileCapacity(File logFile) {
        long totalSize = logFile.length();
        if (totalSize >= MAX_FILE_SIZE) {
            return false;
        }
        return true;
    }

    private void updateFileCount() {
        fileCount = unsavedLogsFolderPath.toFile().list().length;
    }

}
