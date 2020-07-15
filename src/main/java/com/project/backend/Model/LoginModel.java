package com.project.backend.Model;


public class LoginModel {
    String id;
    String password;

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

    public LoginModel(String id, String password) {
        this.id = id;
        this.password = password;
    }
    
}