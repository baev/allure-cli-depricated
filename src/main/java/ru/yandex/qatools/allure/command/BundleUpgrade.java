package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import retrofit.client.Response;
import ru.yandex.qatools.allure.logging.Messages;
import ru.yandex.qatools.allure.repository.RepositoryClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.yandex.qatools.allure.repository.RepositoryClientBuilder.newClient;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "upgrade", description = "Download and install latest release report version")
public class BundleUpgrade extends BundleCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() throws IOException {
        RepositoryClient repositoryClient = newClient();
        String releaseVersion = repositoryClient.getMetadata().getVersioning().getRelease();

        if (isVersionNotInstalled(releaseVersion)) {
            Path executablePath = getExecutablePath(releaseVersion);
            Files.createDirectories(executablePath.getParent());

            getLogger().info(Messages.COMMAND_REPORT_UPGRADE_BUNDLE_DOWNLOADING, releaseVersion);
            Response response = repositoryClient.downloadBundle(releaseVersion);

            try (InputStream stream = response.getBody().in()) {
                Files.copy(stream, executablePath);
                getConfig().getInstalledVersions().add(releaseVersion);
            }
        }

        getConfig().switchVersion(releaseVersion);
        saveConfig();

        getLogger().info(Messages.COMMAND_BUNDLE_SWITCH_BUNDLE_VERSION_SWITCHED, releaseVersion);
    }
}
