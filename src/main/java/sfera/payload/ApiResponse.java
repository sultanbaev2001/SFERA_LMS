package sfera.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String message;
    private boolean success;
    private HttpStatus status;
    private Object body;

    public ApiResponse(String message, HttpStatus status){
        this.message = message;
        this.status = status;
        this.success = false;
    }

    public ApiResponse(String message, HttpStatus status, Object body){
        this.message = message;
        this.status = status;
        this.body = body;
        this.success = true;
    }
}
