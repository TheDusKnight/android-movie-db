package com.example.trevormobilefilm;

public class CardData {
    private String imgUrl;
    private boolean add;
    private String filmType;
    private int filmId;

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

    public CardData(String imgUrl, boolean add, String filmType, int filmId) {
        this.imgUrl = imgUrl;
        this.add = add;
        this.filmType = filmType;
        this.filmId = filmId;
    }
}
