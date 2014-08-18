package ru.yandex.qatools.allure.command;

import io.airlift.command.Option;
import io.airlift.command.OptionType;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.yandex.qatools.allure.AllureCLI;
import ru.yandex.qatools.allure.AllureConfig;

/**
 * eroshenkoam
 * 11/08/14
 */
public abstract class AllureCommand implements Runnable {

    @Option(name = {"--debug"}, type = OptionType.GLOBAL,
            description = "Output debug information")
    protected boolean debug = false;

    private static final AllureConfig config = new AllureConfig();

    private static final Logger logger = LogManager.getLogger(AllureCLI.class);

    public static AllureConfig getConfig() {
        return config;
    }

    public static Logger getLogger() {
        return logger;
    }

    public void run() {
        setUpLogLevel(isDebug() ? Level.DEBUG : Level.INFO);
        try {
            runUnsafe();
        } catch (Exception e) {
            getLogger().info(e.getMessage());
            getLogger().debug(e);
        }
    }

    public abstract void runUnsafe() throws Exception;

    public boolean isDebug() {
        return debug;
    }

    private void setUpLogLevel(Level level) {
        LogManager.getRootLogger().setLevel(level);
    }

}
