package com.example.third_lab

data class Category(val id: Long, val name: String) {
    override fun toString(): String {
        return name
    }
}
