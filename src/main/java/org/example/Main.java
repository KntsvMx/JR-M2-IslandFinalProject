package org.example;

import org.example.config.AppConfigurator;

public class Main {
    public static void main(String[] args) {
        AppConfigurator  appConfigurator = AppConfigurator.getInstance();
        appConfigurator.configure();
    }
}