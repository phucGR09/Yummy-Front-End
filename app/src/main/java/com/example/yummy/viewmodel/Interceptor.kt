import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("AuthInterceptor", "Interceptor invoked")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token") // Thêm token vào header
            .build()
        return chain.proceed(request)
    }
}
