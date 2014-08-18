package ru.yandex.qatools.allure;

import io.airlift.command.*;
import ru.yandex.qatools.allure.command.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.05.14
 */
public class AllureCLI {

    public static void main(String[] args) {
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("allure")
                .withDescription("Allure command line tool")
                .withDefaultCommand(Help.class)
                .withCommand(Help.class)
                .withCommand(Demo.class)
                .withCommand(Version.class);

        builder.withGroup("report")
                .withDescription("Report commands")
                .withDefaultCommand(ReportOpen.class)
                .withCommand(ReportOpen.class)
                .withCommand(ReportClean.class)
                .withCommand(ReportGenerate.class);


        Cli<Runnable> allureParser = builder.build();

        allureParser.parse(args).run();
    }
}
