package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.yandex.qatools.allure.logging.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "open", description = "Open generated report")
public class ReportOpen extends ReportCommand {

    @Override
    protected void runUnsafe() throws IOException, AllureCommandException {
        Path reportDirectory = getReportDirectory();

        if (Files.notExists(reportDirectory)) {
            getLogger().error(Messages.COMMAND_REPORT_OPEN_REPORT_MISSING, reportDirectory);
            return;
        }

        getLogger().info(Messages.COMMAND_REPORT_OPEN_SERVER_STARTING, reportDirectory);

        try {
            Server server = setUpServer();
            server.setStopAtShutdown(true);
            server.start();

            openBrowser(server.getURI());
            getLogger().info(Messages.COMMAND_REPORT_OPEN_SERVER_STARTED, server.getURI());
            server.join();
        } catch (Exception e) {
            throw new AllureCommandException(e);
        }
    }

    /**
     * Set up server for report directory.
     */
    protected Server setUpServer() {
        Server server = new Server(0);
        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setWelcomeFiles(new String[]{"index.html"});
        handler.setResourceBase(getReportDirectory().toString());
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler, new DefaultHandler()});
        server.setHandler(handlers);
        return server;
    }
}
