package ru.yandex.qatools.allure.config;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public class ConfigBuilder {

    public static final String DEFAULT_REPORT_PATH = "allure-report";

    private ConfigBuilder() {
    }

    public static Config newConfig() {
        Config config = new Config();
        config.setReportPath(DEFAULT_REPORT_PATH);
        return config;
    }
}
