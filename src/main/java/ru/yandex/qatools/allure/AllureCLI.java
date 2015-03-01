package ru.yandex.qatools.allure;

import io.airlift.command.*;
import ru.yandex.qatools.allure.command.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.05.14
 */
public class AllureCLI {

    public static void main(String[] args) {
        try {
            Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("allure")
                    .withDescription("Allure command line tool")
                    .withDefaultCommand(Help.class)
                    .withCommand(Help.class)
                    .withCommand(Demo.class)
                    .withCommand(Version.class)
                    .withCommand(ReportGenerate.class);

            builder.withGroup("report")
                    .withDescription("Report commands")
                    .withDefaultCommand(ReportOpen.class)
                    .withCommand(ReportOpen.class)
                    .withCommand(ReportClean.class)
                    .withCommand(ReportGenerate.class);


            Cli<Runnable> allureParser = builder.build();

            
            Runnable command = allureParser.parse(args);
            command.run();
            if (command instanceof AllureCommand) {
                AllureCommand allureCommand = (AllureCommand) command;
                System.exit(allureCommand.getExitCode().getCode());
            } else {
                System.exit(ExitCode.NO_ERROR.getCode());
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(ExitCode.ARGUMENT_PARSING_ERROR.getCode());
        }
    }
}
