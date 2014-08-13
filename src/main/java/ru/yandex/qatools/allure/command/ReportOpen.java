package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * eroshenkoam
 * 13/08/14
 */
@Command(name = "open", description = "Open generated report")
public class ReportOpen extends ReportCommand {

    @Override
    public void runUnsafe() throws IOException {
        File reportDirectory = getReportDirectory();

        URI uri = new File(reportDirectory, "index.html").toURI();
        getLogger().info(String.format("Open report [%s] ", uri));
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri);
        } else {
            getLogger().warn("Will not open browser because this capability is not supported on your platform");
        }
    }
}
