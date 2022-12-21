/*
 * @author Ahmad Ikram Hafidz Bin Ahmad Darwis
 * @matrikelnummer 3526017
 */
import bank.*;
//import bank.exceptions.AccountAlreadyExistsException;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.*;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;

public class Main {
    public static void main(String[] args) {

        PrivateBank bankTest = null;
        try{
            bankTest = new PrivateBank("test", 0.5, 0.5);
            

        }catch (Exception e){
            System.out.println(e.getClass() + ": " + e.getMessage());
        }
        
        try{
            System.out.println(bankTest.getAllAccounts());
            System.out.println("Testing by Type\n");
            System.out.println(bankTest.getTransactionsByType("Account Eva", true));
            System.out.println(bankTest.getTransactionsByType("Account Eva", false));
            System.out.println("\n\nTesting by Sort\n");
            System.out.println(bankTest.getTransactionsSorted("Account Eva", true));
            System.out.println(bankTest.getTransactionsSorted("Account Eva", false));
        }catch (Exception e){
            System.out.println(e.getClass() + ": " + e.getMessage());
        }


    }
}