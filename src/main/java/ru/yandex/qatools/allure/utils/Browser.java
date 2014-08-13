package ru.yandex.qatools.allure.utils;

import java.awt.*;
import java.net.URI;

/**
 * eroshenkoam
 * 13/08/14
 */
public class Browser {

    public static void open(String url) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            System.out.println("Will not open browser because this capability is not supported on your platform.");
        }

    }
}
