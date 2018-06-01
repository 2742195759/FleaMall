package com.univ.chat.util;

public class URL {

    //public static final String BASE_URL = "http://211.159.180.189:8080";
    public static final String BASE_URL = "http://" + Constants.REMOTE_IP + ":8080";
    public static final String LOGIN = BASE_URL + "/auth/login";
    public static final String REGISTER = BASE_URL + "/users";
    public static final String UPDATE_GCM = BASE_URL + "/users/%s";
    public static final String GET_ALL_USERS = BASE_URL + "/users?exclude=%s";
    public static final String GET_ALL_COMMUNICATIONS = BASE_URL + "/communication/%s";
    public static final String CREATE_COMMUNICATIONS = BASE_URL + "/communication/create";
    public static final String DEL_COMMUNICATIONS = BASE_URL + "/communication/delete/%s/%s";
    public static final String FETCH_CHAT_THREAD = BASE_URL + "/messages?from=%s&to=%s";
    public static final String SEND_MESSAGE = BASE_URL + "/messages";
    public static final String TYPING = BASE_URL + "/messages/typing";
}
