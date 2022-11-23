package de.bencoepp.entity;

public class Project {
    private Integer id;
    private String name;
    private String path;

    private Integer type;
    public static Integer TYPE_DOCKER = 1;
    public static Integer TYPE_PODMAN = 2;
    public static Integer TYPE_REDHEAD = 3;

    private String pathToDockerfile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPathToDockerfile() {
        return pathToDockerfile;
    }

    public void setPathToDockerfile(String pathToDockerfile) {
        this.pathToDockerfile = pathToDockerfile;
    }

    public String getPreparedInsert(){
        String str = "INSERT INTO project (name,path,type,pathToDockerfile) VALUES (?,?,?,?)";
        return str;
    }
}
