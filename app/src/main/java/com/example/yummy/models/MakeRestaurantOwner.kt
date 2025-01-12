package com.example.yummy.models

data class YeuCauHoanThanhChuNhaHang(
    val taxNumber: String,
    val username: String
)

data class KetQuaHoanThanhChuNhaHang(
    val code: Int,
    val message: String,
    val result: ChuNhaHangResult
)

data class ChuNhaHangResult(
    val taxNumber: String,
    val user: NguoiDung_NhaHang
)

data class NguoiDung_NhaHang(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val userType: String
)


