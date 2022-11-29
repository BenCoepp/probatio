package de.bencoepp.entity;

import java.io.File;
import java.util.ArrayList;

public class App {
    private String hostname;
    private String ip;
    private Integer status;
    private Integer appStatus;
    private ArrayList<Project> projects;

    private String currentDir = System.getProperty("user.dir");

    public static void init(){
        String homeDir = System.getProperty("user.home");
        File probatio_conf = new File(homeDir + "\\probatio_conf.json");
        if(probatio_conf.exists()){
            //TODO read file and make app ready
        }else{
            //TODO add creation
        }
    }
}
