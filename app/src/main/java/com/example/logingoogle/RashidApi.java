package com.example.logingoogle;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RashidApi {
    @GET("v2/rashid/city")
    Call<RashidData> getRashidCityData();
}