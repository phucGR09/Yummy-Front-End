package com.example.yummy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.yummy.R

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val imageResId: Int
)

data class StoreInfo(
    val name: String,
    val address: String,
    val logoResId: Int
)

class StoreRepository {
    fun getStoreInfo(): StoreInfo {
        return StoreInfo(
            name = "KeyBox Kafe",
            address = "123 Lương Định Của, Bình Dương",
            logoResId = R.drawable.keybox_kafe
        )
    }

    fun getProducts(): List<Product> {
        return listOf(
            Product(1, "Matcha Latte", 55000, R.drawable.matcha_latte),
            Product(2, "Latte", 45000, R.drawable.latte)
        )
    }
}

class StoreViewModel(private val repository: StoreRepository = StoreRepository()) : ViewModel() {
    // Thông tin cửa hàng
    val storeInfo: StoreInfo = repository.getStoreInfo()

    // Quản lý danh sách sản phẩm
    private val _products = MutableStateFlow(repository.getProducts())
    val products: StateFlow<List<Product>> = _products

    // Trạng thái danh sách trống
    val isProductListEmpty: StateFlow<Boolean> = _products.map { it.isEmpty() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Cập nhật giá sản phẩm
    fun updateProductPrice(productId: Int, newPrice: Int) {
        val isUpdated = _products.updateAndGet { currentProducts ->
            currentProducts.map { product ->
                if (product.id == productId) product.copy(price = newPrice) else product
            }
        }.any { it.id == productId }

        if (!isUpdated) {
            println("Product with ID $productId not found.")
        }
    }

    // Xóa sản phẩm theo ID
    fun removeProduct(productId: Int) {
        _products.update { currentProducts ->
            currentProducts.filter { it.id != productId }
        }
    }

    // Đặt lại danh sách sản phẩm
    fun resetProducts(newProducts: List<Product>) {
        _products.value = newProducts
    }
}
