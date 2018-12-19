package movie.android.com.microsltchallenge.libs.rest

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.movies.deveyesin.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()


    val service: ApiService by lazy {
        retrofit.create<ApiService>(ApiService::class.java)
    }

}