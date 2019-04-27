package com.car.dao;

import com.car.bean.admin;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
public class AdminDao {
    public static void main(String args[]){
        Connector.getDatabase();
        admin admin1 = new admin();
        admin1.setAdminname(10001);
        admin1.setLogin(1);
        admin1.setLimit("管理员");
        admin1.save();
        System.out.println("ok");
    }
}
