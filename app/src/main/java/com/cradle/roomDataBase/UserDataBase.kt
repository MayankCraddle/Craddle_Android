package com.cradle.roomDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cradle.model.ProductListItem1
import com.cradle.user.conveter.UserProductMetaConverter

/*
@TypeConverters(UserProductMetaConverter::class)
@Database(entities = [User::class,ProductListItem1::class], version = 1,exportSchema = false)
*/
abstract class UserDataBase :RoomDatabase() {
    abstract fun userDao() : UserDao
    companion object{

        //THIS METHOD ARE DEFINE WHEN UPDATE VERSION THEN NO LOSS DATABASE
        val migration1_2=object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
              //  database.execSQL("ALTER TABLE contact ADD COLUMN column name INTEGER NOT NULL DEFAULT(1)")
            }

        }
        @Volatile
        private var INSTANCE:UserDataBase?=null
        fun getDataBase(context: Context):UserDataBase{
            if (INSTANCE==null){
                synchronized(this){
                    INSTANCE=
                        Room.databaseBuilder(context.applicationContext,UserDataBase::class.java,"contactDb").fallbackToDestructiveMigration().build()
                }

            }
            return INSTANCE!!
        }
    }

}