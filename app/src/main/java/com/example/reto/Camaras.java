package com.example.reto;

public class Camaras {

    private String cameraId;
    private String cameraName;
    private String urlImage;
    private double latitude;
    private double longitude;
    private String road;
    private String kilometer;

    // Constructor
    public Camaras(String cameraId, String cameraName, String urlImage, double latitude, double longitude, String road, String kilometer) {
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.urlImage = urlImage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.road = road;
        this.kilometer = kilometer;
    }

    // Getters
    public String getCameraId() {
        return cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRoad() {
        return road;
    }

    public String getKilometer() {
        return kilometer;
    }
}

