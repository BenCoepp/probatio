package de.bencoepp;


import de.bencoepp.command.*;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "probatio", mixinStandardHelpOptions = true, version = "demo v. 1.8",
        description = "The easiest and best way to test any docker application and generate deployments",
        commandListHeading = "%nCommands:%n%nThe most commonly used probatio commands are:%n",
        footer = "%nSee 'probatio help <command>' to read about a specific subcommand or concept.",
        subcommands = {
                AnalyzeCommand.class,
                DoctorCommand.class,
                InitCommand.class,
                TestCommand.class,
                MonitorCommand.class,
                RemoteCommand.class,
                CommandLine.HelpCommand.class
        })
public class Probatio implements Runnable{
    @CommandLine.Option(names = {"-V", "--version"}, versionHelp = true, description = "display version info")
    boolean versionInfoRequested;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help message")
    boolean usageHelpRequested;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    public Probatio() {
    }

    public static void main(String... args) throws IOException {
        System.exit(new CommandLine(new Probatio()).execute(args));
    }

    @Override
    public void run() {
        spec.commandLine().usage(System.err);
    }

}
