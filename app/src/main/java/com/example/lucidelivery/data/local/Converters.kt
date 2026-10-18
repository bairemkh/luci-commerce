package com.example.lucidelivery.data.local


import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json
    private val serializer = ListSerializer(String.serializer())

    @TypeConverter
    fun fromList(list: List<String>): String = json.encodeToString(serializer, list)

    @TypeConverter
    fun toList(value: String): List<String> =
        runCatching { json.decodeFromString(serializer, value) }.getOrDefault(emptyList())
}