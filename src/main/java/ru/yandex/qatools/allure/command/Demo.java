package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "demo", description = "Open demo report")
public class Demo extends ReportCommand {

    public static final String REPORT_DEMO_URL = "http://ci.qatools.ru/job/allure-core_master-deploy/" +
            "lastSuccessfulBuild/artifact/allure-report-preview/target/allure-report/index.html#/";

    /**
     * {@inheritDoc}
     */
    protected void runUnsafe() throws IOException, AllureCommandException {
        try {
            openBrowser(new URI(REPORT_DEMO_URL));
        } catch (URISyntaxException e) {
            throw new AllureCommandException(e);
        }
    }
}
