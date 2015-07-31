package ru.yandex.qatools.allure.command;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public abstract class BundleCommand extends AbstractCommand {

    /**
     * Returns true if given version is installed and false otherwise.
     *
     * @param version the version of Allure to check it is installed.
     * @return true if given version is installed and false otherwise.
     */
    public boolean isVersionInstalled(String version) {
        return getConfig().getInstalledVersions().contains(version);
    }

    /**
     * Returns false if given version is installed and true otherwise.
     *
     * @param version the version of Allure to check it is installed.
     * @return false if given version is installed and true otherwise.
     */
    public boolean isVersionNotInstalled(String version) {
        return !isVersionInstalled(version);
    }

}
