package de.bencoepp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class CommandHelper {

    public static boolean executeCommand(String[] commands, File dir) throws IOException, IOException {
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
            System.out.println(e);
        }
        return ok;
    }

    public static String executeCommandWithOutput(String[] commands) throws IOException, IOException {
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
                output += s;
            }
            while ((s = stdError.readLine()) != null) {
                output += s;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return output;
    }
}
