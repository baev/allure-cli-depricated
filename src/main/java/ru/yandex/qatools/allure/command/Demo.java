package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import ru.yandex.qatools.allure.utils.Browser;

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

    public void runUnsafe() throws Exception {
        getLogger().debug(String.format("Trying to open the following URL in browser: %s", REPORT_DEMO_URL));
        Browser.open(new URI(REPORT_DEMO_URL));
    }
}
