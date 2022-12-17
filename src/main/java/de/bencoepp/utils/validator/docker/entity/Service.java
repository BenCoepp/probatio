package de.bencoepp.utils.validator.docker.entity;

import java.util.Map;

public class Service {
    private String container_name;
    private String image;
    private String restart;
    private String[] ports;
    private String[] volumes;
    private Map<String, String> environment;
}
