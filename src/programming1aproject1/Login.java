package programming1aproject1;

public class Login {

    // Variables to store the user's registration details
    private String username;
    private String password;
    private String cellphoneNumber;

    // Constructor to store the user's registration details
public Login(String username, String password, String cellphoneNumber) {
    this.username = username;
    this.password = password;
    this.cellphoneNumber = cellphoneNumber;
}

    // Checks if the username contains an underscore and is no more than 5 characters
    public boolean checkUserName() {

        if (username.contains("_") && username.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }

    // Checks if the password meets the required complexity
    public boolean checkPasswordComplexity() {

        if (password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[!@#$%^&*].*")) {
            return true;
        } else {
            return false;
        }
    }

    // Checks if the cellphone number is in the correct South African format
    public boolean checkCellPhoneNumber() {

        return cellphoneNumber.matches("^\\+27\\d{9}$");
    }

    // Registers the user and displays the required registration messages
    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        }

        return "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully captured.";
    }

    // Checks if the login username and password match the registered details
    public boolean loginUser(String enteredUsername, String enteredPassword) {

        if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    // Returns a message showing whether the login was successful
    public String returnLoginStatus(boolean loginSuccessful) {

        if (loginSuccessful) {
            return "Login successful.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}