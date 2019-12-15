package me.gincos.rhoexercise.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.gincos.rhoexercise.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer


class RetrofitClient {

    private val okHttpClient: OkHttpClient
        get() {
            val consumer = OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET)
            consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_SECRET)

            val builder = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
                builder
                    .addInterceptor(loggingInterceptor)
            }

            return builder.build()
        }

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val apiService: EndpointInterface = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(EndpointInterface::class.java)

}

const val BASE_URL = "https://stream.twitter.com/1.1/"