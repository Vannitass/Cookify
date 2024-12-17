import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `check fields validation when empty`() {
        val login = ""
        val email = ""
        val pass = ""

        val isValid = login.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()

        assertFalse("Fields validation should fail for empty fields", isValid)
    }

    @Test
    fun `check fields validation for valid inputs`() {
        val login = "testUser"
        val email = "test@example.com"
        val pass = "password"

        val isValid = login.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()

        assertTrue("Fields validation should pass for valid inputs", isValid)
    }
}
