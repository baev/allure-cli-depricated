package ru.yandex.qatools.allure.utils;

import java.awt.*;
import java.net.URI;

/**
 * eroshenkoam
 * 13/08/14
 */
public class BrowserUtil {

    public static void openBrowser(URI url) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(url);
        } else {
            System.out.println("Can not open browser because this capability is not supported on your platform.");
        }

    }
}
