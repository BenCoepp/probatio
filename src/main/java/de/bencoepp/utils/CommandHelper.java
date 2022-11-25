package de.bencoepp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandHelper {

    public static boolean executeCommand(String[] commands, File dir) throws IOException, IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands,null, dir);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));
        String s = null;
        while ((s = stdInput.readLine()) != null) {
        }
        while ((s = stdError.readLine()) != null) {
        }
        return true;
    }

    public static String executeCommandWithOutput(String[] commands) throws IOException, IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));
        String output = "";
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            output += s;
        }
        while ((s = stdError.readLine()) != null) {
            output += s;
        }
        return output;
    }
}
