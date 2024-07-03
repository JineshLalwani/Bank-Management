package banking.management.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseMeta {
    @JsonProperty("isSuccess")
    private boolean isSuccess;
    @JsonProperty("statusCode")
    private HttpStatus statusCode;
    //    @JsonProperty("responseMessage")
//    private String responseMessage;
    @JsonProperty("displayMessage")
    private String displayMessage;
//    @JsonProperty("error")
//    private ApiError error;
}
