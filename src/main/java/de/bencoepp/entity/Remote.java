package de.bencoepp.entity;

import com.jayway.jsonpath.JsonPath;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.ByteArrayOutputStream;

public class Remote {
    private String name;
    private String description;
    private String ip;
    private String user;
    private String password;
    private Integer port;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void fromJson(String json){
        this.name = JsonPath.read(json, "$.remote.name");
        this.description = JsonPath.read(json, "$.remote.description");
        this.ip = JsonPath.read(json, "$.remote.ip");
        this.user = JsonPath.read(json, "$.remote.user");
        this.password = JsonPath.read(json, "$.remote.password");
        this.port = JsonPath.read(json, "$.remote.port");
    }

    public void print(){
        int width = 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("╭─ " + this.name + " ");
        stringBuilder.append("─".repeat(Math.max(0, width - this.name.length() -1)));
        stringBuilder.append("─╮");
        stringBuilder.append("\n");
        String str = "│ Server: " + this.user + "@" + this.ip + " Password: " + this.password;
        stringBuilder.append(str);
        stringBuilder.append(" ".repeat(Math.max(0, width - str.length() + 4)));
        stringBuilder.append("│\n");
        String desc = WordUtils.wrap(this.description, 90);
        String[] strings = desc.split("\n");
        for (int j = 0; j < strings.length; j++) {
            String str1 = "│ " + strings[j];
            stringBuilder.append(str1);
            stringBuilder.append(" ".repeat(Math.max(0, width - str1.length() + 4)));
            stringBuilder.append("│\n");
        }
        stringBuilder.append("╰──");
        stringBuilder.append("─".repeat(Math.max(0, width)));
        stringBuilder.append("─╯");
        System.out.println(stringBuilder.toString());
    }

    public boolean testConnection() throws JSchException, InterruptedException {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession(user, ip, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("ls");
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = new String(responseStream.toByteArray());
            if(responseString.isBlank()){
                return false;
            }
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return true;
    }
}
