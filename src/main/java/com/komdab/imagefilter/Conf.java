package com.komdab.imagefilter;

import org.ini4j.Ini;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class Conf to create a config
 */
public class Conf {
    public String input;
    public String output;
    public String fileLog;
    public String[] filters;
    public boolean created;

    /**
     * Constructor
     * @throws FileNotFoundException  return exception if file not exists
     */
    public Conf(String fileName) throws FileNotFoundException {
        File config = new File(fileName);
        if(!fileName.equals("config.ini")) {
            if(!config.exists()) {
                throw new FileNotFoundException("File " + fileName + " not found !");
            }
        }
        if(!config.exists())
            createConfigDefault();

        Ini ini = null;
        try {
            ini = new Ini(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert ini != null;
        this.filters = ini.get("config", "filters").split("\\|");
        this.input = ini.get("config", "inputDir");
        this.output = ini.get("config", "outputDir");
        this.fileLog = ini.get("config","logFile");
    }

    /**
     * Method to create default configuration
     */
    private void createConfigDefault() {
        try {
            FileWriter myWriter = new FileWriter("config.ini");
            myWriter.write("[config]\n");
            myWriter.write("inputDir = imgs\n");
            myWriter.write("outputDir = output_imgs\n");
            myWriter.write("logFile = image.log\n");
            myWriter.write("filters = blur:3|grayscale\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred to create init file.");
            e.printStackTrace();
        }
        created = true;
    }
}