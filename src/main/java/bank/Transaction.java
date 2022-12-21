package bank;

public abstract class Transaction implements CalculateBill{
    /*
     * Datum (DD.MM.YYYY) der Transaktion
     */
    private String date;
    /*
     * Die Geldmenge einer Transaktion
     */
    private double amount;
    /*
     * Zusätzliche Beschreibung des Vorgangs
     */
    private String description;

    /*
     * Konstrukteur fur alle Attribute (date, amount, description)
     * @param date: Datum (DD.MM.YYYY) der Transaktion
     * @param amount: Die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param description: Zusätzliche Beschreibung des Vorgangs
     */
    public Transaction(String date, double amount, String description) {
        setDate(date);
        setDescription(description);
        setAmount(amount);
    }
    /*
     * @param payment : Copy-Konstruktur - alle attribute von übergebenen parameter wird zu den entspreschenden Attribute zugewisen
     */
    public Transaction (Transaction transaction){
        setDate(transaction.getDate());
        setAmount(transaction.getAmount());
        setDescription(transaction.getDescription());
    }
    /*
     * @return date
     */
    public String getDate() {
        return date;
    }

    /*
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /*
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /*
     * @param amount
     */
    public void setAmount(double amount)  {
        this.amount = amount;
    }

    /*
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /*
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /*
     * Überschreiben toString() in 'class Object' um die Information der Transaktion auszugeben
     * @return string
     */
    @Override
    public String toString() {
        return "date: " + getDate() + "\n" +
                "amount: " + calculate() + "\n" +
                "description: " + getDescription() + "\n";
    }

    /*
     * Überschreiben equals(obj: Object) in 'class Object' um zu uberpruefen, ob die übergebene Transaktion object die gleichen Werte von Attributen
     *  besitzen wie eigene Transaktion objekt
     * @param obj
     * @return boolean: true
     */
    @Override
    public boolean equals(Object obj) {
        return getDate().equals(((Transaction) obj).getDate())
                && getAmount() == ((Transaction) obj).getAmount()
                && getDescription().equals(((Transaction) obj).getDescription());
    }
}
