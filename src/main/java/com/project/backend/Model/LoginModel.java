package com.project.backend.Model;


public class LoginModel {
    String id;
    String password;
    String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginModel() {
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginModel(String id, String password, String token) {
        this.id = id;
        this.password = password;
        this.token = token;
    }
    
}