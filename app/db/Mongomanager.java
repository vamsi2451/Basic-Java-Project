package db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import Models.Employee;
import play.Configuration;

import javax.inject.Inject;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import play.Configuration;

public class Mongomanager {


    public   MongoCollection<Document> employeeCollection;
    private Configuration configuration;
    public MongoDatabase database;

    // Constructor-based dependency injection
    public Mongomanager() {
//        Access MongoDB URI and database name from application.conf
//        String uri = configuration.getString("mongodb.uri");
//        String databaseName = configuration.getString("mongodb.database");

        String uri = "mongodb://localhost:27017";
        String databaseName = "local";

        if (uri == null || databaseName == null) {
            throw new RuntimeException("MongoDB URI or Database name not configured properly.");
        }

        MongoClient client = MongoClients.create(uri);
        this.database = client.getDatabase(databaseName);
        this.employeeCollection = database.getCollection("Employee_Data");
    }

    Mongomanager(MongoCollection employeeCollection){
        this.employeeCollection = employeeCollection;
    }

//    public List<Employee> getAllEmployees(String employeeCollection) {
//        MongoCollection mongoCollection = database.getCollection("Employee_Data");
//        List<Employee> employees = new ArrayList<>();
//        for (Document doc : employeeCollection.find()) {
//            employees.add(Employee.fromDocument(doc));
//        }
//        return employees;
//    }

    public Optional<Employee> getEmployeeById(String id) {
        Document doc = employeeCollection.find(new Document("id", id)).first();
        return Optional.ofNullable(doc).map(Employee::fromDocument);
    }

    public void addEmployee(Employee employee) {
        Document existingEmployee = employeeCollection.find(new Document("mail", employee.getMail())).first();
        if (existingEmployee != null) {
            throw new RuntimeException("An employee with this email already exists.");
        }
        employeeCollection.insertOne(employee.toDocument());
    }

    public void updateEmployee(String id, Employee updatedEmployee) {
        Document updateDoc = updatedEmployee.toDocument();
        employeeCollection.updateOne(new Document("id", id), new Document("$set", updateDoc));
    }

    public boolean deleteEmployee(String id) {
        return employeeCollection.deleteOne(new Document("id", id)).getDeletedCount() > 0;
    }

    public List<Employee> searchEmployees(String query, int page, int size) {
        List<Employee> employees = new ArrayList<>();
        int skip = (page - 1) * size;

        for (Document doc : employeeCollection.find(
                Filters.or(
                        Filters.regex("name", query, "i"),
                        Filters.regex("role", query, "i"),
                        Filters.regex("mail", query, "i")
                )
        ).skip(skip).limit(size)) {
            employees.add(Employee.fromDocument(doc));
        }
        return employees;
    }

    public long getSearchResultCount(String query) {
        return employeeCollection.countDocuments(
                Filters.or(
                        Filters.regex("name", query, "i"),
                        Filters.regex("role", query, "i"),
                        Filters.regex("mail", query, "i")
                )
        );
    }

    public List<Employee> getByName(String name) {
        List<Employee> employees = new ArrayList<>();
        for (Document doc : employeeCollection.find(new Document("name", name))) {
            employees.add(Employee.fromDocument(doc));
        }
        return employees;
    }

    public List<Employee> getEmployeesWithPagination(int page, int size) {
        List<Employee> employees = new ArrayList<>();
        int skip = (page - 1) * size;
        for (Document doc : employeeCollection.find().skip(skip).limit(size)) {
            employees.add(Employee.fromDocument(doc));
        }
        return employees;
    }

    public long getEmployeeCount() {
        return employeeCollection.countDocuments();
    }
}

