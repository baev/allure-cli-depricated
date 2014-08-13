package ru.yandex.qatools.allure;

import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.With;
import ru.yandex.qatools.properties.annotations.Resource;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.providers.MapOrSyspropPathReplacerProvider;

/**
 * eroshenkoam
 * 13/08/14
 */
@Resource.File("${user.home}/.allure")
@With(MapOrSyspropPathReplacerProvider.class)
public class AllureConfig {

    @Property("report.path")
    private String reportPath = "allure-report";

    @Property("report.version")
    private String reportVersion = "1.3.9";

    @Property("results.pattern")
    private String resultsPattern = "**/allure-results";

    public AllureConfig() {
        PropertyLoader.populate(this);
    }

    public String getReportPath() {
        return reportPath;
    }

    public String getReportVersion() {
        return reportVersion;
    }

    public String getResultsPattern() {
        return resultsPattern;
    }


}
