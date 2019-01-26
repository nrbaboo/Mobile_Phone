package com.example.nrbaboo.scbtest;

public class FavoriteMobile {

    public static final String DATABASE_NAME = "favorite_mobile_list.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "favorite";

    private int id;

    public class Column {
        public static final String ID = "MOBILE_ID";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
