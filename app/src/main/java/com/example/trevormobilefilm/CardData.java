package com.example.trevormobilefilm;

public class CardData {
    private String imgUrl;
    private boolean add;
    private String filmType;
    private String filmName;
    private int filmId;
    private String date;
    private double rate;

    public CardData(String imgUrl, String filmType, String filmName, int filmId) {
        this.imgUrl = imgUrl;
        this.filmType = filmType;
        this.filmName = filmName;
        this.filmId = filmId;
    }

    public CardData(String imgUrl, String filmType, String filmName, int filmId, String date, double rate) {
        this.imgUrl = imgUrl;
        this.filmType = filmType;
        this.filmName = filmName;
        this.filmId = filmId;
        this.date = date;
        this.rate = rate;
    }

    public CardData(String imgUrl, boolean add, String filmType, String filmName, int filmId) {
        this.imgUrl = imgUrl;
        this.add = add;
        this.filmType = filmType;
        this.filmName = filmName;
        this.filmId = filmId;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
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
}
