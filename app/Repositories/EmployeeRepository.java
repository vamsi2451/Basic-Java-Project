//package Repositories;
//
//import Models.Employee;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.Sorts;
//import org.bson.Document;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class EmployeeRepository {
//    private final MongoCollection<Document> employeeCollection;
//
//    public EmployeeRepository(String uri, String databaseName, String collectionName) {
//        MongoClient client = MongoClients.create(uri);
//        MongoDatabase database = client.getDatabase(databaseName);
//        this.employeeCollection = database.getCollection(collectionName);
//    }
//
//    public List<Employee> getAllEmployees() {
////        this.employeeCollection = database.getCollection(collectionName);
//        List<Employee> employees = new ArrayList<>();
//        for (Document doc : employeeCollection.find()) {
//            employees.add(Employee.fromDocument(doc));
//        }
//        return employees;
//    }
//
//    public Optional<Employee> getEmployeeById(String id) {
//        Document doc = employeeCollection.find(new Document("id", id)).first();
//        return Optional.ofNullable(doc).map(Employee::fromDocument);
//    }
//
//    public void addEmployee(Employee employee) {
//        employeeCollection.insertOne(employee.toDocument());
//    }
//
//    public void updateEmployee(String id, Employee updatedEmployee) {
//        Document updateDoc = updatedEmployee.toDocument();
//        employeeCollection.updateOne(new Document("id", id), new Document("$set", updateDoc));
//    }
//
//    public boolean deleteEmployee(String id) {
//        return employeeCollection.deleteOne(new Document("id", id)).getDeletedCount() > 0;
//    }
//
////    public List<Employee> getByName(String name) {
////        List<Employee> employees = new ArrayList<>();
////        for (Document doc : employeeCollection.find(new Document("name", name))) {
////            employees.add(Employee.fromDocument(doc));
////        }
////        return employees;
////    }
//public List<Employee> getByName(String name) {
//    List<Employee> employees = new ArrayList<>();
//    // Use regex for partial, case-insensitive matching
//    for (Document doc : employeeCollection.find(new Document("name", new Document("$regex", name).append("$options", "i")))) {
//        employees.add(Employee.fromDocument(doc));
//    }
//    return employees;
//}
//
//    public long getByNameCount(String name) {
//        org.bson.conversions.Bson regexQuery = Filters.regex("name", ".*" + name + ".*", "i"); // Case-insensitive partial match
//        return employeeCollection.countDocuments(regexQuery);
//    }
//
//    public long deleteByName(String name) {
//        return employeeCollection.deleteMany(new Document("name", name)).getDeletedCount();
//    }
//
//    public List<Employee> getEmployeesWithPagination(int page, int size) {
//        List<Employee> employees = new ArrayList<>();
//        int skip = (page - 1) * size;
//        for (Document doc : employeeCollection.find().skip(skip).limit(size)) {
//            employees.add(Employee.fromDocument(doc));
//        }
//        return employees;
//    }
//    public long getEmployeeCount() {
//        return employeeCollection.countDocuments();
//    }
//
//    public List<Employee> searchEmployees(String query, int page, int size) {
//        List<Employee> employees = new ArrayList<>();
//        int skip = (page - 1) * size;
//
//        // Apply search filter using case-insensitive regex for the query
//        for (Document doc : employeeCollection.find(
//                Filters.or(
//                        Filters.regex("name", query, "i"),
//                        Filters.regex("role", query, "i"),
//                        Filters.regex("mail", query, "i")
//                )
//        ).skip(skip).limit(size)) {
//            employees.add(Employee.fromDocument(doc));
//        }
//        return employees;
//    }
//
//    public long getSearchResultCount(String query) {
//        return employeeCollection.countDocuments(
//                Filters.or(
//                        Filters.regex("name", query, "i"),
//                        Filters.regex("role", query, "i"),
//                        Filters.regex("mail", query, "i")
//                )
//        );
//    }
//
//
//}
