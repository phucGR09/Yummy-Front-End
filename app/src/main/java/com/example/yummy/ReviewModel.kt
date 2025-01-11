package com.example.yummy


data class Review(
    val reviewId: Int,          // Mã ID định danh đánh giá
    val orderId: Int,           // Mã ID định danh của đơn hàng
    val reviewerId: Int,        // Mã ID định danh của người đánh giá
    val rating: Int,            // Điểm đánh giá
    val comment: String,        // Bình luận
    val reviewTime: String,     // Thời gian đánh giá (định dạng datetime)
    val isResponded: Boolean    // Trạng thái đã phản hồi hay chưa
)
