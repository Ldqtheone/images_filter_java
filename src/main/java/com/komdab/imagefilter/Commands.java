package com.komdab.imagefilter;

import org.apache.commons.cli.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class Commands to read / verify option in the CLI
 */
public class Commands {
    public static Logger logger;
    public static Options options;

    /**
     * Function to create option
     */
    private static void createOptions() {
        options = new Options();
        Option filter = Option.builder("f").longOpt("filters").argName("filter name").desc("Select filter to apply in picture").hasArg().valueSeparator(':').build();
        Option help = Option.builder("h").longOpt("help").desc("Return this message").build();
        Option input = Option.builder("i").longOpt("input-dir").argName("directory").desc("Select input directory from pictures").hasArg().build();
        Option output = Option.builder("o").longOpt("output-dir").argName("directory").desc("Select output directory from pictures").hasArg().build();
        Option list = Option.builder("lf").longOpt("list-filters").desc("show all filters available").build();
        Option config = Option.builder("cf").longOpt("config-file").argName("file").desc("Select an init file").hasArg().build();
        Option log = Option.builder("lg").longOpt("log-file").argName("file").desc("Select or create log file").hasArg().build();
        options.addOption(filter);
        options.addOption(help);
        options.addOption(input);
        options.addOption(output);
        options.addOption(list);
        options.addOption(config);
        options.addOption(log);
    }

    /**
     * Function to create Option config
     * @param args  Command line options
     * @return CLI's parser
     * @throws ParseException  return exception if the CLI can't parse
     */
    public static CommandLine commandCreate(String[] args) throws ParseException {
        createOptions();
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    /**
     * Function analyse CLI, configure and run the process
     * @param args  Command line options
     */
    public static void verifyCli(String[] args) {
        CommandLine line;
        try {
            Conf conf = new Conf("config.ini");
            line = Commands.commandCreate(args);

            if(line.hasOption("lg")) {
                logger = new Logger(line.getOptionValue("lg"));
            }
            else {
                logger = new Logger(conf.fileLog);
            }
            if (conf.created) {
                logger.write("File config.ini created.");
            }
            Display.announce(true);
            logger.write("Command line : " + Arrays.toString(args));

            if (line.hasOption("cf")) {
                conf = new Conf(line.getOptionValue("cf"));
            }

            String input = conf.input;
            String output = conf.output;
            String[] filters = conf.filters;

            if (line.hasOption("h")) {
                Display.displayHelp(options);
                return;
            }
            if (line.hasOption("lf")) {
                Display.displayFilter();
                return;
            }
            if (line.hasOption("filters")) {
                filters = line.getOptionValue("filters").split("\\|");
            }
            if (line.hasOption("i")) {
                input = line.getOptionValue("i");
                if (input.isEmpty()) {
                    String s = "No input directory enter !";
                    System.out.println(s);
                    logger.write(s);
                    return;
                }
            }
            if (!new File(input).exists()) {
                String s = "Directory " + input + " not found !";
                System.out.println(s);
                logger.write(s);
                return;
            }
            if (line.hasOption("o")) {
                output = line.getOptionValue("o");
                if (output.isEmpty()) {
                    String s = "No output directory enter !";
                    System.out.println(s);
                    logger.write(s);
                    return;
                }
            }
            if (new File(output).mkdir()) {
                String s = "New output directory " + output + " created !";
                System.out.println(s);
                logger.write(s);
            }

            String s = "Process started...";
            System.out.println(s);
            logger.write(s);
            s = "input directory : " + input;
            System.out.println(s);
            logger.write(s);
            s = "output directory : " + output;
            System.out.println(s);
            logger.write(s);
            process(input, output, filters);
            s = "Process finished !";
            System.out.println(s);
            logger.write(s);
        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            logger.write(e.getMessage());
        } catch (ParseException e) {
            String s = "Command error !";
            System.out.println(s);
            logger.write(s);
            System.out.println("Command error, wrong entry");
        }
    }

    /**
     * Function to run process filter
     */
    private static void process(String inputDir, String outputDir, String[] filters) {

        for (File f : getFilesFromDirectory(inputDir)) {
            String input = inputDir;
            for (String filter : filters) {
                ImagePath imagePath = new ImagePath(input, outputDir, f.getName());
                String[] args = filter.split(":");
                int n = 1;
                if (args.length > 1) {
                    try {
                        n = Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        String s = "Invalid parameter. Can't convert " + args[1] + " to an integer number !";
                        System.out.println(s);
                        Commands.logger.write(s);
                        return;
                    }
                }
                String s;
                try {
                    switch (args[0].toLowerCase()) {
                        case "blur":
                            Filter.blur(imagePath, n);
                            s = imagePath.getFileName() + " has been blured !";
                            break;
                        case "dilate":
                            Filter.dilate(imagePath, n);
                            s = imagePath.getFileName() + " has been dilated !";
                            break;
                        case "grayscale":
                            Filter.grayscale(imagePath);
                            s = imagePath.getFileName() + " has been turn in black & white !";
                            break;
                        case "zeteam":
                            Filter.zeTeam(imagePath);
                            s = imagePath.getFileName() + " your team is forever engraved in our memories !";
                            break;
                        default:
                            s = imagePath.getFileName() + " : Unknow " + args[0].toLowerCase() + " filter !";
                            System.out.println(s);
                            Commands.logger.write(s);
                            continue;
                    }
                } catch (Exception e) {
                    s = "An exception of type " + e.getClass() + " was throw !\n" + e.getMessage();
                }
                System.out.println(s);
                Commands.logger.write(s);
                input = outputDir;
            }
            String s = f.getName() + " process finished !";
            System.out.println(s);
            Commands.logger.write(s);
        }
    }

    /**
     *Function to select images
     * @return Files list
     */
    private static List<File> getFilesFromDirectory(String directory) {
        List<String> extensions = new ArrayList<String>();
        List<File> files = new ArrayList<File>();
        extensions.add("png");
        extensions.add("jpg");
        extensions.add("jpeg");

        String s = "Files selected :";
        System.out.println(s);
        Commands.logger.write(s);
        for (File f : Objects.requireNonNull(new File(directory).listFiles())) {
            String[] t = f.getName().split("\\.");
            if (t.length > 1) {
                if (extensions.indexOf(t[1]) != -1) {
                    System.out.println(f.getName());
                    Commands.logger.write(f.getName());
                    files.add(f);
                }
            }
        }
        return files;
    }
}