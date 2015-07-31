package ru.yandex.qatools.allure.command;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.08.15
 */
public class AllureCommandException extends Exception {

    public AllureCommandException(String message) {
        super(message);
    }

    public AllureCommandException(Throwable e) {
        super(e);
    }
}
