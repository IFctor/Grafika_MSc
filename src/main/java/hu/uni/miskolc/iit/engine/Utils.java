package hu.uni.miskolc.iit.engine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static hu.uni.miskolc.iit.config.AppConfig.appConfig;

public class Utils {

    private static final String RESOURCES_LOCATION = appConfig().getResources().getLocation() + File.separator;

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static String loadFile(String fileName) {

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(RESOURCES_LOCATION + fileName));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
        // allocate a string builder to add line per line
        StringBuilder strBuilder = new StringBuilder();

        try {
            String line = reader.readLine();
            // get text from file, line per line
            while (line != null) {
                strBuilder.append(line);
                strBuilder.append("\n");
                line = reader.readLine();
            }
            // close resources
            reader.close();
            // stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strBuilder.toString();
    }
}