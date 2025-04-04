package com.example.third_lab.data.repository

import com.example.third_lab.data.datasource.CategoryLocalDataSource
import com.example.third_lab.domain.entity.Category
import com.example.third_lab.domain.port.CategoryRepository

class CategoryRepositoryImp(
    private val localDataSource: CategoryLocalDataSource
) : CategoryRepository {

    override fun getCategories(): List<Category> {
        return localDataSource.getAllCategories()
    }

    override fun addCategory(name: String): Long {
        return localDataSource.addCategory(name)
    }

    override fun deleteCategory(id: Long): Int {
        return localDataSource.deleteCategory(id)
    }

    override fun updateCategoryName(id: Long, newName: String): Int {
        return localDataSource.updateCategoryName(id, newName)
    }

    override fun getCategoryIdByName(name: String): Long? {
        return localDataSource.getCategoryIdByName(name)
    }
}
