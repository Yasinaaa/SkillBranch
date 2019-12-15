package ru.skillbranch.gameofthrones.data.local.converters

import androidx.room.TypeConverter
import ru.skillbranch.gameofthrones.data.local.entities.HouseType

/*
 * Created by yasina on 2019-12-15
*/
class Converter {

    @TypeConverter
    fun fromString(value: String) : List<String> = value.split(";")

    @TypeConverter
    fun fromArrayList(list: List<String>) = list.joinToString(";")

    @TypeConverter
    fun fromTitle(value: String) : HouseType = HouseType.fromString(";")

    @TypeConverter
    fun fromEnum(anEnum: HouseType) : String = anEnum.title
}