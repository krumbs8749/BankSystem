package bank;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomDeserializer implements JsonDeserializer<Transaction> {
    /*
     * Konvertiere JsonObject zu Instanzen von Transaction
     * Class der zu erstellenden Transaktion hängt vom CLASSNAME-Attribut in JsonObject ab
     * @param transaction
     * @param type
     * @param jsonSerializationContext
     * @return
     */
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        String className = jsonObject.get("CLASSNAME").getAsString();
        JsonObject instance = jsonObject.get("INSTANCE").getAsJsonObject();
        
        if(className.equals("Payment")){
            return new Payment(
                    instance.get("date").getAsString(),
                    instance.get("amount").getAsDouble(),
                    instance.get("description").getAsString(),
                    instance.get("incomingInterest").getAsDouble(),
                    instance.get("outgoingInterest").getAsDouble());
        } else if (className.equals("OutgoingTransfer")) {
            return new OutgoingTransfer(
                    instance.get("date").getAsString(),
                    instance.get("amount").getAsDouble(),
                    instance.get("description").getAsString(),
                    instance.get("sender").getAsString(),
                    instance.get("recipient").getAsString());
            
        } else if (className.equals("IncomingTransfer")) {
            return new IncomingTransfer(
                    instance.get("date").getAsString(),
                    instance.get("amount").getAsDouble(),
                    instance.get("description").getAsString(),
                    instance.get("sender").getAsString(),
                    instance.get("recipient").getAsString());
        }
        return null;
    }
}
