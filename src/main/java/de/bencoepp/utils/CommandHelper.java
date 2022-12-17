package de.bencoepp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandHelper {

    public static boolean executeCommand(String[] commands, File dir) throws IOException {
        boolean ok = true;
        try {
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
                ok = false;
            }

        }catch (Exception e){
            ok = false;
        }
        return ok;
    }

    public static String executeCommandWithOutput(String[] commands) throws IOException {
        String output = "";
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                output += "\n" + s;
            }
            while ((s = stdError.readLine()) != null) {
                output += "\n" + s;
            }

        }catch (Exception e){
            output += "\n" + e;
        }
        return output;
    }
}
