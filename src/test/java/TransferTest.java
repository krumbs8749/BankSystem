import bank.Transfer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
public class TransferTest {
    Transfer transferShort;
    Transfer transferLong;

    Transfer transferCopy;

    @BeforeEach
    public void init_payment_constructor() {
        transferShort = new Transfer("18.12.2022",  5000, "Test payment class constructor (short)");
        transferLong = new Transfer("18.12.2022",  10000, "Test payment class constructor (long)", "Sender_1", "Recipient_!");
        transferCopy = new Transfer(transferLong);
        transferShort.setSender("Sender_2");
        transferShort.setRecipient("Recipient_2");
    }

    @Test
    public void test_payment_constructor (){
        assertNotNull(transferShort);
        assertNotNull(transferLong);
        assertNotEquals(transferLong, transferShort);
        assertInstanceOf(Transfer.class, transferShort);
        assertInstanceOf(Transfer.class, transferLong);
    }
    @Test
    public void test_payment_copy_and_equals (){
        assertNotNull(transferCopy);
        assertEquals(transferLong, transferCopy);
    }
    @Test
    public void test_calculate(){
        assumeTrue(transferShort.getAmount() > 0);
        assertEquals(transferShort.calculate(), transferShort.getAmount());
        assumeTrue(transferLong.getAmount() > 0);
        assertEquals(transferLong.calculate(), transferLong.getAmount());
    }
    @Test
    public void test_to_String () {
        String output = "date: " + transferLong.getDate() + "\n" +
                "amount: " + transferLong.calculate() + "\n" +
                "description: " + transferLong.getDescription() + "\n" + "sender: " + transferLong.getSender() + "\n" +
                "recipient: " + transferLong.getRecipient() + "\n";
        assertEquals(transferLong.toString(), output);
    }
}
