package org.example;

public class Location {
    private String county;
    private Integer sirutaCode;

    private String locality;
    private String adminUnit;
    private String address;
    private Integer latitude;
    private Integer longitude;

    public Location(String county, Integer sirutaCode) {
        this.county = county;
        this.sirutaCode = sirutaCode;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setAdminUnit(String adminUnit) {
        this.adminUnit = adminUnit;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }
}
