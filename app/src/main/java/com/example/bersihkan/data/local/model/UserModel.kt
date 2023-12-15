package com.example.bersihkan.data.local.model

import com.example.bersihkan.utils.UserRole

data class UserModel(
    val id: String,
    val name: String,
    val token: String,
    val role: UserRole,
    val username: String,
    val email: String,
    val password: String,
    val isLogin: Boolean = false
)