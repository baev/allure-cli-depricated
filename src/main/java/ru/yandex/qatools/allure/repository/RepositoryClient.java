package ru.yandex.qatools.allure.repository;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public interface RepositoryClient {

    @GET("/ru/yandex/qatools/allure/allure-bundle/maven-metadata.xml")
    Metadata getMetadata();

    @GET("/ru/yandex/qatools/allure/allure-bundle/{version}/allure-bundle-{version}.jar")
    Response downloadBundle(@Path("version") String version);
}
