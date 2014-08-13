package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * eroshenkoam
 * 13/08/14
 */
@Command(name = "clean", description = "Clean report")
public class ReportClean extends ReportCommand {

    public void runUnsafe() throws IOException {
        File reportDirectory = getReportDirectory();
        getLogger().debug(String.format("Cleaning up report directory [%s]", reportDirectory));
        if (reportDirectory.exists()) {
            FileUtils.deleteDirectory(reportDirectory);
            getLogger().info(String.format("Report directory [%s] successful deleted", reportDirectory));
        } else {
            getLogger().info("Report directory [%s] doesn't exist");
        }
    }
}
