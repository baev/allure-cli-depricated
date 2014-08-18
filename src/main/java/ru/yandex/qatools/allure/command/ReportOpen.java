package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import ru.yandex.qatools.allure.utils.Browser;

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
    public void runUnsafe() throws Exception {
        File reportDirectory = getReportDirectory();

        URI uri = new File(reportDirectory, "index.html").toURI();
        getLogger().info(String.format("Open report [%s] ", uri));
        Browser.open(uri);
    }
}
