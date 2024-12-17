import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pageforregister.R
import com.example.pageforregister.RegAndAuthoriz.RegActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegActivityUITest {

    @get:Rule
        val activityRule = ActivityScenarioRule(RegActivity::class.java)

    @Test
    fun checkInitialUIState() {
        onView(withId(R.id.user_login)).check(matches(isDisplayed()))
        onView(withId(R.id.user_email)).check(matches(isDisplayed()))
        onView(withId(R.id.user_pass)).check(matches(isDisplayed()))
        onView(withId(R.id.button_reg)).check(matches(isDisplayed()))
    }

    @Test
    fun testValidFormSubmission() {
        // Ввод данных
        onView(withId(R.id.user_login)).perform(typeText("testUser"))
        onView(withId(R.id.user_email)).perform(typeText("test@example.com"))
        onView(withId(R.id.user_pass)).perform(typeText("password"), closeSoftKeyboard())

        // Нажать на кнопку регистрации
        onView(withId(R.id.button_reg)).perform(click())

        // Проверка (вам может понадобиться Mock-сервер для эмуляции ответа)
        // Например: проверяем, если Toast сообщение на экране
        onView(withText("Пользователь testUser добавлен")).check(matches(isDisplayed()))
    }
}
