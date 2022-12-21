package bank;

import bank.exceptions.TransactionAttributeException;

/*
 * @author Ahmad Ikram Hafidz Bin Ahmad Darwis
 * @matrikelnummer 3526017
 */
public class Payment extends Transaction {
    /*
     * Die Zinsen der Einzahlung (positiver Wert in Prozent, 0 bis 1
     */
    private  double incomingInterest;
    /*
     * Die Zinsen der Auszahlung (positiver Wert in Prozent, 0 bis 1
     */
    private double outgoingInterest;

    /*
     * @param date: Datum (DD.MM.YYYY) der Transaktion
     * @param amount: Die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param description: Zusätzliche Beschreibung des Vorgangs
     */
    public Payment(String date, double amount, String description) {
        super(date, amount, description);
    }

    /*
     * @param date : Datum (DD.MM.YYYY) der Transaktion
     * @param amount: Die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param description: Zusätzliche Beschreibung des Vorgangs
     * @param incomingInterest: Die Zinsen der Einzahlung (positiver Wert in Prozent, 0 bis 1;
     * @param outgoingInterest: Die Zinsen der Auszahlung(positiver Wert in Prozent, 0 bis 1
     */
    public Payment (String date, double amount, String description, double incomingInterest, double outgoingInterest){

        super(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }
    /*
     * @param payment : Copy-Konstruktur - alle Attribute von übergebenen parameter wird zu dem entsprechenden Attribute zugewiesen
     */
    public Payment (Payment payment){
        super(payment.getDate(), payment.getAmount(), payment.getDescription());
        setIncomingInterest(payment.incomingInterest);
        setOutgoingInterest(payment.outgoingInterest);
    }



    /*
     * @return incomingInterest
     */
    public double getIncomingInterest() {

        return incomingInterest;
    }

    /* Die Zinsen muss einen positiven Wert in Prozent, 0 bis 1 sein. Sonst gebe eine Fehlermeldung aus
     * @param incomingInterest
     */
    public void setIncomingInterest(double incomingInterest) {
        try{
            if(incomingInterest < 0 || incomingInterest > 1){
                throw new TransactionAttributeException("Einzahlung scheitert: Zinsen muss 0 bis 1 sein");
            }
            this.incomingInterest = incomingInterest;
        }catch(TransactionAttributeException e){
            System.out.println(e.getClass() + ": " + e.getMessage());
        }
    }

    /*
     * @return outgoingInterest
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /* Die Zinsen muss einen positiven Wert in Prozent, 0 bis 1 sein. Sonst gebe eine Fehlermeldung aus
     * @param outgoingInterest
     */
    public void setOutgoingInterest(double outgoingInterest) {

        if(outgoingInterest < 0 || outgoingInterest > 1){
            System.out.println("Auszahlung scheitert: Zinsen muss 0 bis 1 sein");
        }
        this.outgoingInterest = outgoingInterest;
    }

    /*
     *  Falls amount > 0, soll der Wert des incomingInterest Attributes prozentual von der Einzahlung abgezogen und das Ergebnis zurückgegeben werden.
     *  Beispiel: 1000 € einzahlen, incomingInterest = 0.05 => 950€
     *  Falls amount < 0, soll der Wert des outgoingInterest Attributes prozentual zu der Auszahlung hinzuaddiert und das Ergebnis zurückgegeben werden.
     *  Beispiel: -1000€ auszahlen, outgoingInterest = 0.1 => -1100€
     *  @return Aus/Einzahlung Wert: double
     */
    public double calculate() {
        if(getAmount() > 0){
            return getAmount() * (1 - getIncomingInterest());
        }else {
            return getAmount() * (1 + getOutgoingInterest());
        }
    }

    /*
     * Überschreiben toString() in 'class Transaktion' um zusätzliche Information uber Payment auszugeben
     * @return string
     */
    @Override
    public String toString() {
        return "Type: Payment\n" + super.toString() + "Incoming Interest: " + getIncomingInterest() + "\n" +
                "Outgoing Interest: " + getOutgoingInterest() + "\n";
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
                && getOutgoingInterest() == ((Payment)obj).getOutgoingInterest()
                && getIncomingInterest() == ((Payment)obj).getIncomingInterest();
    }
}
