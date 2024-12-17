package com.example.pageforregister.RegAndAuthoriz

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pageforregister.R
import com.example.pageforregister.ToastMatcher // Убедитесь, что класс ToastMatcher существует
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тесты для AuthActivity - демонстрируют UI и функциональные проверки.
 */
@RunWith(AndroidJUnit4::class)
class AuthActivityTest {

    // Правило запуска Activity перед выполнением теста
    @get:Rule
    val activityRule = ActivityScenarioRule(AuthActivity::class.java)

    @Test
    fun checkInitialUIState() {
        // Проверка, что все ключевые элементы отображаются на экране
        onView(withId(R.id.user_login_auth)).check(matches(isDisplayed()))  // Поле ввода логина
        onView(withId(R.id.user_pass_auth)).check(matches(isDisplayed()))   // Поле ввода пароля
        onView(withId(R.id.button_auth)).check(matches(isDisplayed())) // Кнопка авторизации
        onView(withText("Не все поля зополнены")).check(matches(isDisplayed()))

    }

    @Test
    fun testValidFormSubmission() {
        // Ввод корректных данных в поля
        onView(withId(R.id.user_login_auth)).perform(typeText("globus"), closeSoftKeyboard())
        onView(withId(R.id.user_pass_auth)).perform(typeText("1234"), closeSoftKeyboard())

        // Имитация нажатия на кнопку авторизации
        onView(withId(R.id.button_auth)).perform(click())

        // Пример: проверка появления Toast уведомления
        // Убедитесь, что ToastMatcher настроен для работы с Toast сообщениями
        onView(withText("Пользователь globus авторизован")).check(matches(isDisplayed()))
    }

    @Test
    fun testInvalidFormSubmission() {
        // Ввод некорректных данных
        onView(withId(R.id.user_login_auth)).perform(typeText(""), closeSoftKeyboard()) // Пустой логин
        onView(withId(R.id.user_pass_auth)).perform(typeText(""), closeSoftKeyboard()) // Пустой пароль

        // Нажатие на кнопку авторизации
        onView(withId(R.id.button_auth)).perform(click())

        // Проверка наличия сообщения об ошибке
        onView(withText("Не все поля зополнены")).check(matches(isDisplayed()))
    }
}
