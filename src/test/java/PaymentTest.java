import bank.Payment;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
public class PaymentTest {
    Payment paymentShort;
    Payment paymentLong;

    Payment paymentCopy;

    @BeforeEach
    public void init_payment_constructor() {
        paymentShort = new Payment("18.12.2022",  -5000, "Test payment class constructor (short)");
        paymentLong = new Payment("18.12.2022",  10000, "Test payment class constructor (long)", 0.15, 0.17);
        paymentCopy = new Payment(paymentLong);
        paymentShort.setIncomingInterest(0.13);
        paymentShort.setOutgoingInterest(0.43);
    }

    @Test
    public void test_payment_constructor (){
        assertNotNull(paymentShort);
        assertNotNull(paymentLong);
        assertNotEquals(paymentLong, paymentShort);
        assertInstanceOf(Payment.class, paymentShort);
        assertInstanceOf(Payment.class, paymentLong);
    }
    @Test
    public void test_payment_copy_and_equals (){
        assertNotNull(paymentCopy);
        assertEquals(paymentLong, paymentCopy);
    }
    @Test
    public void test_calculate(){
        assumeTrue(paymentShort.getAmount() < 0);
        assertEquals(paymentShort.calculate(), paymentShort.getAmount() * (1 + paymentShort.getOutgoingInterest()));
        assumeTrue(paymentLong.getAmount() > 0);
        assertEquals(paymentLong.calculate(), paymentLong.getAmount() * (1 - paymentLong.getIncomingInterest()));
    }
    @Test
    public void test_to_String () {
        String output = "date: " + paymentLong.getDate() + "\n" +
                "amount: " + paymentLong.calculate() + "\n" +
                "description: " + paymentLong.getDescription() + "\n" + "incomingInterest: " + paymentLong.getIncomingInterest() + "\n" +
                "outgoingInterest: " + paymentLong.getOutgoingInterest() + "\n";
        assertEquals(paymentLong.toString(), output);
    }

}
