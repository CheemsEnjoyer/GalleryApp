package com.example.third_lab.domain.entity

data class Photo(
    val id: Long,
    val categoryId: Long?,
    val photoPath: String
)