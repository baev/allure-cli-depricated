package ru.yandex.qatools.allure;

import io.airlift.command.Cli;
import io.airlift.command.ParseException;
import ru.yandex.qatools.allure.command.AllureCommand;
import ru.yandex.qatools.allure.command.BundleList;
import ru.yandex.qatools.allure.command.BundleRemove;
import ru.yandex.qatools.allure.command.BundleSwitch;
import ru.yandex.qatools.allure.command.BundleUpgrade;
import ru.yandex.qatools.allure.command.Demo;
import ru.yandex.qatools.allure.command.AllureHelp;
import ru.yandex.qatools.allure.command.ReportClean;
import ru.yandex.qatools.allure.command.ReportGenerate;
import ru.yandex.qatools.allure.command.ReportOpen;
import ru.yandex.qatools.allure.command.Version;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.05.14
 */
public class AllureCLI {

    public static void main(String[] args) {
        try {
            Cli.CliBuilder<AllureCommand> builder = Cli.<AllureCommand>builder("allure")
                    .withDescription("Allure command line tool")
                    .withDefaultCommand(AllureHelp.class)
                    .withCommand(AllureHelp.class)
                    .withCommand(BundleList.class)
                    .withCommand(BundleSwitch.class)
                    .withCommand(BundleUpgrade.class)
                    .withCommand(Version.class)
                    .withCommand(Demo.class)
                    .withCommand(ReportGenerate.class);

            builder.withGroup("report")
                    .withDescription("Report commands")
                    .withDefaultCommand(ReportOpen.class)
                    .withCommand(ReportOpen.class)
                    .withCommand(ReportClean.class)
                    .withCommand(ReportGenerate.class);

            builder.withGroup("bundle")
                    .withDescription("Bundle commands")
                    .withDefaultCommand(BundleList.class)
                    .withCommand(BundleList.class)
                    .withCommand(BundleSwitch.class)
                    .withCommand(BundleRemove.class)
                    .withCommand(BundleUpgrade.class);

            Cli<AllureCommand> allureParser = builder.build();

            AllureCommand command = allureParser.parse(args);

            command.run();
            System.exit(command.getExitCode().getCode());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(ExitCode.ARGUMENT_PARSING_ERROR.getCode());
        }
    }
}
