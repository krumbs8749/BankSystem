import bank.*;
import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
public class PrivateBankTest {
    PrivateBank bank;
    PrivateBank bankCopy;
     Payment p1 = new Payment("24.03.1999", 100.0, "p1 - deposit", 0.2, 0.2);
     Payment p2 = new Payment("25.03.1999", -20.0, "p2 - withdrawal", 0.5, 0.5);
     Payment p3 = new Payment(p2);
     IncomingTransfer t1 = new IncomingTransfer("12.12.2000", 100.0, "t1 -incoming", "First Account", "Second Account");
     OutgoingTransfer t2 = new OutgoingTransfer("12.12.2000", 50.0, "t2 - Outgoing", "Second Account", "First Account");
     List<Transaction> list = new ArrayList<>();

     @BeforeEach
    public void init_private_bank (){
        try {
            bank = new PrivateBank("Bank", 0.315, 0.215);
            bankCopy = new PrivateBank(bank);
            bank.createAccount("First Account");
            bank.addTransaction("First Account", p1);
            bank.addTransaction("First Account", t1);

            bank.createAccount("Second Account");
            bank.addTransaction("Second Account", t2);
            bank.addTransaction("Second Account", p2);
        }catch (Exception e){
            System.out.println( e.getClass() + ": " + e.getMessage());
        }
    }
    @Test
    public void test_constructor () {
         assertNotNull(bank);
         assertFalse(bank.getName().isEmpty());
         assertTrue(bank.getIncomingInterest() > 0 && bank.getOutgoingInterest() < 1);
         assertTrue(bank.getOutgoingInterest() >0 && bank.getOutgoingInterest() < 1);
    }
    @Test
    public void test_copy_constructor () {
         assertEquals(bank, bankCopy);
    }
    @Test
    public void test_to_String(){
         String output = "name: " + bank.getName() + "\n" +
                "Incoming Interest: " + bank.getIncomingInterest() + "\n" +
                "Outgoing Interest: " + bank.getOutgoingInterest() + "\n";
         assertEquals(bank.toString(), output);

    }

    @Test
    public void test_create_account() {
         assertTrue(bank.getAccountBalance("First Account") >= 0.0 || bank.getAccountBalance("First Account") <0 );
    }
    @Test
    public void test_add_and_contains_transaction(){
         p1.setOutgoingInterest(bank.getOutgoingInterest());
         p1.setIncomingInterest(bank.getIncomingInterest());
         assertTrue(bank.containsTransaction("First Account", p1));
         assertTrue(bank.containsTransaction("First Account", t1));
    }

    @Test
    public void test_remove_transaction(){
         try{
         bank.removeTransaction("Second Account", t2);
         bank.removeTransaction("Second Account", p2);
         assertFalse(bank.containsTransaction("Second Account", p2));
         assertFalse(bank.containsTransaction("Second Account", t2));
         bank.addTransaction("Second Account", t2);
         bank.addTransaction("Second Account", p2);
         }catch (Exception e){
                System.out.println( e.getClass() + ": " + e.getMessage());
         }

    }

    @Test
    public void test_account_synchronized_with_serialized_data(){
         Gson gson = new Gson();
    try {
        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("src/main/data/accounts"));
        for (Path path : stream) {
            if (!Files.isDirectory(path)) {
                String filePath = path.getFileName().toString();
                String accountName = filePath.split("\\.(?=[^\\.]+$)")[0];
                String absPath = "src/main/data/accounts/" + filePath;
                JsonArray jsonArr = gson.fromJson(new FileReader(absPath), JsonArray.class);

                Gson gsonBuilder = new GsonBuilder()
                        .registerTypeAdapter(Transaction.class, new CustomDeserializer())
                        .setPrettyPrinting()
                        .create();


                jsonArr.forEach(jsonElement -> {
                    Transaction res = gsonBuilder.fromJson(jsonElement, Transaction.class);
                    assertTrue(bank.containsTransaction(accountName, res));
                });
            }
        }
        stream.close();
    }catch (Exception e){
        System.out.println(e.getClass() + ": " + e.getMessage());
    }
    }

    @Test
    public void test_exception_account_already_exist () {
          AccountAlreadyExistsException thrown = Assertions.assertThrows(AccountAlreadyExistsException.class, () -> {
               bank.createAccount("First Account");
            });

          Assertions.assertEquals(AccountAlreadyExistsException.class, thrown.getClass());
    }
    @Test
    public void test_exception_account_does_not_exist() {
          AccountDoesNotExistException thrown = Assertions.assertThrows(AccountDoesNotExistException.class, () -> {
               bank.addTransaction("unknown", p1);
            });

          Assertions.assertEquals(AccountDoesNotExistException.class, thrown.getClass());
    }
    @Test
    public void test_exception_transaction_already_exist () {
          TransactionAlreadyExistException thrown = Assertions.assertThrows(TransactionAlreadyExistException.class, () -> {
               bank.addTransaction("First Account", p1);
            });

          Assertions.assertEquals(TransactionAlreadyExistException.class, thrown.getClass());
    }
    @Test
    public void test_exception_transaction_does_not_exist () {
          TransactionDoesNotExistException thrown = Assertions.assertThrows(TransactionDoesNotExistException.class, () -> {
               bank.removeTransaction("First Account", t2);
            });

          Assertions.assertEquals(TransactionDoesNotExistException.class, thrown.getClass());
    }


}
