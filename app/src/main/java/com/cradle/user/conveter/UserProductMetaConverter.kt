package com.cradle.user.conveter

import androidx.room.TypeConverter
import com.cradle.model.MetaDataNew


class UserProductMetaConverter {
    @TypeConverter
    fun StringToExample(string: String?): MetaDataNew? {
        return if (string == null) null else MetaDataNew(string,listOf(string))
    }

    @TypeConverter
    fun ExampleToString(example: MetaDataNew?): String? {
        return if (example == null) null else example.images.toString()
    }
}