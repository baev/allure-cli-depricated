package ru.yandex.qatools.allure.command;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.logging.Messages;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "remove", description = "Remove the specified bundle from cache")
public class BundleRemove extends BundleCommand {

    @Arguments(title = "version", required = true,
            description = "The version to remove from cache")
    protected String version;

    /**
     * Remove the specified bundle from cache. The removed bundle can be cached
     * again using update or switch command.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() throws IOException {
        if (isVersionNotInstalled(version)) {
            getLogger().warn(Messages.COMMAND_BUNDLE_REMOVE_BUNDLE_ABSENT, version);
            return;
        }

        Path bundlePath = getBundlesPath(version);

        getLogger().info(Messages.COMMAND_BUNDLE_REMOVE_BUNDLE_REMOVING, bundlePath.toAbsolutePath());
        FileUtils.deleteDirectory(bundlePath.toFile());

        getConfig().removeVersion(version);
        saveConfig();

        getLogger().info(Messages.COMMAND_BUNDLE_REMOVE_BUNDLE_REMOVED, version);
    }
}
