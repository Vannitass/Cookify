package com.example.pageforregister

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class Serializable_json (
    val loggin: String,
    val email: String,
    val password: String
)

fun stringToJsonObject(jsonString: String): Serializable_json {
    return Json.decodeFromString(jsonString)
}

// Функция для преобразования объекта в JSON-формат (строка)
fun jsonObjectToString(person: Serializable_json): String {
    return Json.encodeToString(person)
}
