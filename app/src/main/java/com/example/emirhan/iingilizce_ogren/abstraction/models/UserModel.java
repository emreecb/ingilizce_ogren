package com.example.emirhan.iingilizce_ogren.abstraction.models;

import com.example.emirhan.iingilizce_ogren.abstraction.core.ICloneable;

/**
 * Created by emirhan on 12/16/2017.
 */

public class UserModel implements ICloneable<UserModel> {

    public String id;
    public String username;
    public String email;

    public UserModel(String id, String username, String email) {

        this.id = id;
        this.username = username;
        this.email = email;
    }

    public void clone(UserModel cloneFrom) {
        this.id = cloneFrom.id;
        this.username = cloneFrom.username;
        this.email = cloneFrom.email;
    }
}
