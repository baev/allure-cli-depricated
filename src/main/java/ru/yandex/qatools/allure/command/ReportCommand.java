package ru.yandex.qatools.allure.command;

import io.airlift.command.Option;
import io.airlift.command.OptionType;
import ru.yandex.qatools.allure.logging.Messages;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public abstract class ReportCommand extends AbstractCommand {

    @Option(name = {"-o", "--report-dir", "--output"}, type = OptionType.COMMAND,
            description = "The directory to generate Allure report into.")
    protected String reportDirectoryPath;

    /**
     * The string representation of path to the report directory.
     */
    protected String getReportDirectoryPath() {
        return isEmpty(reportDirectoryPath) ? getConfig().getReportPath() : reportDirectoryPath;
    }

    /**
     * The path to the report directory {@link #getReportDirectoryPath()}.
     */
    protected Path getReportDirectory() {
        return Paths.get(getReportDirectoryPath()).toAbsolutePath();
    }

    /**
     * Open the given url in default system browser.
     */
    protected void openBrowser(URI url) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(url);
        } else {
            getLogger().error(Messages.COMMAND_REPORT_CANT_OPEN_BROWSER);
        }
    }
}
