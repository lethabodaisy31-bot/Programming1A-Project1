package programming1aproject1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Create Scanner to receive input from the user
        Scanner scanner = new Scanner(System.in);

        // Display the Chat App heading
        System.out.println("=================================");
        System.out.println("          CHAT APP");
        System.out.println("=================================");

        // Get registration details from the user
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your cellphone number: ");
        String cellphoneNumber = scanner.nextLine();

        // Create a Login object using the registration details
        Login login = new Login(username, password, cellphoneNumber);

        // Register the user
        System.out.println("\n" + login.registerUser());

        // Display login section
        System.out.println("\n=================================");
        System.out.println("             LOGIN");
        System.out.println("=================================");

        // Get login details
        System.out.print("Enter your username to login: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password to login: ");
        String loginPassword = scanner.nextLine();

        // Check the login details
        boolean loginSuccessful =
                login.loginUser(loginUsername, loginPassword);

        // Display login status
        System.out.println(login.returnLoginStatus(loginSuccessful));

        // Close Scanner
        scanner.close();
    }
}