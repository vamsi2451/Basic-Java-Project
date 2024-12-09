//package services;
//
//import db.Mongomanager;
//import Models.Employee;
//
//import javax.inject.Inject;
//import java.util.List;
//import java.util.Optional;
//
//public class EmployeeService {
//
//    private final Mongomanager mongoManager;
//
//    @Inject
//    public EmployeeService(Mongomanager mongoManager) {
//        this.mongoManager = mongoManager;
//    }
//
//    // Method to get all employees
//    public List<Employee> getAllEmployees() {
//        return mongoManager.getAllEmployees();
//    }
//
//    // Method to get employee by ID
//    public Optional<Employee> getEmployeeById(String id) {
//        return mongoManager.getEmployeeById(id);
//    }
//
//    // Method to add a new employee
////    public void addEmployee(Employee employee) {
////        mongoManager.addEmployee(employee);
////    }
//    public void addEmployee(Employee employee) {
//        try {
//            mongoManager.addEmployee(employee);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Error adding employee: " + e.getMessage());
//        }
//    }
//
//
//    // Method to update an existing employee
//    public void updateEmployee(String id, Employee updatedEmployee) {
//        mongoManager.updateEmployee(id, updatedEmployee);
//    }
//
//    // Method to delete employee by ID
//    public boolean deleteEmployee(String id) {
//        return mongoManager.deleteEmployee(id);
//    }
//
//    // Method to get employees by name
//    public List<Employee> getEmployeesByName(String name) {
//        return mongoManager.getByName(name);
//    }
//
//        // Method to delete employees by name
////    public long deleteEmployeesByName(String name) {
////        return mongoManager.deleteEmployeesByName(name);
////    }
//
//        // Method for pagination of employees
//    public List<Employee> getEmployeesWithPagination(int page, int size) {
//        return mongoManager.getEmployeesWithPagination(page, size);
//    }
//
//        // Method to get the count of employees
//    public long getEmployeeCount() {
//        return mongoManager.getEmployeeCount();
//    }
//
//        // Method to search employees by name, role, or mail
//        public List<Employee> searchEmployees (String query,int page, int size){
//            return mongoManager.searchEmployees(query, page, size);
//        }
//
//        // Method to get the count of search results
//        public long getSearchResultCount (String query){
//            return mongoManager.getSearchResultCount(query);
//        }
//    }


package services;

import com.google.inject.Inject;
import db.Mongomanager;
import Models.Employee;

import java.util.List;
import java.util.Optional;

public class EmployeeService {

    @Inject
    public   Mongomanager mongoManager;

    // Constructor-based dependency injection
    public EmployeeService() {
        this.mongoManager = new Mongomanager();
    }

    EmployeeService(Mongomanager mongoManager){
        this.mongoManager = mongoManager;
    }

//    public List<Employee> getAllEmployees() {
//        return mongoManager.getAllEmployees("Employee_Data");
//    }

    public Optional<Employee> getEmployeeById(String id) {
        return mongoManager.getEmployeeById(id);
    }

    public void addEmployee(Employee employee) {
        try {
            mongoManager.addEmployee(employee);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error adding employee: " + e.getMessage());
        }
    }

    public void updateEmployee(String id, Employee updatedEmployee) {
        mongoManager.updateEmployee(id, updatedEmployee);
    }

    public boolean deleteEmployee(String id) {
        return mongoManager.deleteEmployee(id);
    }

    public List<Employee> getEmployeesByName(String name) {
        return mongoManager.getByName(name);
    }

    public List<Employee> getEmployeesWithPagination(int page, int size) {
        return mongoManager.getEmployeesWithPagination(page, size);
    }

    public long getEmployeeCount() {
        return mongoManager.getEmployeeCount();
    }

    public List<Employee> searchEmployees(String query, int page, int size) {
        return mongoManager.searchEmployees(query, page, size);
    }

    public long getSearchResultCount(String query) {
        return mongoManager.getSearchResultCount(query);
    }
}
