package com.car.bean;


import org.litepal.crud.DataSupport;

public class admin extends DataSupport {
    private int adminname;
    private int Login;
    private String limit;

    public void setAdminname(int adminname) {
        this.adminname = adminname;
    }

    public void setLogin(int Login) {
        this.Login = Login;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public int getAdminname() {
        return adminname;
    }

    public String getLimit() {
        return limit;
    }
}
