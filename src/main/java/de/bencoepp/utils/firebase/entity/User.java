package de.bencoepp.utils.firebase.entity;

import com.jayway.jsonpath.JsonPath;

public class User {
    private String kind;
    private String localId;
    private String email;
    private String displayName;
    private String idToken;
    private Boolean registered;
    private String refreshToken;
    private String expiresIn;

    public void fromJson(String json){
        this.kind = JsonPath.read(json, "$.user.kind");
        this.localId = JsonPath.read(json, "$.user.localId");
        this.email = JsonPath.read(json, "$.user.email");
        this.displayName = JsonPath.read(json, "$.user.displayName");
        this.idToken = JsonPath.read(json, "$.user.idToken");
        this.registered = JsonPath.read(json, "$.user.registered");
        this.refreshToken = JsonPath.read(json, "$.user.refreshToken");
        this.expiresIn = JsonPath.read(json, "$.user.expiresIn");
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
