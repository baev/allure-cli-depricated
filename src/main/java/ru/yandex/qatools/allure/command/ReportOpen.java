package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import org.eclipse.jetty.server.Server;

import java.io.File;
import java.net.URI;

import static ru.yandex.qatools.allure.utils.ServerUtil.getServerURI;
import static ru.yandex.qatools.allure.utils.ServerUtil.setUpServerForReportDirectory;
import static ru.yandex.qatools.allure.utils.BrowserUtil.openBrowser;

/**
 * eroshenkoam
 * 13/08/14
 */
@Command(name = "open", description = "Open generated report")
public class ReportOpen extends ReportCommand {

    @Override
    public void runUnsafe() throws Exception {
        File reportDirectory = getReportDirectory();
        if (!reportDirectory.exists()) {
            getLogger().error(String.format("Can't open report: directory [%s] doesn't exist",
                    reportDirectory.getAbsolutePath()));
            return;
        }

        getLogger().info(String.format("Starting web server for report directory [%s] ",
                reportDirectory.getAbsolutePath()));
        Server server = setUpServerForReportDirectory(reportDirectory);
        server.start();

        URI uri = getServerURI(server);
        getLogger().info(String.format("Open report [%s] ", uri));
        openBrowser(uri);

        server.join();
    }


}
