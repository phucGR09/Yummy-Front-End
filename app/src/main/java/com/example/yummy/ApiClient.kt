package com.example.yummy

import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import java.time.LocalDateTime


class LocalTimeAdapter : JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime {
        return LocalTime.parse(json.asString, formatter)
    }

    override fun serialize(src: LocalTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }
}

class LocalDateTimeAdapter : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(json.asString, formatter)
    }

    override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }
}

object ApiClient {
    private const val BASE_URL = "http://192.168.1.3:8080/api/v1/"  // Thay bằng địa chỉ IP backend

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var authToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJZdW1teS1iYWNrZW5kLXRlYW0iLCJzdWIiOiJvd25lcjEiLCJleHAiOjE3MzY3MDQ4NjYsImlhdCI6MTczNjYxODQ2Niwic2NvcGUiOiJSRVNUQVVSQU5UX09XTkVSIn0.gUgqm4S8mbwNLGrOsC1EEk2fQdW6lN-KP7IwsBWL33g_CVlFg32WVRbEHVBs_ZnBE3xmuvBxG2okcIB7UhcftA"
    private var restaurantId = 1
    fun setAuthToken(token: String) {
        authToken = token
    }

    fun setRestaurantId(id: Int){
        restaurantId = id
    }

    fun getRestaurantId(): Int{
        return restaurantId
    }

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        // Thêm header Authorization với token Bearer
        requestBuilder.addHeader("Authorization", "Bearer $authToken")

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}
