package com.example.trevormobilefilm;

public class SliderData {

    // image url is used to
    // store the url of image
    private String imgUrl;
    private String filmType;
    private String filmName;

    private int filmId;

    // Constructor method.
    public SliderData(String imgUrl, String filmType, int filmId, String filmName) {
        this.imgUrl = imgUrl;
        this.filmType = filmType;
        this.filmId = filmId;
        this.filmName = filmName;
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

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }
}

