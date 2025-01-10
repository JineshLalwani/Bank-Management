package banking.management.controller;

import banking.management.model.UploadDocuments;
import banking.management.service.UploadDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/uploadDocuments")
public class UploadDocumentsController {
    @Autowired
    UploadDocumentsService uploadDocumentsService;

    @PostMapping("/saveDocuments")
    public UploadDocuments createPayment(@RequestBody UploadDocuments uploadDocuments) {
        return uploadDocumentsService.saveDocuments(uploadDocuments);
    }

    @GetMapping("/getDocuments")
    public Object getAllDocuments(@RequestParam String keyword) throws IOException {
        if(Objects.equals(keyword, "")){
            return uploadDocumentsService.getDocuments();
        }
        else {
            return uploadDocumentsService.getDocuments(keyword);
        }
    }

}
