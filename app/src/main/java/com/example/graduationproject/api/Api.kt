package com.example.graduationproject.api

import com.example.graduationproject.models.Response
import com.example.graduationproject.models.Result
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    // TODO: write the last word in the url in the value field
    // TODO: name of the function preferred to be the same name of the value
    // TODO: return type of the CALL should be the type of the message received
    @FormUrlEncoded
    @POST("v1/user/signUp")
    fun signUp(
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("email") email:String,
        @Field("mobileNumber") mobileNumber:String,
        @Field("password") password:String
    ): Call<Response>


    @FormUrlEncoded
    @POST("v1/user/logIn")
    fun logIn(
        @Field("email") email:String,
        @Field("mobileNumber") mobileNumber:String,
        @Field("password") password:String
    ): Call<Response>


    @FormUrlEncoded
    @POST("Errors")
    fun Errors(
        @Field("ApiKey") ApiKey:String,
        @Field("ApiSecret") ApiSecret:String,
        @Field("projectKey") projectKey:String,
        @Field("Column") Column:Int,
        @Field("Row") Row:Int,
        @Field("FileName") FileName:String,
        @Field("Path") Path:String,
        @Field("Description") Description:String,
        @Field("Title") Title:String
    ): Call<Void> // return type should be changed to Void
}