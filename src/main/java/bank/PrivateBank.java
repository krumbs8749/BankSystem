package bank;

import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PrivateBank implements Bank{
    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    private  String directoryName = "src/main/data/accounts";
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    public PrivateBank(String name, double incomingInterest, double outgoingInterest) throws IOException{

        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        readAccounts();
    }

    public String getDirectoryName() {
        return directoryName;
    }
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }



    public PrivateBank(PrivateBank bank) throws IOException{
        setName(bank.getName());
        setIncomingInterest(bank.getIncomingInterest());
        setOutgoingInterest(bank.getOutgoingInterest());
        readAccounts();
    }

    public String getName() {
        return name;
    }

    public double getIncomingInterest() {
        return incomingInterest;
    }

    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIncomingInterest(double incomingInterest) {
        this.incomingInterest = incomingInterest;
    }

    public void setOutgoingInterest(double outgoingInterest) {
        this.outgoingInterest = outgoingInterest;
    }
     /*
     * Überschreiben toString() in 'class Object' um die Information des PrivateBanks auszugeben
     * @return string
     */
    @Override
    public String toString(){
        return "name: " + getName() + "\n" +
                "Incoming Interest: " + getIncomingInterest() + "\n" +
                "Outgoing Interest: " + getOutgoingInterest() + "\n";
    }
    /*
     * Überschreiben equals(obj: Object) in 'class Object' um zu uberpruefen, ob die übergebene PrivateBank object die gleichen Werte von Attributen
     *  besitzen wie eigene Transaktion objekt
     * @param obj
     * @return boolean: true
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj.getClass() == this.getClass()) || obj == null)return false;
        return getName().equals(((PrivateBank) obj).getName())
                && getIncomingInterest() == ((PrivateBank) obj).getIncomingInterest()
                && getOutgoingInterest() == ((PrivateBank) obj).getOutgoingInterest();
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    public void createAccount(String account) throws AccountAlreadyExistsException{
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException("account already exists");
        }
        accountsToTransactions.put(account, new ArrayList<>());

        try{
            writeAccount(account);
        }catch (IOException e){
            System.out.println(e.fillInStackTrace() + " - " + e.getClass() + ": " + e.getMessage());
        }
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException{

        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException("account already exists");
        }else if(accountsToTransactions.containsKey(transactions)){
            throw new TransactionAlreadyExistException("Transaction already exist");
        }
        accountsToTransactions.put(account, new ArrayList<>());
        transactions.forEach(t -> {
            try{
                this.addTransaction(account, t);
            }catch(Exception e){
                System.out.println(e.getClass() + ": " + e.getMessage());
            }

        });
        try{
            writeAccount(account);
        }catch (IOException e){
            System.out.println(e.fillInStackTrace() + " - " + e.getClass() + ": " + e.getMessage());
        }
    }

    public List<String> getAllAccounts(){

        return  new ArrayList<>(accountsToTransactions.keySet());
    }
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if(!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("account does not exist");
        }
        accountsToTransactions.remove(account);

        File file = new File(this.getDirectoryName() +"/" + account + ".json");
        boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    System.out.println("File does not exist: " + file);
                }
                System.out.println("Unable to delete file: " + file);
            }
    }
    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException{

        if(!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("account does not exist");

        }


        if(transaction.getClass() == Payment.class){
            if(((Payment)transaction).getIncomingInterest() < 0 || ((Payment)transaction).getIncomingInterest() > 1 ||
              ((Payment)transaction).getOutgoingInterest() < 0 || ((Payment)transaction).getOutgoingInterest() > 1){
                throw new TransactionAttributeException("Zinsen muss 0 bis 1 sein");
            }
            ((Payment) transaction).setIncomingInterest(this.getIncomingInterest());
            ((Payment) transaction).setOutgoingInterest(this.getOutgoingInterest());
        }else if(transaction instanceof Transfer && ((Transfer)transaction).getAmount() < 0){
             throw new TransactionAttributeException("Amount muss > 0  sein");
        }
        if(accountsToTransactions.get(account).contains(transaction)) {
            throw new TransactionAlreadyExistException("transaction already exists");
        }
        accountsToTransactions.get(account).add(transaction);
        try{
            writeAccount(account);
        }catch (IOException e){
            System.out.println(e.fillInStackTrace() + " - " + e.getClass() + ": " + e.getMessage());
        }

    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException{
        if(!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("account does not exist");
        }
        else if(!accountsToTransactions.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException("transaction does not exists");
        }
        accountsToTransactions.get(account).remove(transaction);
        try{
            writeAccount(account);
        }catch (IOException e){
            System.out.println(e.fillInStackTrace() + " - " + e.getClass() + ": " + e.getMessage());
        }
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    public boolean containsTransaction(String account, Transaction transaction){
        return accountsToTransactions.get(account).contains(transaction);
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    public double getAccountBalance(String account){
        double balance = accountsToTransactions.get(account).stream().mapToDouble(CalculateBill::calculate).sum();
        return balance;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    public List<Transaction> getTransactions(String account){
        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    public List<Transaction> getTransactionsSorted(String account, boolean asc){
        List<Transaction> transactions = getTransactions(account);

        transactions.sort((t1,t2) -> asc ? (int)(t1.calculate() - t2.calculate()) : (int)(t2.calculate() - t1.calculate()));

        return transactions;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    public List<Transaction> getTransactionsByType(String account, boolean positive){
        return accountsToTransactions.get(account)
                                     .stream()
                                     .filter(transaction -> positive ? transaction.calculate() > 0: transaction.calculate() < 0 )
                                     .collect(Collectors.toList());
    }

    /*
     * Hole Daten von Dateien im directoryName und setze alle Daten zu einer entsprechenden Transaction class
     * jede einzelne Datei bezieht sich auf ein Account
     * Füge danach alle Transactions in accountToTransactions
     * @throws IOException
     */
    private void readAccounts() throws IOException {
        Gson gson = new Gson();

       File folder = new File(this.getDirectoryName());
       File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String filePath = file.getName();
                String accountName = filePath.split("\\.(?=[^\\.]+$)")[0];
                String absPath = this.getDirectoryName() + "/" + filePath;
                FileReader fR = new FileReader(absPath);
                JsonArray jsonArr = gson.fromJson(fR, JsonArray.class);
                fR.close();
                Gson gsonBuilder = new GsonBuilder()
                        .registerTypeAdapter(Transaction.class, new CustomDeserializer())
                        .setPrettyPrinting()
                        .create();
                try{
                    this.createAccount(accountName);
                }catch (AccountAlreadyExistsException e){
                    System.out.println(e.getClass() + " :" + e.getMessage());
                }
                jsonArr.forEach(jsonElement -> {
                    Transaction res = gsonBuilder.fromJson(jsonElement, Transaction.class);
                    try{
                        this.addTransaction(accountName, res);
                    } catch (TransactionAlreadyExistException | AccountDoesNotExistException |
                             TransactionAttributeException e) {
                        System.out.println(e.getClass() + ": " + e.getMessage());
                    }
                });
            }
        }
    }

    /*
     * Konvertiere alle Transactions einer Account zu einem String im json format
     * Füge das String als .json Dokument in directoryName rein
     * Wenn die Name der Datei schon existiert, ersetze die Datei
     * @param account
     * @throws IOException
     */
    void writeAccount(String account) throws IOException{
        List<String> transactionsArray = new ArrayList<>();
        accountsToTransactions.get(account).forEach(transaction -> {

                Gson gsonBuilder = new GsonBuilder()
                            .registerTypeAdapter(transaction.getClass(), new CustomSerializer())
                            .setPrettyPrinting()
                            .create();
                String transactionJson = gsonBuilder.toJson(transaction);
                transactionsArray.add(transactionJson);

        });
        String transactionsStringArray  = transactionsArray.toString();
        try(FileWriter writer = new FileWriter(this.getDirectoryName() + "/" + account + ".json")){
                writer.write(transactionsStringArray);
        }catch (IOException e){
                System.out.println(e.getClass() + ": " + e.getMessage());
        }
    }
}
