package com.example.pageforregister.MainPage

data class RecipeResponse(
    val recipes: List<Recipe>, // Список рецептов
    val status: String         // Статус ответа
)