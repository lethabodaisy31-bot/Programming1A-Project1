package programming1aproject1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Create a Scanner object to read input from the keyboard
        Scanner scanner = new Scanner(System.in);

        // Display the application heading
        System.out.println("=================================");
        System.out.println("       CHAT APP REGISTRATION     ");
        System.out.println("=================================");

        // Ask the user for registration details
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your cellphone number: ");
        String cellphoneNumber = scanner.nextLine();

        // Create a Login object using the registration details
        Login login = new Login(username, password, cellphoneNumber);

        // Register the user and display the registration result
        System.out.println(login.registerUser());

        // Display the login section
        System.out.println("\n=================================");
        System.out.println("             LOGIN               ");
        System.out.println("=================================");

        // Ask the user for login details
        System.out.print("Enter your username to login: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password to login: ");
        String loginPassword = scanner.nextLine();

        // Check whether the login details are correct
        boolean loginSuccessful = login.loginUser(loginUsername, loginPassword);

        // Display the login result
        System.out.println(login.returnLoginStatus(loginSuccessful));

        // Close the Scanner
        scanner.close();
    }
}