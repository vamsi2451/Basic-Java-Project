package db;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import Models.Employee;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.sun.jna.platform.win32.DsGetDC;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.verification.AtLeast;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MongomanagerTest {

    private Mongomanager mongoManager;

    @Mock
    private MongoCollection<Document> mockEmployeeCollection;

    @Mock
    private MongoDatabase mockDatabase;
    private Employee testEmployee;
    private Document testEmployeeDoc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mongoManager = new Mongomanager(mockEmployeeCollection);
//        mongoManager.database = mockDatabase;

        testEmployee = new Employee("123", "John Doe", "johndoe@mail.com", "Developer");
        testEmployeeDoc = new Document("id", testEmployee.getId())
                .append("name", testEmployee.getName())
                .append("mail", testEmployee.getMail())
                .append("role", testEmployee.getRole());
    }

    @Test
    public void testAddEmployee_Success() {
        FindIterable<Document> findIterable = mock(FindIterable.class);
        when(mockEmployeeCollection.find(any(Document.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);

        mongoManager.addEmployee(testEmployee);

        verify(mockEmployeeCollection, times(1)).insertOne(testEmployee.toDocument());
    }

    @Test(expected = RuntimeException.class)
    public void testAddEmployee_ExistingEmployee_ThrowsException() {
        // Mock behavior for checking if the employee already exists
        when(mockEmployeeCollection.find(new Document("mail", testEmployee.getMail())).first()).thenReturn(testEmployeeDoc);

        // Call the method under test, it should throw a RuntimeException
        mongoManager.addEmployee(testEmployee);
    }

    @Test
    public void testUpdateEmployee() {
        // Arrange: Prepare the updated employee data and corresponding document
        Employee updatedEmployee = new Employee("123", "John Updated", "johnupdated@mail.com", "Senior Developer");
        Document updateDoc = updatedEmployee.toDocument();

        mongoManager.updateEmployee("123", updatedEmployee);
        verify(mockEmployeeCollection, times(1))
                .updateOne(new Document("id", "123"), new Document("$set", updateDoc));
    }

    @Test
    public void testDeleteEmployee_Success1() {
        // Arrange: Mock the DeleteResult and set up its behavior
        DeleteResult mockDeleteResult = mock(DeleteResult.class);
        when(mockDeleteResult.getDeletedCount()).thenReturn(1L);
        when(mockEmployeeCollection.deleteOne(new Document("id", "123"))).thenReturn(mockDeleteResult);

        boolean result = mongoManager.deleteEmployee("123");

        // Assert: Verify interactions and return value
        verify(mockEmployeeCollection, times(1)).deleteOne(new Document("id", "123"));
        assertTrue(result); // Assert that the method returns true
    }

    @Test
    public void testDeleteEmployee_Failure() {
        // Arrange: Mock the DeleteResult and set up its behavior
        DeleteResult mockDeleteResult = mock(DeleteResult.class);
        when(mockDeleteResult.getDeletedCount()).thenReturn(0L); // Simulate no deletion
        when(mockEmployeeCollection.deleteOne(new Document("id", "123"))).thenReturn(mockDeleteResult);

        // Act: Call the deleteEmployee method
        boolean result = mongoManager.deleteEmployee("123");

        // Assert: Verify interactions and return value
        verify(mockEmployeeCollection, times(1)).deleteOne(new Document("id", "123"));
        assertFalse(result); // Assert that the method returns false
    }

//    @Test
//    public void testGetEmployeesWithPagination() {
//        List<Document> mockDocuments = List.of(
//                new Document("id", "123").append("name", "John Doe").append("mail", "johndoe@mail.com").append("role", "Developer"),
//                new Document("id", "124").append("name", "Jane Doe").append("mail", "janedoe@mail.com").append("role", "Tester")
//        );
//        when(mockEmployeeCollection.find().skip(anyInt()).limit(anyInt())).thenReturn(mockDocuments);
//
//        List<Employee> result = mongoManager.getEmployeesWithPagination(1, 2);
//
//        assertEquals(2, result.size());
//        assertEquals("John Doe", result.get(0).getName());
//        assertEquals("Jane Doe", result.get(1).getName());
//    }

    @Test
    public void testGetEmployeeCount() {
        when(mockEmployeeCollection.countDocuments()).thenReturn(10L);

        long result = mongoManager.getEmployeeCount();

        assertEquals(10L, result);
        verify(mockEmployeeCollection, times(1)).countDocuments();
    }

//    @Test
//    public void testSearchEmployees() {
//        // Arrange
//        String query = "Developer";
//        int page = 1;
//        int size = 2;
//        int skip = (page - 1) * size;
//
//        List<Document> mockDocuments = Arrays.asList(
//                new Document("id", "123").append("name", "John Doe").append("mail", "johndoe@mail.com").append("role", "Developer"),
//                new Document("id", "124").append("name", "Jane Doe").append("mail", "janedoe@mail.com").append("role", "Developer")
//        );
//
//
//        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
//        when(mockFindIterable.skip(skip)).thenReturn(mockFindIterable);
//        when(mockFindIterable.limit(size)).thenReturn(mockFindIterable);
//        when(mockFindIterable.iterator()).thenReturn(mockDocuments.iterator());
//        when(mockEmployeeCollection.find(
//                Filters.or(
//                        Filters.regex("name", query, "i"),
//                        Filters.regex("role", query, "i"),
//                        Filters.regex("mail", query, "i")
//                )
//        )).thenReturn(mockFindIterable);
//
//        // Act
//        List<Employee> result = mongoManager.searchEmployees(query, page, size);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("John Doe", result.get(0).getName());
//        assertEquals("Jane Doe", result.get(1).getName());
//    }

    @Test
    public void testGetEmployeesWithPagination() {
        // Arrange
        int page = 1;
        int size = 2;
        int skip = (page - 1) * size;

        // Create mock documents
        Document doc1 = new Document("id", "123").append("name", "John Doe").append("mail", "johndoe@mail.com").append("role", "Developer");
        Document doc2 = new Document("id", "124").append("name", "Jane Doe").append("mail", "janedoe@mail.com").append("role", "Tester");

        // Mock MongoCursor
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);
        when(mockCursor.hasNext()).thenReturn(true, true, false);
        when(mockCursor.next()).thenReturn(doc1, doc2);

        // Mock FindIterable
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockFindIterable.skip(skip)).thenReturn(mockFindIterable);
        when(mockFindIterable.limit(size)).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        // Mock employeeCollection.find()
        when(mockEmployeeCollection.find()).thenReturn(mockFindIterable);

        // Act
        List<Employee> result = mongoManager.getEmployeesWithPagination(page, size);

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());

        // Verify interactions
        verify(mockEmployeeCollection, times(1)).find();
        verify(mockFindIterable, times(1)).skip(skip);
        verify(mockFindIterable, times(1)).limit(size);
        verify(mockCursor, times(3)).hasNext();
        verify(mockCursor, times(2)).next();
    }

    @Test
    public void testSearchEmployees() {
        // Arrange
        String query = "Developer";
        int page = 1;
        int size = 2;
        int skip = (page - 1) * size;

        // Create mock documents
        Document doc1 = new Document("id", "123").append("name", "John Doe").append("mail", "johndoe@mail.com").append("role", "Developer");
        Document doc2 = new Document("id", "124").append("name", "Jane Doe").append("mail", "janedoe@mail.com").append("role", "Tester");

        // Mock MongoCursor
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);
        when(mockCursor.hasNext()).thenReturn(true, true, false);
        when(mockCursor.next()).thenReturn(doc1, doc2);

        // Mock FindIterable
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockFindIterable.skip(skip)).thenReturn(mockFindIterable);
        when(mockFindIterable.limit(size)).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        // Mock employeeCollection.find()
        when(mockEmployeeCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        // Act
        List<Employee> result = mongoManager.searchEmployees(query, page, size);

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());

        // Verify interactions
        verify(mockEmployeeCollection, times(1)).find(any(Bson.class));
        verify(mockFindIterable, times(1)).skip(skip);
        verify(mockFindIterable, times(1)).limit(size);
        verify(mockCursor, times(3)).hasNext(); // Twice true, once false
        verify(mockCursor, times(2)).next(); // Two documents retrieved
    }

}
