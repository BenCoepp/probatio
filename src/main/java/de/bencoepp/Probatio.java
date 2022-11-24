package de.bencoepp;

import de.bencoepp.command.InitCommand;
import de.bencoepp.command.InstallCommand;
import de.bencoepp.command.ProjectCommand;
import de.bencoepp.command.ScanCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "probatio", mixinStandardHelpOptions = true, version = "demo v. 1.8",
        description = "The easiest and best way to test any docker application and genereate deployments",
        commandListHeading = "%nCommands:%n%nThe most commonly used probatio commands are:%n",
        footer = "%nSee 'probatio help <command>' to read about a specific subcommand or concept.",
        subcommands = {
                InitCommand.class,
                ProjectCommand.class,
                InstallCommand.class,
                ScanCommand.class,
                CommandLine.HelpCommand.class
        })
public class Probatio implements Runnable{
    @CommandLine.Option(names = {"-V", "--version"}, versionHelp = true, description = "display version info")
    boolean versionInfoRequested;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help message")
    boolean usageHelpRequested;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    public static void main(String... args) {
        System.exit(new CommandLine(new Probatio()).execute(args));
    }

    @Override
    public void run() {
        spec.commandLine().usage(System.err);
    }
}