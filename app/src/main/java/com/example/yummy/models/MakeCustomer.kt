package com.example.yummy.models

data class YeuCauHoanThanhKhachHang(
    val address: String,
    val username: String
)

data class KetQuaHoanThanhKhachHang(
    val code: Int,
    val message: String,
    val result: KhachHangResult
)

data class KhachHangResult(
    val user: NguoiDung_KhachHang,
    val address: String
)

data class NguoiDung_KhachHang(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val userType: String
)
