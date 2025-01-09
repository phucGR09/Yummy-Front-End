package com.example.yummy

import java.time.LocalDateTime

data class Review(
    val reviewId: Int,       // PK: Mã ID định danh đánh giá
    val orderId: Int,        // FK: Mã ID định danh của đơn hàng
    val reviewerId: Int,     // FK: Mã ID định danh của người đánh giá
    val rating: Int,         // Điểm đánh giá
    val comment: String,     // Bình luận (nvarchar(1000))
    val reviewTime: LocalDateTime // Thời gian đánh giá
)