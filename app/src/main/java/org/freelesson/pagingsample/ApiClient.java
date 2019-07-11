package org.freelesson.pagingsample;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    static Retrofit retrofit=null;
    static {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logger).build();
        retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
