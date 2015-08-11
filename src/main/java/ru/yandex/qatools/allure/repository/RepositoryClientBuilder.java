package ru.yandex.qatools.allure.repository;

import retrofit.RestAdapter;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public class RepositoryClientBuilder {

    public static final String ENDPOINT_URL = "https://oss.sonatype.org/content/repositories/releases";

    private RepositoryClientBuilder() {
    }

    public static RepositoryClient newClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT_URL)
                .setConverter(new XmlConverter())
                .build();
        return restAdapter.create(RepositoryClient.class);
    }
}
