package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.*;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class CrudCustomer {
    
    // Formato miles, crear un DecimalFormat con el patrón de formato deseado
    DecimalFormat formatter = new DecimalFormat("#,###");

    private static final String DATABASE_NAME = "TeslaFXStoreDB";
    private static final String COLLECTION_NAME = "customer";

    // Método privado para obtener la colección de MongoDB, en caso de que no cambie la coleccion
    private static MongoCollection<Document> getCollection() {
        MongoDatabase database = Conexion.getMongoClient().getDatabase(DATABASE_NAME);
        return database.getCollection(COLLECTION_NAME);
    }

    // Método para añadir datos a la colección usando un objeto Customer
    public void saveCustomer(Customer customer) {
        MongoCollection<Document> collection = getCollection();

        // Crea un documento a partir del objeto Customer y lo inserta en la colección
        Document document = new Document("customerName", customer.getCustomerName())
                .append("country", customer.getCountry())
                .append("age", customer.getAge())
                .append("email", customer.getEmail())
                .append("password", customer.getPassword());
        collection.insertOne(document);

        System.out.println("Datos guardados exitosamente en MongoDB.");
    }

    // Método para buscar un documento en la colección usando un objeto Customer
    // Método para buscar un documento en la colección usando un objeto Customer
    public Customer findCustomer(String email) {
        MongoCollection<Document> collection = getCollection();

        // Crea una consulta para encontrar un documento basado en el campo email
        Document query = new Document("email", email);
        Document result = collection.find(query).first();

        if (result != null) {
            // Extrae los datos del Document y crea un objeto Customer
            ObjectId id = result.getObjectId("_id");
            String customerName = result.getString("customerName");
            String country = result.getString("country");
            String age = result.getString("age");
            String password = result.getString("password"); // Asegúrate de que el campo password también esté en el documento

            // Crear y devolver un objeto Customer
            return new Customer(id, customerName, country, age, email, password);
        } else {
            return null; // O lanza una excepción si prefieres manejarlo de otra manera
        }
    }

    // Método para eliminar un documento de la colección usando un objeto Customer
    public void deleteCustomer(String email) {
        MongoCollection<Document> collection = getCollection();

        // Elimina el documento que coincida con el email del Customer
        Document query = new Document("email", email);
        collection.deleteOne(query);

        System.out.println("Datos eliminados exitosamente de MongoDB.");
    }

    public String listCustomers() {
        MongoCollection<Document> collection = getCollection();
        
        int contCustomer = 1;
        StringBuilder listaBuilder = new StringBuilder();
        
        // Itera sobre cada documento y lo convierte en un objeto Customer
        for (Document doc : collection.find()) {
        listaBuilder.append("------------- Customer "+contCustomer+" -------------\n");
            for(String key : doc.keySet()){
                listaBuilder
                        .append("- ")
                        .append(key)
                        .append(" = ")
                        .append(doc.get(key))
                        .append("\n");
            }
            listaBuilder.append("\n");
            contCustomer++;
//            listaBuilder.append("------------- Customer "+contCustomer+" -------------\n");
        }
        return listaBuilder.toString();
    }

    // Método para modificar un documento en la colección usando un objeto Customer
    public void updateCustomer(Customer customer) {
        MongoCollection<Document> collection = getCollection();

        // Crea una consulta para encontrar el documento basado en el email
        Bson filter = eq("email", customer.getEmail());

        // Crea una actualización combinada para modificar los campos del documento
        Bson update = combine(
                set("customerName", customer.getCustomerName()),
                set("country", customer.getCountry()),
                set("age", customer.getAge()),
                set("email", customer.getEmail()),
                set("password", customer.getPassword())
        );

        // Actualiza el documento
        collection.updateOne(filter, update);

        System.out.println("Datos actualizados exitosamente en MongoDB.");
    }
}
