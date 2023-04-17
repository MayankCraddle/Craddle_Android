package com.cradle.roomDataBase

import androidx.lifecycle.LiveData
import androidx.room.*

import com.cradle.model.ProductListItem1

@Dao
interface UserDao {
  /*  @Insert
    suspend fun inserData(user: ProductListItem1)
    @Update
    suspend fun updateData(user:ProductListItem1)
    @Delete
    suspend fun deleteData(user:ProductListItem1)
    @Query("SELECT * FROM productlist")
    fun getAllData():LiveData<List<ProductListItem1>>

    *//**
     * Updating only price
     * By order id
     *//*
    @Query("UPDATE contact SET name=:name WHERE id = :productId")
    fun update(productId: Long,name: String?)
*/}