package banking.management.controller;

import banking.management.model.DocVerification;
import banking.management.response.APIResponse;
import banking.management.response.ResponseMeta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/docVerification")
public class DocVerificationController {

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<DocVerification>> uploadFile(@RequestPart("ticketDetails") String t,
                                                                   @RequestPart("contactDetails") String a,
                                                                   @RequestPart List<MultipartFile> documents,
                                                                   @RequestParam("isAccountHolder") String b,
                                                                   @RequestParam("aadharNo") String c) throws IOException {
        ResponseMeta meta = new ResponseMeta();
        DocVerification docVerification = new DocVerification();
        APIResponse<DocVerification> apiResponse = new APIResponse<>();

        List<DocVerification.FileDetails> fileDetailsList = new ArrayList<>();
        for (MultipartFile file : documents) {
            fileDetailsList.add(new DocVerification.FileDetails(file));
        }

        docVerification.setDocuments(fileDetailsList);
        docVerification.setAadharNo(c);
        docVerification.setContactDetails(objectMapper.readTree(a));
        docVerification.setTicketDetails(objectMapper.readTree(t));
        docVerification.setIsAccountHolder(Boolean.parseBoolean(b));

        meta.setSuccess(true);
        meta.setStatusCode(HttpStatus.CREATED);
        meta.setDisplayMessage("DocVerification created successfully");

        apiResponse.setMeta(meta);
        apiResponse.setData(docVerification);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
