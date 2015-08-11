package ru.yandex.qatools.allure.command;

import ru.yandex.qatools.allure.ExitCode;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.08.15
 */
public class AllureHelp extends io.airlift.command.Help implements AllureCommand {

    /**
     * {@inheritDoc}
     */
    @Override
    public ExitCode getExitCode() {
        return ExitCode.NO_ERROR;
    }
}
