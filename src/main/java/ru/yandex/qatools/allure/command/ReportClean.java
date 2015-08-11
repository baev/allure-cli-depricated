package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import ru.yandex.qatools.allure.logging.Messages;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author eroshenkoam@yandex-team.ru
 */
@Command(name = "clean", description = "Clean report directory")
public class ReportClean extends ReportCommand {

    /**
     * Remove the report directory.
     *
     * @throws IOException if any occurs.
     */
    @Override
    protected void runUnsafe() throws IOException {
        Path reportDirectory = getReportDirectory();
        Files.walkFileTree(reportDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                getLogger().debug(Messages.COMMAND_REPORT_CLEAN_ITEM_DELETED, file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                getLogger().debug(Messages.COMMAND_REPORT_CLEAN_ITEM_DELETED, dir);
                return FileVisitResult.CONTINUE;
            }
        });

        getLogger().info(Messages.COMMAND_REPORT_CLEAN_REPORT_CLEANED, reportDirectory);
    }
}
