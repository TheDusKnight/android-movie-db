package com.example.trevormobilefilm;

public class SliderData {

    // image url is used to
    // store the url of image
    private String imgUrl;
    private String filmType;
    private int filmId;

    // Constructor method.
    public SliderData(String imgUrl, String filmType, int filmId) {
        this.imgUrl = imgUrl;
        this.filmType = filmType;
        this.filmId = filmId;
    }

    public String getFilmType() {
        return filmType;
    }

    public void setFilmType(String filmType) {
        this.filmType = filmType;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

