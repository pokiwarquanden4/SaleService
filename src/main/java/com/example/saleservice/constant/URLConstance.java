package com.example.saleservice.constant;

public class URLConstance {
    //Login
    public static final int login_LimitRequest = 3;
    public static final int login_LimitSecond = 60;

    //readFile
    public static final String readFileURL = "/readFile/{serial}";
    public static final int readFileURL_LimitRequest = 3;
    public static final int readFileURL_LimitSecond = 60;

    //Create
    public static final String createURL = "/create";
    public static final int createURL_LimitRequest = 3;
    public static final int createURL_LimitSecond = 60;

    //{id}
    public static final String getByIdURL = "/{id}";
    public static final int getByIdURL_LimitRequest = 5;
    public static final int getByIdURL_LimitSecond = 60;

    //all
    public static final String getAllURL = "/all";
    public static final int getAllURL_LimitRequest = 1;
    public static final int getAllURL_LimitSecond = 10;
}
