package ru.yandex.qatools.allure.command;

import io.airlift.command.Option;
import io.airlift.command.OptionType;

import java.io.File;

/**
 * eroshenkoam
 * 13/08/14
 */
public abstract class ReportCommand extends AllureCommand {

    @Option(name = {"-o", "--report-path"}, type = OptionType.GROUP,
            description = "Relative path to report directory")
    protected String reportPath = getConfig().getReportPath();

    public String getReportPath() {
        return reportPath;
    }

    public File getReportDirectory() {
        return new File(getReportPath());
    }

}
