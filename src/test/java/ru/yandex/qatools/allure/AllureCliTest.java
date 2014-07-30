package ru.yandex.qatools.allure;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AllureCliTest extends AllureCli {

    @Test
    public void getInputDirectories() throws IOException {
        final Path tempDirectory = Files.createTempDirectory("allure-cli-test");
        System.setProperty("user.dir", tempDirectory.toString());

        final String SUBDIR_NAME = "subdir";
        final File subDirectory = new File(tempDirectory.toFile(), SUBDIR_NAME);
        if (!subDirectory.mkdir()) {
            throw new RuntimeException("Can't create temporary subdirectory for testing!");
        }

        List<String> paths = new ArrayList<String>() {
            {
                add(new File(tempDirectory.toFile(), "not-existing-dir1").getAbsolutePath());
                add(subDirectory.getAbsolutePath());
                add(new File(tempDirectory.toFile(), "not-existing-dir2").getAbsolutePath());
            }
        };

        List<File> inputFilesFromPaths = getInputDirectories(paths);
        assertEquals("Only 1 path should come from absolute paths", 1, inputFilesFromPaths.size());
        assertEquals("Path from absolute paths should be a " + subDirectory.getAbsolutePath(), subDirectory.getAbsolutePath(), inputFilesFromPaths.get(0).getAbsolutePath());

        List<String> globs = new ArrayList<String>() {
            {
                add("**/not-existing-dir1");
                add("**/" + SUBDIR_NAME);
                add("**/not-existing-dir2");
            }
        };
        List<File> inputFilesFromGlobs = getInputDirectories(globs);
        assertEquals("Only 1 path should come from globs", 1, inputFilesFromGlobs.size());
        assertEquals("Path from globs should be " + subDirectory.getAbsolutePath(), subDirectory.getAbsolutePath(), inputFilesFromGlobs.get(0).getAbsolutePath());
    }
}
