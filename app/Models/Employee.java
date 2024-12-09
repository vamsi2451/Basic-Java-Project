package Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.Document;

import java.util.UUID;
@Data
public class Employee {
    @JsonProperty("id")
    private String id;  // Use String for unique ID
    @JsonProperty("name")
    private String name;
    @JsonProperty("role")
    private String role;
    @JsonProperty("mail")
    private String mail;

    // Default constructor
    public Employee() {
        this.id = generateUniqueId();  // Automatically generate a unique ID
    }

    // Constructor with parameters
    public Employee(String id, String name, String role, String mail) {
        this.id = (id != null) ? id : generateUniqueId();
        this.name = name;
        this.role = role;
        this.mail = mail;
    }
    // Generate a unique ID
    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    // Method to convert to Document
    public Document toDocument() {
        Document d = new Document();
        if (id != null) {
            d.append("id", id);  // Store the id as a string
        }
        d.append("name", name);
        d.append("role", role);
        d.append("mail", mail);
        return d;
    }

    // Static method to convert Document to Employee object
    public static Employee fromDocument(Document d) {
        return new Employee(
                d.getString("id"),
                d.getString("name"),
                d.getString("role"),
                d.getString("mail")
        );
    }
}
