package ru.yandex.qatools.allure.command;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import ru.yandex.qatools.allure.logging.Messages;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "generate", description = "Generate the Allure report from given test results.")
public class ReportGenerate extends ReportCommand {

    public static final String JAVA_HOME = "JAVA_HOME";

    public static final String CLASS_PATH = "-cp";

    @Arguments(title = "Results directories", required = true,
            description = "A list of input directories or globs to be processed")
    public List<String> resultsDirectories = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    protected void runUnsafe() throws IOException, AllureCommandException {
        String version = getConfig().getCurrentVersion();

        if (isEmpty(version)) {
            getLogger().warn(Messages.COMMAND_REPORT_GENERATE_CURRENT_VERSION_MISSING);
            return;
        }

        if (resultsDirectories.isEmpty()) {
            getLogger().error(Messages.COMMAND_REPORT_GENERATE_RESULTS_MISSING);
            return;
        }

        new DefaultExecutor().execute(createCommandLine());

        getLogger().info(Messages.COMMAND_REPORT_GENERATE_REPORT_GENERATED, getReportDirectoryPath());
    }

    /**
     * Create a {@link CommandLine} to run bundle with needed arguments.
     *
     * @throws AllureCommandException if any occurs.
     * @see #getJavaExecutable()
     */
    protected CommandLine createCommandLine() throws AllureCommandException {
        List<String> arguments = new ArrayList<>();
        arguments.add(getLoggerConfigurationArgument());
        arguments.add(CLASS_PATH);
        arguments.add(getClassPathArgument());
        arguments.add(getMainClassArgument());
        arguments.addAll(resultsDirectories);
        arguments.add(getReportDirectoryPath());

        return new CommandLine(getJavaExecutable())
                .addArguments(arguments.toArray(new String[arguments.size()]));
    }

    /**
     * Returns the path to java executable.
     *
     * @return the path to java executable.
     * @throws AllureCommandException if JAVA_HOME environment variable is not set.
     */
    protected String getJavaExecutable() throws AllureCommandException {
        String javaHome = System.getenv(JAVA_HOME);
        if (javaHome == null) {
            throw new AllureCommandException("Could not find java executable: JAVA_HOME is not set");
        }

        return String.format("%s/bin/java", javaHome);
    }

    /**
     * Get argument to configure log level for bundle.
     */
    protected String getLoggerConfigurationArgument() {
        return String.format("-Dorg.slf4j.simpleLogger.defaultLogLevel=%s",
                isQuiet() || !isVerbose() ? "error" : "debug");
    }

    private String getClassPathArgument() {
        String version = getConfig().getCurrentVersion();
        String separator = System.getProperty("path.separator");
        Path configPath = getConfigPath().getParent().toAbsolutePath();
        Path bundleJarPath = getExecutablePath(version);
        String pluginsPath = getPluginsPath().toAbsolutePath() + "/*";
        return configPath + separator + bundleJarPath + separator + pluginsPath;
    }

    private Path getPluginsPath() {
        return getHomeDirectory().resolve("plugins");
    }

    private String getMainClassArgument() {
        return "ru.yandex.qatools.allure.AllureMain";
    }
}
