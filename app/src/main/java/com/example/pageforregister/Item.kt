package com.example.pageforregister

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val image: String,             // URI изображения
    val title: String,             // Название блюда
    val description: String,       // Описание блюда
    val cookingTime: String?,      // Время приготовления (опционально)
    val servings: Int?,            // Порции (опционально)
    val ingredients: String,       // Ингредиенты
    val steps: List<Step>,         // Пошаговые инструкции
    val author: String             // Автор рецепта
) : Parcelable

@Parcelize
data class Step(
    val stepNumber: Int,           // Номер шага
    val instruction: String,       // Текст инструкции
    val imageUri: String?          // URI изображения шага (опционально)
) : Parcelable
