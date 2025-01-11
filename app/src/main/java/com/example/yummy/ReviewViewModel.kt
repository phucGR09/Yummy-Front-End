package com.example.yummy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class ReviewViewModel : ViewModel() {
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    // Giả lập dữ liệu
    init {
        _reviews.value = listOf(
            Review(1, 1001, 201, 5, "Nước rất ngon, giao hàng rất tốt!", "2025-01-09 10:30", true),
            Review(2, 1002, 202, 4, "Món ăn rất ngon, sẽ quay lại!", "2025-01-08 14:20", false),
            Review(3, 1003, 203, 3, "Giao hàng chậm, nhưng thức ăn ổn.", "2025-01-07 18:45", false),
            Review(4, 1004, 204, 2, "Thức ăn bị nguội, không hài lòng.", "2025-01-06 12:00", false),
            Review(5, 1005, 205, 5, "Rất tuyệt vời!", "2025-01-05 09:00", true)
        )
    }

    // Lọc đánh giá chưa phản hồi
    fun getUnrespondedReviews(): List<Review> {
        return _reviews.value.filter { !it.isResponded }
    }
}
