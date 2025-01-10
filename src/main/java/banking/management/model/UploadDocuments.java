package banking.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Document(indexName = "upload_documents")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String userName;

    private Long accountId;

    private Map<String,String> documentNameAndId;
}
