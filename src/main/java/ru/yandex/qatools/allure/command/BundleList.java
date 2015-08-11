package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import ru.yandex.qatools.allure.logging.Messages;

import java.util.List;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "list", description = "Display the list of installed Allure bundles")
public class BundleList extends BundleCommand {

    /**
     * Display the list of installed bundles.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() {
        String currentVersion = getConfig().getCurrentVersion();
        List<String> installedVersions = getConfig().getInstalledVersions();
        if (installedVersions.isEmpty()) {
            getLogger().warn(Messages.COMMAND_BUNDLE_LIST_BUNDLES_MISSING);
        }
        for (String version : installedVersions) {
            getLogger().info(Messages.COMMAND_BUNDLE_LIST_BUNDLE_VERSION_PRINT,
                    version.equals(currentVersion) ? "*" : " ",
                    version);
        }
    }
}
