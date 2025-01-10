package banking.management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Data
public class DocVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long docVerificationId;

    @JsonProperty("documents")
    private List<FileDetails> documents;

    @JsonProperty("ticketDetails")
    private JsonNode ticketDetails;

    @JsonProperty("contactDetails")
    private JsonNode contactDetails;

    @JsonProperty("isAccountHolder")
    private Boolean isAccountHolder;

    @JsonProperty("aadharNo")
    private String aadharNo;

    @Data
    public static class FileDetails {
        private String fileName;
        private long fileSize;
        private byte[] fileContent;

        public FileDetails(MultipartFile file) throws IOException {
            this.fileName = file.getOriginalFilename();
            this.fileSize = file.getSize();
            InputStream inputStream = file.getInputStream();
            this.fileContent = inputStream.readAllBytes();

        }
    }
}
