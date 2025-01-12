    package com.example.yummy

    import com.example.yummy.models.ApiResponse
    import com.example.yummy.response.MenuItemResponse
    import retrofit2.http.Body
    import retrofit2.http.POST
    import retrofit2.Call // Đây là lớp cơ bản để định nghĩa các API call trong Retrofit
    import retrofit2.http.Path
    import retrofit2.http.PUT
    import retrofit2.http.DELETE
    import retrofit2.http.GET
    import retrofit2.http.PATCH
    import java.time.LocalDateTime
    import retrofit2.Response
    import retrofit2.http.Query
    import java.time.LocalTime


    data class OrderUpdateRequest(
        val id: Int,
        val restaurantId: Int,
        val driverUsername: String? = null,
        val customerUsername: String? = null,
        val orderTime: String,
        val totalPrice: Double,
        val status: String
    )


    data class CreateMenuItemRequest(
        val name: String ="",
        val price: Double = 0.0,
        val description: String = "",
        val imageUrl: String = "",
        val restaurantId: Int
    )

    data class UpdateMenuItemRequest(
        val id: Int,
        val name: String,
        val price: Double= 0.0,
        val description: String ="",
        val imageUrl: String ="",
        val restaurantId: Int
    )

    data class UpdateMenuItemResponse(
        val code: Int,
        val message: String,
        val result: MenuItemResult
    )


    data class CreateMenuItemResponse(
        val code: Int,
        val message: String,
        val result: MenuItemResult
    )

    data class MenuItemsResponse(
        val code: Int,
        val message: String,
        val result: List<MenuItemResult>

    )

    data class UpdateOrderResponse(
        val code: Int,
        val message: String?,
        val result: OrderDetails // Chứa thông tin chi tiết của đơn hàng sau khi cập nhật
    )


    data class OrderResponse(
        val code: Int,
        val message: String,
        val result: List<OrderDetails>
    )

    data class OrderDetails(
        val id: Int,
        val restaurant: RestaurantDetails,
        val driver: DriverDetails?,
        val customer: CustomerDetails,
        val orderTime: LocalDateTime, // Đổi sang LocalDateTime
        val totalPrice: Double,
        val status: String
    )

    data class DriverDetails(
        val id: Int,
        val avatarUrl: String?,
        val licensePlate: String,
        val identityNumber: String,
        val user: UserDetails
    )

    data class CustomerDetails(
        val id: Int,
        val address: String,
        val user: UserDetails
    )

    data class MenuItemResult(
        val id: Int,
        val name: String,
        val price: Double,
        val description: String= "",
        val imageUrl: String= "",
        val restaurant: RestaurantDetails
    )

    data class RestaurantDetails(
        val id: Int,
        val name: String,
        val address: String,
        val openingHours: String,
        val owner: OwnerDetails
    )


    data class OwnerDetails(
        val id: Int,
        val taxNumber: String,
        val user: UserDetails
    )

    data class UserDetails(
        val id: Int,
        val username: String,
        val email: String,
        val fullName: String,
        val phoneNumber: String,
        val userType: String
    )

    data class RestaurantResponse(
        val code: Int,
        val message: String,
        val result: List<RestaurantDetails>
    )


    interface ApiService {
        @PATCH("admin/menu-items/update")
        suspend fun updateMenuItem(@Body request: UpdateMenuItemRequest): Response<UpdateMenuItemResponse>


        @DELETE("admin/menu-items/delete/id")
        suspend fun deleteDish(@Query("id") id: Int): Response<Unit>

        @GET("admin/menu-items")
        suspend fun getMenu(): Response<MenuItemsResponse>


        @POST("admin/menu-items/create")
        suspend fun createMenuItem(@Body request: CreateMenuItemRequest): Response<CreateMenuItemResponse>

        @GET("admin/restaurants/username")
        suspend fun getRestaurantsByUsername(
            @Query("username") username: String
        ): Response<RestaurantResponse>

        @GET("admin/menu-items/restaurant-id")
        suspend fun getMenuItemByRestaurantId(@Query("id") id: Int) : Response<MenuItemsResponse>

        @GET("api/v1/admin/menu-items/contained-dish-name/name")
        suspend fun getMenuItemsByContainedDishName(@Query("name") name: String): Response<MenuItemsResponse>
        @GET("admin/orders/restaurant-id")
        suspend fun getOrdersByRestaurantId(
            @Query("restaurant-id") restaurantId: Int
        ): Response<OrderResponse>

        @PATCH("admin/orders/update")
        suspend fun updateOrder(@Body order: OrderUpdateRequest): Response<UpdateOrderResponse>
    }
