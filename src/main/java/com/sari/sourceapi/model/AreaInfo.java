package com.sari.sourceapi.model;

public class AreaInfo {
    private final int id;
    private final String name;
    private final int floor;

    public AreaInfo(int id, String name, int floor) {
        this.id = id;
        this.name = name;
        this.floor = floor;
    }

    public int getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }
}
