import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

// Data class cho Order
data class Order(
    val orderId: Int, // Primary Key
    val restaurantId: Int, // Foreign Key (Restaurant)
    val driverId: Int, // Foreign Key (Driver)
    val customerId: Int, // Foreign Key (Customer)
    val orderTime: LocalDateTime, // Thời gian đặt hàng
    val totalPrice: Double, // Tổng tiền đơn hàng
    var orderStatus: OrderStatus // Trạng thái đơn hàng
)

// Enum class cho trạng thái đơn hàng
enum class OrderStatus {
    waiting_confirmation, // Đang chờ xác nhận
    confirmed, // Đã xác nhận
    waiting_delivery, // Đang chờ lấy hàng
    delivering, // Đang giao
    delivered, // Đã giao
    cancelled // Đã hủy
}

class OrderModel {

    // Danh sách toàn bộ đơn hàng
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    // Danh sách đơn hiện tại
    private val _currentOrders = MutableStateFlow<List<Order>>(emptyList())
    val currentOrders: StateFlow<List<Order>> = _currentOrders

    // Danh sách đơn quá khứ
    private val _pastOrders = MutableStateFlow<List<Order>>(emptyList())
    val pastOrders: StateFlow<List<Order>> = _pastOrders

    // Hàm giả lập đọc danh sách đơn hàng từ server
    fun getOrdersList(restaurantId: Int) {
        // Đây là giả lập việc đọc dữ liệu từ server
        val serverData = listOf(
            Order(1, restaurantId, 101, 201, LocalDateTime.now().minusDays(1), 50000.0, OrderStatus.delivered),
            Order(2, restaurantId, 102, 202, LocalDateTime.now(), 30000.0, OrderStatus.waiting_confirmation),
            Order(3, restaurantId, 103, 203, LocalDateTime.now(), 40000.0, OrderStatus.delivering),
            Order(4, restaurantId, 104, 204, LocalDateTime.now().minusDays(2), 20000.0, OrderStatus.cancelled),
            Order(5, restaurantId, 105, 205, LocalDateTime.now().minusHours(1), 45000.0, OrderStatus.confirmed)
        )

        _orders.value = serverData
        updateFilteredOrders()
    }

    // Hàm cập nhật trạng thái đơn hàng
    fun setOrderStatus(orderId: Int, newStatus: OrderStatus) {
        // Tìm đơn hàng dựa trên orderId
        val order = _orders.value.find { it.orderId == orderId }
        if (order != null) {
            // Cập nhật trên server trước
            updateOrderOnServer(orderId, newStatus) { success ->
                if (success) {
                    // Nếu thành công, cập nhật trong danh sách cục bộ
                    _orders.update { orders ->
                        orders.map {
                            if (it.orderId == orderId) it.copy(orderStatus = newStatus)
                            else it
                        }
                    }
                    updateFilteredOrders() // Cập nhật danh sách đơn hiện tại và quá khứ
                } else {
                    println("Lỗi: Không thể cập nhật trạng thái trên server.")
                }
            }
        } else {
            println("Lỗi: Không tìm thấy đơn hàng với orderId = $orderId")
        }
    }

    // Hàm cập nhật trạng thái đơn hàng trên server
    private fun updateOrderOnServer(orderId: Int, newStatus: OrderStatus, onResult: (Boolean) -> Unit) {
        // Giả lập gọi API để cập nhật trạng thái
        runBlocking {
            try {
                println("Đang gửi yêu cầu cập nhật trạng thái đơn hàng lên server...")
                // Giả lập thời gian thực hiện API call
                delay(1000)
                println("Cập nhật thành công: Đơn hàng $orderId -> Trạng thái $newStatus")
                onResult(true) // Trả về thành công
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Trả về thất bại
            }
        }
    }

    // Hàm cập nhật danh sách đơn hiện tại và quá khứ
    private fun updateFilteredOrders() {
        val allOrders = _orders.value
        _currentOrders.value = allOrders.filter {
            it.orderStatus !in listOf(OrderStatus.delivered, OrderStatus.cancelled)
        }.sortedByDescending { it.orderTime }

        _pastOrders.value = allOrders.filter {
            it.orderStatus in listOf(OrderStatus.delivered, OrderStatus.cancelled)
        }.sortedByDescending { it.orderTime }
    }
}
