package controllers;

import Models.Employee;
import Models.Response;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import services.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static play.test.Helpers.*;


@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {
    @Mock
    private EmployeeController employeeController;

    @Mock
    private EmployeeService mockEmployeeService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        employeeController = new EmployeeController();
        employeeController.employeeService = mockEmployeeService;
    }

    @Test
    public void testGetEmployeeSuccess() {
        String id = "123";
        Employee employee = new Employee(id, "John Doe", "Engineer", "john.doe@example.com");
        when(mockEmployeeService.getEmployeeById(id)).thenReturn(Optional.of(employee));

        Result result = employeeController.getEmployee(id);

        assertEquals(OK, result.status());
        JsonNode jsonResponse = Json.parse(contentAsString(result));
        assertEquals("John Doe", jsonResponse.get("name").asText());
    }

    @Test
    public void testGetEmployeeNotFound() {
        String id = "123";
        when(mockEmployeeService.getEmployeeById(id)).thenReturn(Optional.empty());

        Result result = employeeController.getEmployee(id);

        assertEquals(NOT_FOUND, result.status());
        assertEquals("Employee not found", contentAsString(result));
    }

    @Test
    public void testDeleteEmployeeSuccess() {
        String id = "123";
        when(mockEmployeeService.deleteEmployee(id)).thenReturn(true);

        Result result = employeeController.deleteEmployee(id);

//        assertEquals(CREATED, result.status());
        JsonNode jsonResponse = Json.parse(contentAsString(result));
        assertEquals(201, jsonResponse.get("status").asInt());
        assertEquals("Employee Deleted successfully", jsonResponse.get("message").asText());
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        String id = "123";
        when(mockEmployeeService.deleteEmployee(id)).thenReturn(false);

        Result result = employeeController.deleteEmployee(id);

        assertEquals(OK, result.status());
        JsonNode jsonResponse = Json.parse(contentAsString(result));
        assertEquals(501, jsonResponse.get("status").asInt());
        assertEquals("Employee Not Found", jsonResponse.get("errorMessage").asText());
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("123", "John Doe", "Engineer", "john.doe@example.com"));
        when(mockEmployeeService.getEmployeesWithPagination(1, 10)).thenReturn(employees);
        when(mockEmployeeService.getEmployeeCount()).thenReturn(1L);

        Result result = employeeController.getAllEmployees(1, 10);

        assertEquals(OK, result.status());
        JsonNode jsonResponse = Json.parse(contentAsString(result));
        assertEquals(200, jsonResponse.get("status").asInt());
        assertEquals("Employee fetched successfully", jsonResponse.get("message").asText());
        assertEquals(1, jsonResponse.get("total").asInt());
    }

    @Test
    public void testSearchEmployees() {
        String query = "Engineer";
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("123", "John Doe", "Engineer", "john.doe@example.com"));
        when(mockEmployeeService.searchEmployees(query, 1, 10)).thenReturn(employees);
        when(mockEmployeeService.getSearchResultCount(query)).thenReturn(1L);

        Result result = employeeController.searchEmployees(query, 1, 10);

        assertEquals(OK, result.status());
        JsonNode jsonResponse = Json.parse(contentAsString(result));
        assertEquals(200, jsonResponse.get("status").asInt());
        assertEquals("Searching operation successful", jsonResponse.get("message").asText());
        assertEquals(1, jsonResponse.get("total").asInt());
    }

    @Test
    public void testAddEmployeeSuccess() {
        // Arrange
        Employee employee = new Employee("123", "John Doe", "Engineer", "john.doe@example.com");
        doNothing().when(mockEmployeeService).addEmployee(employee);

        JsonNode requestJson = Json.toJson(employee);
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(requestJson);
        Http.Context.current.set(new Http.Context(request));
        Result result = employeeController.addEmployee();

        assertEquals(OK, result.status());
        Response response = Json.fromJson(Json.parse(contentAsString(result)), Response.class);
        assertEquals(200, response.getStatus());
        assertEquals("Employee added to the DB successfully", response.getMessage());
        verify(mockEmployeeService, times(1)).addEmployee(any(Employee.class));
    }

    @Test
    public void testAddEmployeeFailure() {
        // Arrange
        Employee employee = new Employee("123", "John Doe", "Engineer", "john.doe@example.com");
        doThrow(new RuntimeException("Database error")).when(mockEmployeeService).addEmployee(any(Employee.class));

        JsonNode requestJson = Json.toJson(employee);
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(requestJson);

        Http.Context.current.set(new Http.Context(request));
        Result result = employeeController.addEmployee();

        // Assert
        assertEquals(BAD_REQUEST, result.status());
        Response response = Json.fromJson(Json.parse(contentAsString(result)), Response.class);
        assertEquals(500, response.getStatus());
        assertEquals("Database error", response.getErrorMessage());
        verify(mockEmployeeService, times(1)).addEmployee(any(Employee.class));
    }
    @Test
    public void testUpdateEmployeeSuccess() {
        String employeeId = "123";
        Employee updatedEmployee = new Employee(employeeId, "vamsi", "Senior Engineer", "vamsi@mail.com");
        doNothing().when(mockEmployeeService).updateEmployee("123", updatedEmployee);

        JsonNode requestJson = Json.toJson(updatedEmployee);
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .bodyJson(requestJson)
                .uri("/employees/" + employeeId);
        Http.Context.current.set(new Http.Context(request));
        Result result = employeeController.updateEmployee(employeeId);
        assertEquals(OK, result.status());
        Response response = Json.fromJson(Json.parse(contentAsString(result)), Response.class);
        assertEquals(200, response.getStatus());
        assertEquals("Employee Updated successfully", response.getMessage());
        verify(mockEmployeeService, atLeastOnce()).updateEmployee(eq(employeeId), any(Employee.class));
    }
//    @Test
//    public void testUpdateEmployeeNotFound() {
//        String employeeId = "123";
//        Employee updatedEmployee = new Employee(employeeId, "vamsi", "Senior Engineer", "vamsi@newemail.com");
//        doThrow(new RuntimeException("Employee not found")).when(mockEmployeeService).updateEmployee(eq(employeeId), any(Employee.class));
//
//        JsonNode requestJson = Json.toJson(updatedEmployee);
//        Http.RequestBuilder request = fakeRequest()
//                .method(PUT)
//                .bodyJson(requestJson)
//                .uri("/employees/" + employeeId);
//
//        Http.Context.current.set(new Http.Context(request));
//        Result result = employeeController.updateEmployee(employeeId);
//
//        assertEquals(BAD_REQUEST, result.status());
//        Response response = Json.fromJson(Json.parse(contentAsString(result)), Response.class);
//        assertEquals(500, response.getStatus());
//        assertEquals("Employee not found", response.getErrorMessage());
//        verify(mockEmployeeService, times(1)).updateEmployee(eq(employeeId), any(Employee.class));
//    }
}
