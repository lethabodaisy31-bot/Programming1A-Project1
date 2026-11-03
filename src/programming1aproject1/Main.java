
package programming1aproject1;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Create Scanner to receive input from the user
        Scanner scanner = new Scanner(System.in);

        // Display the Chat App heading
        System.out.println("=================================");
        System.out.println("       QUICKCHAT REGISTRATION");
        System.out.println("=================================");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your cellphone number (+27 followed by 9 digits): ");
        String cellphoneNumber = scanner.nextLine().trim();

        // Create a Login object using the registration details
        Login login = new Login(username, password, cellphoneNumber);

        String registrationResult = login.registerUser();
        System.out.println(registrationResult);

        // Do not allow login or messaging if registration is invalid.
        if (!registrationResult.startsWith("Username successfully captured.")) {
            System.out.println("Please correct your registration details and run the application again.");
            scanner.close();
            return;
        }

        System.out.println("\n=================================");
        System.out.println("              LOGIN");
        System.out.println("=================================");

        // Get login details
        System.out.print("Enter your username to login: ");
        String loginUsername = scanner.nextLine().trim();

        System.out.print("Enter your password to login: ");
        String loginPassword = scanner.nextLine();

        // Check the login details
        boolean loginSuccessful =
                login.loginUser(loginUsername, loginPassword);

        // Display login status
        System.out.println(login.returnLoginStatus(loginSuccessful));

        if (!loginSuccessful) {
            scanner.close();
            return;
        }

        System.out.println("Welcome, " + firstName + " " + lastName + "!");
        System.out.println("Welcome to QuickChat.");

        System.out.print("How many messages would you like to enter? ");
        int numberOfMessages;

        while (true) {
            try {
                numberOfMessages = Integer.parseInt(scanner.nextLine().trim());

                if (numberOfMessages < 0) {
                    System.out.print("Please enter zero or a positive number: ");
                    continue;
                }

                break;
            } catch (NumberFormatException exception) {
                System.out.print("Please enter a valid whole number: ");
            }
        }

        Message messageManager =
                new Message(0, "+27718693002", "Initial placeholder message");

        boolean running = true;

        while (running) {
            System.out.println("\n========== QUICKCHAT MENU ==========");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.print("Choose an option (1-4): ");

            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1":
                    if (numberOfMessages == 0) {
                        System.out.println("You selected zero messages to enter.");
                        break;
                    }

                    // The for loop allows only the chosen number of messages.
                    for (int i = 0; i < numberOfMessages; i++) {

                        System.out.println("\n--- Message " + (i + 1)
                                + " of " + numberOfMessages + " ---");

                        String recipient;

                        while (true) {
                            System.out.print("Enter recipient cellphone (+27 followed by 9 digits): ");
                            recipient = scanner.nextLine().trim();

                            Message validationMessage =
                                    new Message(i + 1, recipient, "Validation message");

                            if (validationMessage.checkRecipientCell().equals(
                                    "Cell phone number successfully captured.")) {
                                break;
                            }

                            System.out.println(validationMessage.checkRecipientCell());
                        }

                        String messageText;

                        while (true) {
                            System.out.print("Enter your message (maximum 250 characters): ");
                            messageText = scanner.nextLine();

                            if (messageText.length() <= 250) {
                                break;
                            }

                            System.out.println("Please enter a message of less than 250 characters.");
                            System.out.println("Your message exceeds the limit by "
                                    + (messageText.length() - 250) + " characters.");
                        }

                        Message currentMessage =
                                new Message(i + 1, recipient, messageText);

                        System.out.println(currentMessage.checkMessageLength());
                        System.out.println("Message ID generated: "
                                + currentMessage.getMessageID());
                        System.out.println("Message Hash: "
                                + currentMessage.getMessageHash());

                        System.out.println("\n1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message to send later");
                        System.out.print("Choose an option (1-3): ");

                        int messageChoice;

                        while (true) {
                            try {
                                messageChoice = Integer.parseInt(
                                        scanner.nextLine().trim());

                                if (messageChoice >= 1 && messageChoice <= 3) {
                                    break;
                                }
                            } catch (NumberFormatException exception) {
                                // Ask the user to enter a valid option.
                            }

                            System.out.print("Please enter 1, 2 or 3: ");
                        }

                        String result = currentMessage.SentMessage(messageChoice);
                        System.out.println(result);

                        if (messageChoice == 2) {
                            System.out.print("Enter 0 to confirm discarding this message: ");
                            String deleteChoice = scanner.nextLine().trim();

                            if (deleteChoice.equals("0")) {
                                System.out.println("Message disregarded.");
                            } else {
                                System.out.println("No deletion confirmed.");
                            }
                        }

                        if (messageChoice == 1
                                && result.equals("Message successfully sent.")) {
                            System.out.println(currentMessage.printSingleMessage());
                        }
                    }

                    System.out.println("\nTotal messages successfully sent: "
                            + messageManager.returnTotalMessagess());
                    break;

                case "2":
                    System.out.println(messageManager.printMessages());
                    break;

                case "3":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;

                case "4":
                    storedMessagesMenu(scanner, messageManager);
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }

        scanner.close();
    }

    // Displays the Stored Messages menu and processes the selected option.
    private static void storedMessagesMenu(
            Scanner scanner, Message messageManager) {

        boolean inStoredMenu = true;

        while (inStoredMenu) {
            System.out.println("\n======= STORED MESSAGES =======");
            System.out.println("1. Display stored message recipients");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search for a message using its ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete a message using its hash");
            System.out.println("6. Display the full stored messages report");
            System.out.println("7. Return to the main menu");
            System.out.print("Choose an option (1-7): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        System.out.println(
                                messageManager.displayStoredSendersAndRecipients());
                        break;

                    case "2":
                        System.out.println("Longest stored message:");
                        System.out.println(
                                messageManager.getLongestStoredMessage());
                        break;

                    case "3":
                        System.out.print("Enter the message ID: ");
                        String searchID = scanner.nextLine().trim();

                        System.out.println(
                                messageManager.searchStoredMessageByID(searchID));
                        break;

                    case "4":
                        System.out.print("Enter the recipient cellphone number: ");
                        String searchRecipient = scanner.nextLine().trim();

                        System.out.println(
                                messageManager.searchStoredMessagesByRecipient(
                                        searchRecipient));
                        break;

                    case "5":
                        System.out.print("Enter the message hash to delete: ");
                        String searchHash = scanner.nextLine().trim();

                        System.out.println(
                                messageManager.deleteStoredMessageByHash(searchHash));
                        break;

                    case "6":
                        System.out.println("======= STORED MESSAGE REPORT =======");
                        System.out.println(
                                messageManager.displayStoredMessagesReport());
                        break;

                    case "7":
                        inStoredMenu = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please choose 1-7.");
                }
            } catch (IOException exception) {
                System.out.println("An error occurred while accessing stored messages: "
                        + exception.getMessage());
            }
        }
    }
}
