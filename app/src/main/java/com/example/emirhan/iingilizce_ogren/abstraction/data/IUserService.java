package com.example.emirhan.iingilizce_ogren.abstraction.data;


/**
 * Created by emirhan on 12/17/2017.
 */

public interface IUserService<TResult, TUserModel> {

    TUserModel getAuthUser();
    TResult updateUser(TUserModel newModel);
}
