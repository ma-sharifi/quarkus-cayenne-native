package com.djem.dto;

/**
 * @author Mahdi Sharifi
 */
public class ShipmentDto {
    String name;

    public ShipmentDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShipmentDto(String name) {
        this.name = name;
    }
}
