package com.komdab.imagefilter;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import java.lang.reflect.Method;

public class Display {
    /**
     * Function to display filter
     */
    public static void displayFilter() {
        Filter obj = new Filter();

        Class cls = obj.getClass();
        System.out.println("Here are the filters at your disposal");
        System.out.println("-----------------");

        Method[] methods = cls.getDeclaredMethods();
        for (Method method:methods)
            System.out.println(method.getName());

        System.out.println("-----------------");
    }

    /**
     * Function to display all available options
     * @param options collection of CLI's options
     */
    public static void displayHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("help", options, true);
    }

    /**
     * Function to print start and finish message
     */
    public static void announce(boolean starting)
    {
        String s = starting ? "App imageFilter has started..." : "App imageFilter has finished...";
        System.out.println(s);
        Commands.logger.write(s);
    }
}
