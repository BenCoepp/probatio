package de.bencoepp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {

    public static boolean executeCommand(String[] commands) throws IOException, IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
            return false;
        }
        return true;
    }
}
