package de.bencoepp;

import picocli.CommandLine;

@CommandLine.Command(
        name = "hello",
        description = "Says hello"
)
public class Probatio implements Runnable{
    public static void main(String... args) {
        int exitCode = new CommandLine(new Probatio()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Hello World!");
    }
}