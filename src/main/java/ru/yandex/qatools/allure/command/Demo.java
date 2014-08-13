package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * eroshenkoam
 * 13/08/14
 */
@Command(name = "demo", description = "Open demo report")
public class Demo extends AllureCommand {

    public static final String REPORT_DEMO_URL = "http://teamcity.qatools.ru/repository/download/" +
            "allure_core_master_release/lastSuccessful/index.html?guest=1";

    public void runUnsafe() throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported()) {
            getLogger().info("Trying to open the following URL in browser: " + REPORT_DEMO_URL);
            Desktop.getDesktop().browse(new URI(REPORT_DEMO_URL));
        } else {
            System.out.println("Will not open browser because this capability is not supported on your platform.");
        }
    }
}
