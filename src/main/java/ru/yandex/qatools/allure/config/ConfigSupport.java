package ru.yandex.qatools.allure.config;

import java.util.List;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public abstract class ConfigSupport {

    public abstract String getCurrentVersion();

    public abstract void setCurrentVersion(String version);

    public abstract String getPreviousVersion();

    public abstract void setPreviousVersion(String version);

    public abstract List<String> getInstalledVersions();

    public void switchVersion(String version) {
        setCurrentVersion("-".equals(version) ? getPreviousVersion() : version);
        setPreviousVersion(getCurrentVersion());
    }

    public void removeVersion(String version) {
        if (getInstalledVersions().contains(version)) {
            getInstalledVersions().remove(version);
        }
        if (getPreviousVersion().equals(version)) {
            setPreviousVersion(null);
        }
        if (getCurrentVersion().equals(version)) {
            setCurrentVersion(null);
        }
    }
}
