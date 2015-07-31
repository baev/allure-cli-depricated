package ru.yandex.qatools.allure.command;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import ru.yandex.qatools.allure.logging.Messages;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "switch", description = "Switch between bundles")
public class BundleSwitch extends BundleCommand {

    @Arguments(title = "version", required = true,
            description = "Version to switch")
    public String version;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() {
        if (isVersionNotInstalled(version)) {
            getLogger().error(
                    Messages.COMMAND_BUNDLE_SWITCH_BUNDLE_VERSION_MISSING,
                    version,
                    getConfig().getInstalledVersions()
            );
            return;
        }

        getConfig().switchVersion(version);
        saveConfig();

        getLogger().info(Messages.COMMAND_BUNDLE_SWITCH_BUNDLE_VERSION_SWITCHED, version);
    }

}
