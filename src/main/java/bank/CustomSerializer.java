package bank;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomSerializer implements JsonSerializer<Transaction> {
    /*
     * Konvertiere Instanzen von Transaction zu einem JsonObject
     * Das Format des Object hängt von der Art des Transaction (Payment / Transfer) ab
     * @param transaction
     * @param type
     * @param jsonSerializationContext
     * @return
     */
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        if(transaction.getClass() == Payment.class){
            JsonObject object = new JsonObject();
            JsonObject instance = new JsonObject();
            object.addProperty("CLASSNAME", "Payment");
            instance.addProperty("incomingInterest", ((Payment)transaction).getIncomingInterest());
            instance.addProperty("outgoingInterest", ((Payment)transaction).getOutgoingInterest());
            instance.addProperty("date", transaction.getDate());
            instance.addProperty("amount", transaction.getAmount());
            instance.addProperty("description", transaction.getDescription());
            object.add("INSTANCE", instance );
            return object;
        }else if(transaction.getClass() == OutgoingTransfer.class){
            JsonObject object = new JsonObject();
            JsonObject instance = new JsonObject();
            object.addProperty("CLASSNAME", "OutgoingTransfer");
            instance.addProperty("sender", ((OutgoingTransfer)transaction).getSender());
            instance.addProperty("recipient", ((OutgoingTransfer)transaction).getRecipient());
            instance.addProperty("date", transaction.getDate());
            instance.addProperty("amount", transaction.getAmount());
            instance.addProperty("description", transaction.getDescription());
            object.add("INSTANCE", instance );
            return object;
        }else if(transaction.getClass() == IncomingTransfer.class){
            JsonObject object = new JsonObject();
            JsonObject instance = new JsonObject();
            object.addProperty("CLASSNAME", "IncomingTransfer");
            instance.addProperty("sender", ((IncomingTransfer)transaction).getSender());
            instance.addProperty("recipient", ((IncomingTransfer)transaction).getRecipient());
            instance.addProperty("date", transaction.getDate());
            instance.addProperty("amount", transaction.getAmount());
            instance.addProperty("description", transaction.getDescription());
            object.add("INSTANCE", instance );
            return object;
        }
        return null;
    }
}
