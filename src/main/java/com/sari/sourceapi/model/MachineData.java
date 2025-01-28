package com.sari.sourceapi.model;

import com.opencsv.bean.CsvBindByName;

public class MachineData {
    @CsvBindByName(column = "UDI")
    private String UDI;

    @CsvBindByName(column = "ProductID")
    private String productId;

    @CsvBindByName(column = "Type")
    private String type;

    @CsvBindByName(column = "Airtemperature")
    private String airTemperature;

    @CsvBindByName(column = "Processtemperature")
    private String processTemperature;

    @CsvBindByName(column = "Rotationalspeed")
    private String rotationalSpeed;

    @CsvBindByName(column = "Torque")
    private String torque;

    @CsvBindByName(column = "Toolwear")
    private String toolWear;

    @CsvBindByName(column = "Machinefailure")
    private String machineFailure;

    @CsvBindByName(column = "TWF")
    private String TWF;

    @CsvBindByName(column = "HDF")
    private String HDF;

    @CsvBindByName(column = "PWF")
    private String PWF;

    @CsvBindByName(column = "OSF")
    private String OSF;

    @CsvBindByName(column = "RNF")
    private String RNF;

    @CsvBindByName(column = "Floor")
    private String Floor;

    @CsvBindByName(column = "Area")
    private String Area;
    // Getters and Setters

    public String getUDI() {
        return UDI;
    }

    public void setUDI(String UDI) {
        this.UDI = UDI;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(String airTemperature) {
        this.airTemperature = airTemperature;
    }

    public String getProcessTemperature() {
        return processTemperature;
    }

    public void setProcessTemperature(String processTemperature) {
        this.processTemperature = processTemperature;
    }

    public String getRotationalSpeed() {
        return rotationalSpeed;
    }

    public void setRotationalSpeed(String rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed;
    }

    public String getTorque() {
        return torque;
    }

    public void setTorque(String torque) {
        this.torque = torque;
    }

    public String getToolWear() {
        return toolWear;
    }

    public void setToolWear(String toolWear) {
        this.toolWear = toolWear;
    }

    public String getMachineFailure() {
        return machineFailure;
    }

    public void setMachineFailure(String machineFailure) {
        this.machineFailure = machineFailure;
    }

    public String getTWF() {
        return TWF;
    }

    public void setTWF(String TWF) {
        this.TWF = TWF;
    }

    public String getHDF() {
        return HDF;
    }

    public void setHDF(String HDF) {
        this.HDF = HDF;
    }

    public String getPWF() {
        return PWF;
    }

    public void setPWF(String PWF) {
        this.PWF = PWF;
    }

    public String getOSF() {
        return OSF;
    }

    public void setOSF(String OSF) {
        this.OSF = OSF;
    }

    public String getRNF() {
        return RNF;
    }

    public void setRNF(String RNF) {
        this.RNF = RNF;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }
}
