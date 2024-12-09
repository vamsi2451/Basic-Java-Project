package Models;
import lombok.Data;

@Data

public class Response {
    private int status;
    private Object data;
    private String errorMessage;
    private String message;
    private  long total;
}


