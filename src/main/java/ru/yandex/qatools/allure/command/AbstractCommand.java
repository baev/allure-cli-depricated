package ru.yandex.qatools.allure.command;

import ch.qos.cal10n.MessageConveyor;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import ru.yandex.qatools.allure.ExitCode;
import ru.yandex.qatools.allure.config.Config;
import ru.yandex.qatools.allure.logging.Messages;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Locale;

import static ru.yandex.qatools.allure.config.ConfigBuilder.newConfig;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public abstract class AbstractCommand implements AllureCommand {

    public static final String HOME_DIR_NAME = ".allure";

    public static final String BUNDLES_DIR_NAME = "bundles";

    public static final String CONFIG_FILE_NAME = "config.xml";

    public static final String EXECUTABLE_FILE_NAME = "allure-bundle.jar";

    protected final Path homeDirectory = Paths.get(FileUtils.getUserDirectoryPath()).resolve(HOME_DIR_NAME);

    protected final Path bundlesDirectory = getHomeDirectory().resolve(BUNDLES_DIR_NAME);

    protected final Path configPath = getHomeDirectory().resolve(CONFIG_FILE_NAME);

    protected ExitCode exitCode = ExitCode.NO_ERROR;

    protected Config config;

    @Option(name = {"-v", "--verbose"}, type = OptionType.GLOBAL,
            description = "Switch on the verbose mode.")
    protected boolean verbose = false;

    @Option(name = {"-q", "--quiet"}, type = OptionType.GLOBAL,
            description = "Switch on the quiet mode.")
    protected boolean quiet = false;

    /**
     * Run command and handle exceptions if any occurs. See {@link #runUnsafe()}
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public final void run() {
        setUpLogLevel();
        try {
            init();
            runUnsafe();
        } catch (IOException | AllureCommandException e) {
            getLogger().error(Messages.COMMAND_ALLURE_COMMAND_ABORTED, e);
            setExitCode(ExitCode.GENERIC_ERROR);
        }
    }

    /**
     * Initialization method. Creates the directories needed during report
     * report generation and reads client configuration.
     *
     * @throws Exception if any occurs.
     */
    protected void init() throws IOException {
        Files.createDirectories(getHomeDirectory());
        Files.createDirectories(getBundlesDirectory());

        if (Files.notExists(getConfigPath())) {
            Path configFile = Files.createFile(getConfigPath());
            JAXB.marshal(newConfig(), configFile.toFile());
        }

        config = JAXB.unmarshal(getConfigPath().toFile(), Config.class);
    }

    /**
     * Run command.
     *
     * @throws Exception if any occurs.
     */
    protected abstract void runUnsafe() throws IOException, AllureCommandException;

    /**
     * Marshall {@link #config} to its file.
     */
    protected synchronized void saveConfig() {
        Collections.sort(getConfig().getInstalledVersions(), Collections.reverseOrder());
        JAXB.marshal(getConfig(), getConfigPath().toFile());
    }

    /**
     * Create the instance of localized logger.
     */
    protected LocLogger getLogger() {
        Locale locale = getLocale();
        MessageConveyor conveyor = new MessageConveyor(locale);
        LocLoggerFactory factory = new LocLoggerFactory(conveyor);
        return factory.getLocLogger(getClass());
    }

    /**
     * Returns the path to the bundle directory with specified version.
     */
    protected Path getBundlesPath(String version) {
        return getBundlesDirectory().resolve(version);
    }

    /**
     * Returns the path to the allure-bundle.jar with specified version.
     */
    protected Path getExecutablePath(String version) {
        return getBundlesPath(version).resolve(EXECUTABLE_FILE_NAME).toAbsolutePath();
    }


    /**
     * Get the locale form config. If locale is not set use {@link Locale#ENGLISH}.
     */
    protected Locale getLocale() {
        return config.getLocale() != null ?
                Locale.forLanguageTag(config.getLocale()) :
                Locale.ENGLISH;
    }

    /**
     * Get log level depends on provided client parameters such as verbose and quiet.
     */
    protected Level getLogLevel() {
        return isQuiet() ? Level.OFF : isVerbose() ? Level.DEBUG : Level.INFO;
    }

    /**
     * Configure logger with needed level {@link #getLogLevel()}.
     */
    private void setUpLogLevel() {
        LogManager.getRootLogger().setLevel(getLogLevel());
    }

    @Override
    public ExitCode getExitCode() {
        return exitCode;
    }

    public void setExitCode(ExitCode exitCode) {
        this.exitCode = exitCode;
    }

    public Config getConfig() {
        return config;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public Path getHomeDirectory() {
        return homeDirectory;
    }

    public Path getBundlesDirectory() {
        return bundlesDirectory;
    }

    public Path getConfigPath() {
        return configPath;
    }
}
