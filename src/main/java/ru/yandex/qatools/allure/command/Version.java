package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Manifest;

/**
 * eroshenkoam
 * 11/08/14
 */
@Command(name = "version", description = "Display version information")
public class Version extends AllureCommand {

    @Override
    public void runUnsafe() throws IOException {
        URL url = ((URLClassLoader) getClass().getClassLoader()).findResource("META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest(url.openStream());
        String specificationVersion = manifest.getMainAttributes().getValue("Specification-Version");
        if (specificationVersion != null) {
            System.out.println(specificationVersion);
        } else {
            System.out.println("Failed to load version from MANIFEST.MF. This is probably a bug.");
        }
    }
}
