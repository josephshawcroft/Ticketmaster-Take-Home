package com.shawcroftstudios.ticketmastertakehome.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.shawcroftstudios.ticketmastertakehome.domain.model.Images

class ImagesTypeConverter {

    private val gson = Gson()
    @TypeConverter
    fun imagesToString(images: Images?): String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun stringToImages(value: String?): Images? {
        return gson.fromJson(value, Images::class.java)
    }
}
