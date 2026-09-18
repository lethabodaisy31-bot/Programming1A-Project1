package programming1aproject1;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LoginTest {

    @Test
    public void testCheckUserName() {
        Login login = new Login("kyl_1", "Password1!", "+27838968976");

        assertEquals(true, login.checkUserName());
    }

    @Test
    public void testCheckUserNameIncorrect() {
        Login login = new Login("kyle", "Password1!", "+27838968976");

        assertEquals(false, login.checkUserName());
    }

    @Test
    public void testCheckPasswordComplexity() {
        Login login = new Login("kyl_1", "Password1!", "+27838968976");

        assertEquals(true, login.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexityIncorrect() {
        Login login = new Login("kyl_1", "password", "+27838968976");

        assertEquals(false, login.checkPasswordComplexity());
    }

    @Test
    public void testCheckCellPhoneNumber() {
        Login login = new Login("kyl_1", "Password1!", "+27838968976");

        assertEquals(true, login.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumberIncorrect() {
        Login login = new Login("kyl_1", "Password1!", "0838968976");

        assertEquals(false, login.checkCellPhoneNumber());
    }

    @Test
    public void testLoginUser() {
        Login login = new Login("kyl_1", "Password1!", "+27838968976");

        assertEquals(true, login.loginUser("kyl_1", "Password1!"));
    }

    @Test
    public void testLoginUserIncorrect() {
        Login login = new Login("kyl_1", "Password1!", "+27838968976");

        assertEquals(false, login.loginUser("wrong", "wrong"));
    }

    @Test
    public void testReturnLoginStatus() {
        Login login = new Login("kyl_1", "Password1!", "+27838968976");

        assertEquals("Login successful.", login.returnLoginStatus(true));
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }
}
