package model.pojo;

public enum PackageType {

    // The different package types sent between client -> handler -> server

    CLIENT_CONNECT,
    CLIENT_DISCONNECT,
    USER,
    MESSAGE, NEW_USER, ADD_CONTACT, GET_ONLINE_USERS, GET_USER

}
