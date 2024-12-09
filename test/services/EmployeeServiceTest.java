package services;

import Models.Employee;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import db.Mongomanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Mock
    private Mongomanager mockMongoManager;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(mockMongoManager);
    }

    @Test
    public void testGetEmployeeById() {
        String id = "123";
        Employee employee = new Employee(id, "John Doe", "Engineer", "john.doe@example.com");
        when(mockMongoManager.getEmployeeById(id)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(id);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    public void testAddEmployee() {
        Employee employee = new Employee("123", "John Doe", "Engineer", "john.doe@example.com");
        doNothing().when(mockMongoManager).addEmployee(employee);

        employeeService.addEmployee(employee);

        verify(mockMongoManager, times(1)).addEmployee(employee);
    }

    @Test(expected = RuntimeException.class)
    public void testAddEmployeeThrowsException() {
        Employee employee = new Employee("123", "John Doe", "Engineer", "john.doe@example.com");
        doThrow(new RuntimeException("Database error")).when(mockMongoManager).addEmployee(employee);

        employeeService.addEmployee(employee);
    }

    @Test
    public void testDeleteEmployee() {
        String id = "123";
        when(mockMongoManager.deleteEmployee(id)).thenReturn(true);

        boolean result = employeeService.deleteEmployee(id);

        assertTrue(result);
        verify(mockMongoManager, times(1)).deleteEmployee(id);
    }

    @Test
    public void testGetEmployeesByName() {
        String name = "John Doe";
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("123", name, "Engineer", "john.doe@example.com"));
        when(mockMongoManager.getByName(name)).thenReturn(employees);

        List<Employee> result = employeeService.getEmployeesByName(name);

        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }

    @Test
    public void testGetEmployeesWithPagination() {
        int page = 1, size = 10;
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("123", "John Doe", "Engineer", "john.doe@example.com"));
        when(mockMongoManager.getEmployeesWithPagination(page, size)).thenReturn(employees);

        List<Employee> result = employeeService.getEmployeesWithPagination(page, size);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    public void testGetEmployeeCount() {
        long count = 100L;
        when(mockMongoManager.getEmployeeCount()).thenReturn(count);

        long result = employeeService.getEmployeeCount();

        assertEquals(count, result);
    }

    @Test
    public void testSearchEmployees() {
        String query = "Engineer";
        int page = 1, size = 10;
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("123", "John Doe", "Engineer", "john.doe@example.com"));
        when(mockMongoManager.searchEmployees(query, page, size)).thenReturn(employees);

        List<Employee> result = employeeService.searchEmployees(query, page, size);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }
    @Test
    public void testGetSearchResultCount() {
        String query = "Engineer";
        long count = 50L;
        when(mockMongoManager.getSearchResultCount(query)).thenReturn(count);

        long result = employeeService.getSearchResultCount(query);

        assertEquals(count, result);
    }
    @Test
    public void testUpdateEmployee_Success() {
        String id = "123";
        Employee updatedEmployee = new Employee(id, "Jane Doe", "Manager", "jane.doe@example.com");
        doNothing().when(mockMongoManager).updateEmployee(id, updatedEmployee);
        employeeService.updateEmployee(id, updatedEmployee);
        verify(mockMongoManager, times(1)).updateEmployee(id, updatedEmployee);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateEmployee_ThrowsException() {
        String id = "123";
        Employee updatedEmployee = new Employee(id, "Jane Doe", "Manager", "jane.doe@example.com");
        doThrow(new RuntimeException("Database error")).when(mockMongoManager).updateEmployee(id, updatedEmployee);
        employeeService.updateEmployee(id, updatedEmployee);
    }


}
