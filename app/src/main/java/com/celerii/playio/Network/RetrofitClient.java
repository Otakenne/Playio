package com.celerii.playio.Network;

import com.celerii.playio.Interfaces.MyAPIInterface;
import com.celerii.playio.Utility.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient retrofitClient = null;
    private final MyAPIInterface myAPIInterface;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPIInterface = retrofit.create(MyAPIInterface.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }

        return retrofitClient;
    }

    public MyAPIInterface getMyAPIInterface() {
        return myAPIInterface;
    }
}
