package com.example.yummy
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Category(
    val name: String,
    val description: String?, // Mô tả danh mục (tùy chọn)
    val order: Int, // Số thứ tự
    val isVisible: Boolean, // Hiển thị danh mục
    val isAvailable: Boolean, // Còn sẵn để đặt hàng
    val dishes: MutableList<Dish> // Danh sách món ăn trong danh mục
)

data class Dish(
    val name: String,
    val price: Int,
    val description: String?, // Mô tả món ăn (tùy chọn)
    var imagePath: String?, // Đường dẫn đến ảnh món ăn
    var isVisible: Boolean, // Hiển thị trên thực đơn
    var isAvailable: Boolean // Còn hàng
)



class MenuSellerViewModel : ViewModel() {
    private val _categories = MutableStateFlow(
        listOf(
            Category(
                name = "Nước uống",
                description = "Các loại nước giải khát",
                order = 1,
                isVisible = true,
                isAvailable = true,
                dishes = mutableListOf(
                    Dish("Trà sữa", 25000, "Ngọt và ngon", null, true, true)
                )
            ),Category(
                name = "Ăn vặt",
                description = "",
                order = 2,
                isVisible = true,
                isAvailable = true,
                dishes = mutableListOf(
                    Dish("Bánh tráng trộn", 25000, null, null, true, true)
                )
            )

        )
    )
    val categories: StateFlow<List<Category>> = _categories

    // Thêm danh mục mới
    fun addCategory(newCategory: Category) {
        val updatedCategories = _categories.value.toMutableList()
        updatedCategories.add(newCategory)
        updatedCategories.sortBy { it.order } // Sắp xếp theo thứ tự
        _categories.value = updatedCategories
    }
}