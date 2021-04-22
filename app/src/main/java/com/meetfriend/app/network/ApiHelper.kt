package com.meetfriend.app.network


import com.google.gson.GsonBuilder
import com.meetfriend.app.utilclasses.ApiStatus
import com.meetfriend.app.utilclasses.AppConstants
import contractorssmart.app.utilsclasses.ErrorResponse
import com.meetfriend.app.utilclasses.Status
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {
    private var mRetrofit: Retrofit

    private var mRetrofitApp: Retrofit
    //private var okHttpClient = OkHttpClient.Builder()
    // Creating Retrofit Object
    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        mRetrofit = Retrofit.Builder()
            .baseUrl(AppConstants.APIS_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getClient())
            
     /*       .addConverterFactory(GsonConverterFactory.create(gson))*/
            .build()
        mRetrofitApp = Retrofit.Builder()
            .baseUrl(AppConstants.APIS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getAppClient())
           /* .addConverterFactory(GsonConverterFactory.create(gson))*/
            .build()
    }
    // Creating OkHttpclient Object
    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            
//            .addInterceptor(ChuckInterceptor(Application.getContext()))
            /*.addNetworkInterceptor(AddHeaderInterceptor())*/
            .build()
    }
    // Creating OkHttpclient Object
    private fun getAppClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
//            .addInterceptor(ChuckInterceptor(Application.getContext()))
            .addNetworkInterceptor(AddHeaderInterceptor())
            .build()
    }
    //val apiService: WebService = mRetrofit.create(WebService::class.java)
    //Creating service class for calling the web services
    fun createService(): WebService {
        return mRetrofit.create(WebService::class.java)
    }
    fun createAppService(): WebService {
        return mRetrofitApp.create(WebService::class.java)
    }
    // Handling error messages returned by Apis
    fun handleApiError(body: ResponseBody?): String {
        var errorMsg = AppConstants.SOMETHING_WENT_WRONG
        try {
            val errorConverter: Converter<ResponseBody, ApiStatus> =
                mRetrofit.responseBodyConverter(Status::class.java, arrayOfNulls(0))
            val error: ApiStatus = errorConverter.convert(body)!!
            errorMsg = error.message
        } catch (e: Exception) {
        }

        return errorMsg
    }

    // Handling error messages returned by Apis
    fun handleAuthenticationError(body: ResponseBody?): String {
        val errorConverter: Converter<ResponseBody, ErrorResponse> =
            mRetrofit.responseBodyConverter(ErrorResponse::class.java, arrayOfNulls(0))
        val errorResponse: ErrorResponse = errorConverter.convert(body)!!
        return errorResponse.message!!
    }
}