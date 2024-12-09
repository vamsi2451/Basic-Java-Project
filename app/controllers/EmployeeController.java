package controllers;

import Models.Employee;
import Models.Response;
import services.EmployeeService;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.List;


public class EmployeeController extends Controller {

    public EmployeeService employeeService;

    // Constructor-based dependency injection
    public EmployeeController() {
        this.employeeService = new EmployeeService();
    }

    public Result getEmployee(String id) {
        return employeeService.getEmployeeById(id)
                .map(employee -> ok(Json.toJson(employee)))
                .orElse(notFound("Employee not found"));
    }

    public Result addEmployee() {
        Response resp = new Response();
        Employee employee = Json.fromJson(request().body().asJson(), Employee.class);
        try {
            employeeService.addEmployee(employee);
            resp.setStatus(200);
            resp.setData(employee);
            resp.setMessage("Employee added to the DB successfully");
            return ok(Json.toJson(resp)).withHeader("Access-Control-Allow-Origin", "*");
        } catch (RuntimeException e) {
            resp.setErrorMessage(e.getMessage());
            resp.setStatus(500);
            return badRequest(Json.toJson(resp)).withHeader("Access-Control-Allow-Origin", "*");
        }
    }

    public Result updateEmployee(String id) {
        Response resp =new Response();
        Employee updatedEmployee = Json.fromJson(request().body().asJson(), Employee.class);
        employeeService.updateEmployee(id, updatedEmployee);
        resp.setData(updatedEmployee);
        resp.setMessage("Employee Updated successfully");
        resp.setStatus(200);
        return ok(Json.toJson(resp)).withHeader("Access-Control-Allow-Origin", "*");
    }

    public Result deleteEmployee(String id) {
        Response resp = new Response();
        if (employeeService.deleteEmployee(id)) {
            resp.setStatus(201);
            resp.setMessage("Employee Deleted successfully");
            return ok(Json.toJson(resp)).withHeader("access-control-allow-origin", "*");
        }
        resp.setErrorMessage("Employee Not Found");
        resp.setStatus(501);
        return  ok(Json.toJson(resp)).withHeader("access-control-allow-origin", "*");
    }

    public Result getAllEmployees(int page, int size) {
        Response response = new Response();
        List<Employee> employees = employeeService.getEmployeesWithPagination(page, size);
        long total = employeeService.getEmployeeCount();
        response.setStatus(200);
        response.setData(employees);
        response.setMessage("Employee fetched successfully");
        response.setTotal(total);
        return ok(Json.toJson(response)).withHeader("access-control-allow-origin", "*");
    }

    public Result searchEmployees(String query, int page, int size) {
        Response response = new Response();
        List<Employee> employees = employeeService.searchEmployees(query, page, size);
        long total = employeeService.getSearchResultCount(query);
        response.setTotal(total);
        response.setMessage("Searching operation successful");
        response.setData(employees);
        response.setStatus(200);
        return ok(Json.toJson(response)).withHeader("access-control-allow-origin", "*");
    }

    public Result options(String path) {
        return ok()
                .withHeader("Access-Control-Allow-Origin", "*")
                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .withHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
    }
}
