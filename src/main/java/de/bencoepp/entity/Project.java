package de.bencoepp.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.text.WordUtils;

public class Project {
    private String title;
    private String description;
    private String root;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void fromJson(String json) {
        this.title = JsonPath.read(json, "$.project.title");
        this.description = JsonPath.read(json, "$.project.description");
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void print(){
        int width = 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("╭─ " + this.title + " ");
        stringBuilder.append("─".repeat(Math.max(0, width - this.title.length() -1)));
        stringBuilder.append("─╮");
        stringBuilder.append("\n");
        String str = "│ Project Root: " + this.root;
        stringBuilder.append(str);
        stringBuilder.append(" ".repeat(Math.max(0, width - str.length() + 4)));
        stringBuilder.append("│\n");
        //noinspection deprecation
        String desc = WordUtils.wrap(this.description, 90);
        String[] strings = desc.split("\n");
        for (String string : strings) {
            String str1 = "│ " + string;
            stringBuilder.append(str1);
            stringBuilder.append(" ".repeat(Math.max(0, width - str1.length() + 4)));
            stringBuilder.append("│\n");
        }
        stringBuilder.append("╰──");
        stringBuilder.append("─".repeat(width));
        stringBuilder.append("─╯");
        System.out.println(stringBuilder);
    }
}
