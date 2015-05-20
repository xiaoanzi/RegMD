package com.app.regmd.model;

import java.io.Serializable;

/**
 * Created by 王海 on 2015/4/8.
 */
public class User implements Serializable {
    private int id;
    private String name;
    private String password;
    private String email;
    private String flag; //1表示可以签到， 表示不能签到
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
