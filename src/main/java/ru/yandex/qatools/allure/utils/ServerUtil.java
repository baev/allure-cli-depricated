package ru.yandex.qatools.allure.utils;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * eroshenkoam
 * 21/08/14
 */
public class ServerUtil {

    public static final String DEFAULT_SCHEME = "http";

    public static final String DEFAULT_HOST = "localhost";

    public static final int DEFAULT_PORT = 0;

    public static final String WELCOME_FILE = "index.html";

    public static Server setUpServerForReportDirectory(File reportDirectory) {

        Server server = new Server(DEFAULT_PORT);
        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setWelcomeFiles(new String[]{WELCOME_FILE});
        handler.setResourceBase(reportDirectory.getAbsolutePath());
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler, new DefaultHandler()});
        server.setHandler(handlers);
        return server;
    }

    public static URI getServerURI(Server server) throws URISyntaxException {
        return new URIBuilder()
                .setScheme(DEFAULT_SCHEME)
                .setHost(DEFAULT_HOST)
                .setPort(getServerPort(server))
                .build();
    }

    public static int getServerPort(Server server) {
        return ((ServerConnector) server.getConnectors()[0]).getLocalPort();
    }
}
