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

class LocalTimeAdapter : JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime {
        return LocalTime.parse(json.asString, formatter)
    }

    override fun serialize(src: LocalTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }
}

object ApiClient {
    private const val BASE_URL = "http://192.168.229.110:8080/api/v1/"  // Thay bằng địa chỉ IP backend

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var authToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJZdW1teS1iYWNrZW5kLXRlYW0iLCJzdWIiOiJhZG1pbiIsImV4cCI6MTczNjYxMDgwNiwiaWF0IjoxNzM2NTI0NDA2LCJzY29wZSI6IkFETUlOIn0.7v5IMTvMWx1_MRtf9YJsaA-yP89JWpTLVPWrN5VYqj9xKyN_33AykUQSuKucpJAKHMJEiCco9ImZBDpd59Mw2w"

    fun setAuthToken(token: String) {
        authToken = token
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
        .create()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}
