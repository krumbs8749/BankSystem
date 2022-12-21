package bank;

public class IncomingTransfer extends Transfer {

    /*
     * Konstrukteur fuer 3 Attribute (date, amount, description)
     * @param date: Datum (DD.MM.YYYY) der Transaktion
     * @param amount: Die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung [amount >= 0]
     * @param description : Zusätzliche Beschreibung des Vorgangs
     */
    public IncomingTransfer(String date, double amount, String description) {
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
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient) {

        super(date, amount, description, sender,recipient);
    }

    /*
     * @param transfer: Copy-kontrukstur - alle Attribute von übergebenen parameter wird zu dem entsprechenden Attribute zugewiesen
     */
    public IncomingTransfer(Transfer transfer){
        super(transfer);
    }

    /*
     * Gebe die positive amount zurück
     * @return amount: double
     */
    @Override
    public double calculate() {
        return getAmount();
    }
    /*
     * Überschreiben toString() in 'class Transaktion' um zusätzliche Information uber Transfer auszugeben
     * @return string
     */
    @Override
    public String toString() {
        return "Type: Incoming Transfer\n"  + super.toString();
    }
}
