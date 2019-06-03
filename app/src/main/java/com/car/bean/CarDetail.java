package com.car.bean;

import com.forum.model.entity.Ac_refrigerator;
import com.forum.model.entity.Body;
import com.forum.model.entity.Chassis_steering;
import com.forum.model.entity.Control_configuration;
import com.forum.model.entity.Engine;
import com.forum.model.entity.External_configuration;
import com.forum.model.entity.Glass_rearviewmirror;
import com.forum.model.entity.High_tech_configuration;
import com.forum.model.entity.Internal_configuration;
import com.forum.model.entity.Light_configuration;
import com.forum.model.entity.Media_configuration;

public class CarDetail {
    String carnum;
    Internal_configuration internal_configuration;
    Glass_rearviewmirror glass_rearviewmirror;
    External_configuration external_configuration;
    Control_configuration control_configuration;
    Light_configuration light_configuration;
    High_tech_configuration high_tech_configuration;
    Body body;
    Ac_refrigerator ac_refrigerator;
    Engine engine ;
    Chassis_steering chassis_steering;
    Media_configuration media_configuration;

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public Internal_configuration getInternal_configuration() {
        return internal_configuration;
    }

    public void setInternal_configuration(Internal_configuration internal_configuration) {
        this.internal_configuration = internal_configuration;
    }

    public Glass_rearviewmirror getGlass_rearviewmirror() {
        return glass_rearviewmirror;
    }

    public void setGlass_rearviewmirror(Glass_rearviewmirror glass_rearviewmirror) {
        this.glass_rearviewmirror = glass_rearviewmirror;
    }

    public External_configuration getExternal_configuration() {
        return external_configuration;
    }

    public void setExternal_configuration(External_configuration external_configuration) {
        this.external_configuration = external_configuration;
    }

    public Control_configuration getControl_configuration() {
        return control_configuration;
    }

    public void setControl_configuration(Control_configuration control_configuration) {
        this.control_configuration = control_configuration;
    }

    public Light_configuration getLight_configuration() {
        return light_configuration;
    }

    public void setLight_configuration(Light_configuration light_configuration) {
        this.light_configuration = light_configuration;
    }

    public High_tech_configuration getHigh_tech_configuration() {
        return high_tech_configuration;
    }

    public void setHigh_tech_configuration(High_tech_configuration high_tech_configuration) {
        this.high_tech_configuration = high_tech_configuration;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Ac_refrigerator getAc_refrigerator() {
        return ac_refrigerator;
    }

    public void setAc_refrigerator(Ac_refrigerator ac_refrigerator) {
        this.ac_refrigerator = ac_refrigerator;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Chassis_steering getChassis_steering() {
        return chassis_steering;
    }

    public void setChassis_steering(Chassis_steering chassis_steering) {
        this.chassis_steering = chassis_steering;
    }

    public Media_configuration getMedia_configuration() {
        return media_configuration;
    }

    public void setMedia_configuration(Media_configuration media_configuration) {
        this.media_configuration = media_configuration;
    }

    @Override
    public String toString() {
        return "CarDetail{" +
                "carnum='" + carnum + '\'' +
                ", internal_configuration=" + internal_configuration +
                ", glass_rearviewmirror=" + glass_rearviewmirror +
                ", external_configuration=" + external_configuration +
                ", control_configuration=" + control_configuration +
                ", light_configuration=" + light_configuration +
                ", high_tech_configuration=" + high_tech_configuration +
                ", body=" + body +
                ", ac_refrigerator=" + ac_refrigerator +
                ", engine=" + engine +
                ", chassis_steering=" + chassis_steering +
                ", media_configuration=" + media_configuration +
                '}';
    }

}
