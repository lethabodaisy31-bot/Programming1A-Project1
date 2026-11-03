
package programming1aproject1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    private static int totalMessagesSent = 0;
    
    // Part 3 arrays
    private static String[] sentMessages = new String[0];
    private static String[] disregardedMessages = new String[0];
    private static String[] storedMessages = new String[0];
    private static String[] messageHashes = new String[0];
    private static String[] messageIDs = new String[0];

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;

        long randomID = ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);

        this.messageID = String.valueOf(randomID);
        this.messageHash = createMessageHash();
    }

    // Adds a value to an array.
    private static String[] addToArray(String[] array, String value) {
        String[] newArray = Arrays.copyOf(array, array.length + 1);
        newArray[newArray.length - 1] = value;
        return newArray;
    }

    // Checks that the message ID contains ten digits.
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    // Checks the South African cellphone number format.
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Checks that the message does not exceed 250 characters.
    public String checkMessageLength() {
        if (messageText == null) {
            return "Message exceeds 250 characters by 0; please reduce the size.";
        }

        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess
                    + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    // Creates the message hash.
    public String createMessageHash() {
        String firstTwoDigits = messageID.substring(0, 2);

        if (messageText == null || messageText.trim().isEmpty()) {
            return (firstTwoDigits + ":" + messageNumber + ":").toUpperCase();
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0]
                .replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");

        String lastWord = words[words.length - 1]
                .replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");

        return (firstTwoDigits + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Adds the message ID and hash to their arrays.
    private void addMessageReferences() {
        messageIDs = addToArray(messageIDs, messageID);
        messageHashes = addToArray(messageHashes, messageHash);
    }

    // Processes the user's choice: send, disregard or store.
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                if (!checkMessageID()
                        || !checkRecipientCell().equals(
                                "Cell phone number successfully captured.")
                        || !checkMessageLength().equals("Message ready to send.")) {
                    return "Message could not be sent. Please correct the message details.";
                }

                totalMessagesSent++;
                sentMessages = addToArray(sentMessages, printSingleMessage());
                addMessageReferences();

                return "Message successfully sent.";

            case 2:
                disregardedMessages = addToArray(
                        disregardedMessages, printSingleMessage());

                addMessageReferences();

                return "Press 0 to delete the message.";

            case 3:
                try {
                    storeMessage();
                    addMessageReferences();
                    refreshStoredMessages();

                    return "Message successfully stored.";
                } catch (IOException exception) {
                    return "Message could not be stored: "
                            + exception.getMessage();
                }

            default:
                return "Invalid option. Please select 1, 2 or 3.";
        }
    }

    // Escapes special characters before saving JSON.
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Changes escaped JSON characters back to normal characters.
    private String unescapeJson(String value) {
        return value.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    // Saves the message in messages.json.
    public void storeMessage() throws IOException {
        Path file = Path.of("messages.json");

        String jsonObject = "  {"
                + "\"messageID\":\"" + escapeJson(messageID) + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                + "\"recipient\":\"" + escapeJson(recipient) + "\","
                + "\"message\":\"" + escapeJson(messageText) + "\""
                + "}";

        String existing = Files.exists(file)
                ? Files.readString(file).trim() : "";

        if (existing.isEmpty() || existing.equals("[]")) {
            Files.writeString(file, "[\n" + jsonObject + "\n]",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            if (!existing.endsWith("]")) {
                throw new IOException("The existing JSON file is not valid.");
            }

            String updated = existing.substring(0, existing.length() - 1)
                    + ",\n" + jsonObject + "\n]";

            Files.writeString(file, updated,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    // Reads a value from a JSON message object.
    private String getJsonField(String object, String field) {
        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(field)
                + "\"\\s*:\\s*(?:\"((?:\\\\.|[^\"\\\\])*)\"|(\\d+))");

        Matcher matcher = pattern.matcher(object);

        if (matcher.find()) {
            String value = matcher.group(1) != null
                    ? matcher.group(1) : matcher.group(2);

            return unescapeJson(value);
        }

        return "";
    }

    // Reads messages.json and populates the stored message arrays.
    public void refreshStoredMessages() throws IOException {
        storedMessages = new String[0];

        Path file = Path.of("messages.json");

        if (!Files.exists(file)) {
            return;
        }

        String json = Files.readString(file);
        Matcher matcher = Pattern.compile("\\{[^{}]*\\}").matcher(json);

        while (matcher.find()) {
            String object = matcher.group();

            String id = getJsonField(object, "messageID");
            String hash = getJsonField(object, "messageHash");
            String number = getJsonField(object, "messageNumber");
            String recipientValue = getJsonField(object, "recipient");
            String message = getJsonField(object, "message");

            String details = "Message ID: " + id
                    + "\nMessage Hash: " + hash
                    + "\nMessage Number: " + number
                    + "\nRecipient: " + recipientValue
                    + "\nMessage: " + message;

            storedMessages = addToArray(storedMessages, details);

            if (!id.isEmpty() && !contains(messageIDs, id)) {
                messageIDs = addToArray(messageIDs, id);
            }

            if (!hash.isEmpty() && !contains(messageHashes, hash)) {
                messageHashes = addToArray(messageHashes, hash);
            }
        }
    }

    // Checks whether an array already contains a value.
    private boolean contains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }

        return false;
    }

    // Displays the details of one message.
    public String printSingleMessage() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // Displays all messages sent during the current program run.
    public String printMessages() {
        if (sentMessages.length == 0) {
            return "No messages have been sent.";
        }

        return String.join("\n\n", sentMessages);
    }

    // Displays the stored recipients.
    public String displayStoredSendersAndRecipients() throws IOException {
        refreshStoredMessages();

        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        StringBuilder result = new StringBuilder();

        for (String stored : storedMessages) {
            String recipientValue = stored.replaceAll(
                    "(?s).*?Recipient: ([^\\n]*).*", "$1");

            result.append("Recipient: ")
                    .append(recipientValue)
                    .append("\n\n");
        }

        return result.toString().trim();
    }

    // Finds the longest stored message.
    public String getLongestStoredMessage() throws IOException {
        Path file = Path.of("messages.json");

        if (!Files.exists(file)) {
            return "No stored messages found.";
        }

        String json = Files.readString(file);
        Matcher matcher = Pattern.compile("\\{[^{}]*\\}").matcher(json);

        String longest = "";

        while (matcher.find()) {
            String message = getJsonField(matcher.group(), "message");

            if (message.length() > longest.length()) {
                longest = message;
            }
        }

        if (longest.isEmpty()) {
            return "No stored messages found.";
        }

        return longest;
    }

    // Searches for a stored message using its message ID.
    public String searchStoredMessageByID(String searchID)
            throws IOException {
        Path file = Path.of("messages.json");

        if (!Files.exists(file)) {
            return "No stored messages found.";
        }

        Matcher matcher = Pattern.compile("\\{[^{}]*\\}")
                .matcher(Files.readString(file));

        while (matcher.find()) {
            String object = matcher.group();

            if (getJsonField(object, "messageID").equals(searchID)) {
                return "Recipient: " + getJsonField(object, "recipient")
                        + "\nMessage: " + getJsonField(object, "message");
            }
        }

        return "Message ID not found.";
    }

    // Finds all stored messages for a particular recipient.
    public String searchStoredMessagesByRecipient(String searchRecipient)
            throws IOException {
        Path file = Path.of("messages.json");

        if (!Files.exists(file)) {
            return "No stored messages found.";
        }

        Matcher matcher = Pattern.compile("\\{[^{}]*\\}")
                .matcher(Files.readString(file));

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String object = matcher.group();

            if (getJsonField(object, "recipient").equals(searchRecipient)) {
                result.append("Message ID: ")
                        .append(getJsonField(object, "messageID"))
                        .append("\nRecipient: ")
                        .append(getJsonField(object, "recipient"))
                        .append("\nMessage: ")
                        .append(getJsonField(object, "message"))
                        .append("\n\n");
            }
        }

        return result.length() == 0
                ? "No messages found for this recipient."
                : result.toString().trim();
    }

    // Deletes a stored message using its message hash.
    public String deleteStoredMessageByHash(String searchHash)
            throws IOException {
        Path file = Path.of("messages.json");

        if (!Files.exists(file)) {
            return "No stored messages found.";
        }

        String json = Files.readString(file);
        Matcher matcher = Pattern.compile("\\{[^{}]*\\}").matcher(json);

        String[] remainingObjects = new String[0];
        String deletedMessage = null;

        while (matcher.find()) {
            String object = matcher.group();
            String hash = getJsonField(object, "messageHash");
            String message = getJsonField(object, "message");

            if (hash.equals(searchHash) && deletedMessage == null) {
                deletedMessage = message;
            } else {
                remainingObjects = addToArray(remainingObjects, object);
            }
        }

        if (deletedMessage == null) {
            return "Message hash not found.";
        }

        String updatedJson = "[\n"
                + String.join(",\n", remainingObjects)
                + "\n]";

        Files.writeString(file, updatedJson,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        refreshStoredMessages();

        return "Message: \"" + deletedMessage
                + "\" successfully deleted.";
    }

    // Displays the full report of stored messages.
    public String displayStoredMessagesReport() throws IOException {
        refreshStoredMessages();

        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        return String.join("\n\n", storedMessages);
    }

    // Returns copies of the arrays for testing and use elsewhere.
    public String[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentMessages.length);
    }

    public String[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages, disregardedMessages.length);
    }

    public String[] getStoredMessages() throws IOException {
        refreshStoredMessages();
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageHashes.length);
    }

    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, messageIDs.length);
    }

    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    } 
}