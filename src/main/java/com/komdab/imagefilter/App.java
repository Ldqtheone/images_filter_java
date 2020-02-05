package com.komdab.imagefilter;

public class App {

    /**
     *Main function
     * @param args  Command line options
     */
    public static void main(String[] args) {
        Commands.verifyCli(args);
        Display.announce(false);
    }
}