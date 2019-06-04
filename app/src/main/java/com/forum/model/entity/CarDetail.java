package com.forum.model.entity;

public class CarDetail {
    String carid;

    @Override
    public String toString() {
        return "CarDetail{" +
                "carid='" + carid + '\'' +
                ", carnum='" + carnum + '\'' +
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
                ", basic_parameter=" + basic_parameter +
                ", brand=" + brand +
                ", safety_equipment=" + safety_equipment +
                ", seat_configuration=" + seat_configuration +
                ", transmission=" + transmission +
                ", wheel_brake=" + wheel_brake +
                '}';
    }

    public String getCarid() {
        return carid;
    }

    public Basic_parameter getBasic_parameter() {
        return basic_parameter;
    }

    public void setBasic_parameter(Basic_parameter basic_parameter) {
        this.basic_parameter = basic_parameter;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Safety_equipment getSafety_equipment() {
        return safety_equipment;
    }

    public void setSafety_equipment(Safety_equipment safety_equipment) {
        this.safety_equipment = safety_equipment;
    }

    public Seat_configuration getSeat_configuration() {
        return seat_configuration;
    }

    public void setSeat_configuration(Seat_configuration seat_configuration) {
        this.seat_configuration = seat_configuration;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Wheel_brake getWheel_brake() {
        return wheel_brake;
    }

    public void setWheel_brake(Wheel_brake wheel_brake) {
        this.wheel_brake = wheel_brake;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

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

    Basic_parameter  basic_parameter ;
    Brand  brand ;

    Safety_equipment  safety_equipment ;

    Seat_configuration  seat_configuration ;

    Transmission  transmission ;

    Wheel_brake  wheel_brake;

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

}
