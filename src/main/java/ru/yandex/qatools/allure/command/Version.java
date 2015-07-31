package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import ru.yandex.qatools.allure.logging.Messages;

import java.io.IOException;

/**
 * Print the information about Allure command line tool.
 *
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "version", description = "Display version information")
public class Version extends AbstractCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() throws IOException {
        String toolVersion = getClass().getPackage().getImplementationVersion();
        String bundleVersion = getConfig().getCurrentVersion();
        getLogger().info(Messages.COMMAND_VERSION_INFO, toolVersion, bundleVersion);
    }

}
