package bank;

import bank.exceptions.TransactionAttributeException;

/*
 * @author Ahmad Ikram Hafidz Bin Ahmad Darwis
 * @matrikelnummer 3526017
 */
public class Transfer extends Transaction {



    private String sender;
    /*
     * Der Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat.
     */
    private String recipient;

    /*
     * Konstrukteur fuer 3 Attribute (date, amount, description)
     * @param date: Datum (DD.MM.YYYY) der Transaktion
     * @param amount: Die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung [amount >= 0]
     * @param description : Zusätzliche Beschreibung des Vorgangs
     */
    public Transfer(String date, double amount, String description) {
        super(date, amount,description);
    }

    /*
     * Konstrukteur fuer alle Attribute
     * @param date: Datum (DD.MM.YYYY) der Transaktion
     * @param amount: Die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung [amount >= 0]
     * @param description : Zusätzliche Beschreibung des Vorgangs
     * @param sender: Der Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat.
     * @param recipient: Der Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat.
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) {

        super(date, amount, description);
        setSender(sender);
        setRecipient(recipient);
    }

    /*
     * @param transfer: Copy-kontrukstur - alle Attribute von übergebenen parameter wird zu dem entsprechenden Attribute zugewiesen
     */
    public Transfer(Transfer transfer){
        super(transfer.getDate(), transfer.getAmount(), transfer.getDescription());
        setSender(transfer.sender);
        setRecipient(transfer.recipient);
    }



    /* Ueberschreiben setAmount(amount: double) methode in 'class Transaktion', um zu ueberpruefen, ob amount > 0 ist.
     * Wenn nicht, gebe eine Fehlermeldung aus
     * @param amount
     */
    @Override
    public void setAmount(double amount) {
        try{
            if(amount < 0) {
            throw new TransactionAttributeException("For Transfer-Class, amount must be > 0");
            }
            super.setAmount(amount);
        }catch(TransactionAttributeException e){
            System.out.println(e.getClass() + ": " + e.getMessage());
        }

    }

    /*
     * @return sender
     */
    public String getSender() {
        return sender;
    }

    /* Setzen sender
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /*
     * @return recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /*
     * Setzt recipient
     * @param recipient
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }



    /*
     * Gebe die amount zurück
     * @return amount: double
     */
    public double calculate() {
        return getAmount();
    }
    /*
     * Überschreiben toString() in 'class Transaktion' um zusätzliche Information uber Transfer auszugeben
     * @return string
     */
    @Override
    public String toString() {
        return super.toString() + "sender: " + getSender() + "\n" +
                "recipient: " + getRecipient() + "\n";
    }
    /*
     * Überschreiben equals(obj: Object) in 'class Transaktion' um zu uberpruefen, ob die übergebene Payment object die gleichen Werte von Attributen
     *  besitzen wie eigene Payment objekt
     * @param obj
     * @return boolean: true
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && getSender().equals(((Transfer)obj).getSender())
                && getRecipient().equals(((Transfer)obj).getRecipient());
    }
}
