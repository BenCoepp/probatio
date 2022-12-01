package de.bencoepp.entity;

import java.util.ArrayList;

public class Driver {
    private String name;
    private String description;
    private String installInstructions;
    private ArrayList<String[]> installCommands;
    private String fixInstructions;
    private ArrayList<String[]> fixCommands;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstallInstructions() {
        return installInstructions;
    }

    public void setInstallInstructions(String installInstructions) {
        this.installInstructions = installInstructions;
    }

    public ArrayList<String[]> getInstallCommands() {
        return installCommands;
    }

    public void setInstallCommands(ArrayList<String[]> installCommands) {
        this.installCommands = installCommands;
    }

    public String getFixInstructions() {
        return fixInstructions;
    }

    public void setFixInstructions(String fixInstructions) {
        this.fixInstructions = fixInstructions;
    }

    public ArrayList<String[]> getFixCommands() {
        return fixCommands;
    }

    public void setFixCommands(ArrayList<String[]> fixCommands) {
        this.fixCommands = fixCommands;
    }

    public void printFix(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("To fix the issue with " + this.name);
        stringBuilder.append("You need to follow the instructions below:");
        stringBuilder.append(this.fixInstructions);
        System.out.println(stringBuilder.toString());
    }
}

