package programming1aproject1;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MessagePart3Test {

    // Test 1: Check that sent messages are added to the sent messages list.
    @Test
    public void testSentMessagesArray() {
        Message message = new Message(
                1,
                "+27834557896",
                "Did you get the cake?"
        );

        message.SentMessage(1);

        boolean found = false;

for (String sentMessage : message.getSentMessages()) {
    if (sentMessage.equals(message.printSingleMessage())) {
        found = true;
        break;
    }
}

assertEquals(true, found);                
        
    }

    // Test 2: Check that the longest stored message is returned.
    @Test
    public void testLongestStoredMessage() throws Exception {
        Message message = new Message(
                2,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time."
        );

        message.storeMessage();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                message.getLongestStoredMessage()
        );
    }

    // Test 3: Search for a stored message using its message ID.
    @Test
    public void testSearchStoredMessageByID() throws Exception {
        Message message = new Message(
                4,
                "+27838884567",
                "It is dinner time !"
        );

        message.storeMessage();

        assertEquals(
                "Recipient: +27838884567\nMessage: It is dinner time !",
                message.searchStoredMessageByID(message.getMessageID())
        );
    }

    // Test 4: Search for all stored messages using the recipient's number.
    @Test
    public void testSearchStoredMessagesByRecipient() throws Exception {
        Message message1 = new Message(
                2,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time."
        );

        Message message2 = new Message(
                5,
                "+27838884567",
                "Ok, I am leaving without you."
        );

        message1.storeMessage();
        message2.storeMessage();

        String result = message1.searchStoredMessagesByRecipient(
                "+27838884567"
        );

        assertEquals(true, result.contains(
                "Where are you? You are late!"
        ));

        assertEquals(true, result.contains(
                "Ok, I am leaving without you."
        ));
    }

    // Test 5: Delete a stored message using its message hash.
    @Test
    public void testDeleteStoredMessageByHash() throws Exception {
        Message message = new Message(
                6,
                "+27834557896",
                "This message must be deleted."
        );

        message.storeMessage();

        assertEquals(
                "Message: \"This message must be deleted.\" successfully deleted.",
                message.deleteStoredMessageByHash(message.getMessageHash())
        );
    }

    // Test 6: Check that the stored messages report displays message details.
    @Test
    public void testDisplayStoredMessagesReport() throws Exception {
        Message message = new Message(
                7,
                "+27834557896",
                "This is my report test."
        );

        message.storeMessage();

        String report = message.displayStoredMessagesReport();

        assertEquals(true, report.contains("Message Hash:"));
        assertEquals(true, report.contains("Recipient: +27834557896"));
        assertEquals(true, report.contains("This is my report test."));
    }
}