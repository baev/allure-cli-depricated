package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;

import java.awt.*;
import java.net.URI;

/**
 * eroshenkoam
 * 13/08/14
 */
@Command(name = "demo", description = "Open allure demo report in default browser")
public class ReportDemo extends AllureCommand {

    public static final String REPORT_DEMO_URL = "http://teamcity.qatools.ru/repository/download/" +
            "allure_core_master_release/lastSuccessful/index.html?guest=1";

    public void run() {
        if (Desktop.isDesktopSupported()) {
            System.out.println("Trying to open the following URL in browser: " + REPORT_DEMO_URL);
            try {
                Desktop.getDesktop().browse(new URI(REPORT_DEMO_URL));
            } catch (Exception e) {
                System.out.println("Failed to open URL in browser: " + REPORT_DEMO_URL);
            }
        } else {
            System.out.println("Will not open browser because this capability is not supported on your platform.");
        }
    }
}
