package de.bencoepp.entity;

import com.jayway.jsonpath.JsonPath;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class CheckElement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean check;
    private String title;
    private String description;
    private String info;

    public Long getId() {
        return id;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void print(boolean verbose){
        if(this.check){
            System.out.println("[√] " + this.title + " (" + this.description + ")");
        }else{
            System.out.println("[!] " + this.title + " (" + this.description + ")");
        }
        if(verbose){
            System.out.println(this.info);
        }
    }

    public void fromJson(String json){
        this.title = JsonPath.read(json, "$.doctor.title");
        this.description = JsonPath.read(json, "$.doctor.description");
        this.check = JsonPath.read(json, "$.doctor.check");
        this.info = JsonPath.read(json, "$.doctor.info");
    }
}

