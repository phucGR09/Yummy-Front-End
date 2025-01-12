package com.example.yummy.models
data class YeuCauTaoNhaHang(
    val name: String,  // Tên nhà hàng
    val address: String,  // Địa chỉ nhà hàng
    val openingHours: GioMoCua,  // Giờ mở cửa
    val username: String  // Tên đăng nhập của chủ sở hữu
)

data class GioMoCua(
    val hour: Int,  // Giờ
    val minute: Int,  // Phút
    val second: Int,  // Giây
    val nano: Int  // Nano giây
)
data class PhanHoiTaoNhaHang(
    val code: Int,  // Mã phản hồi
    val message: String,  // Thông báo phản hồi
    val result: ChiTietNhaHang?  // Thông tin chi tiết về nhà hàng
)

data class ChiTietNhaHang(
    val id: Int,  // ID nhà hàng
    val name: String,  // Tên nhà hàng
    val address: String,  // Địa chỉ nhà hàng
    val openingHours: GioMoCua,  // Giờ mở cửa
    val owner: ThongTinChuSoHuu  // Thông tin về chủ sở hữu
)

data class ThongTinChuSoHuu(
    val id: Int,  // ID của chủ sở hữu
    val taxNumber: String,  // Mã số thuế
    val user: ThongTinNguoiDung  // Thông tin tài khoản người dùng
)

data class ThongTinNguoiDung(
    val id: Int,  // ID người dùng
    val username: String,  // Tên đăng nhập
    val email: String,  // Email
    val fullName: String,  // Họ và tên
    val phoneNumber: String,  // Số điện thoại
    val userType: String  // Loại người dùng
)
