package banking.management.service;

import banking.management.model.DocVerification;
import org.springframework.web.multipart.MultipartFile;

public interface DocVerificationService {
    DocVerification uploadFile(DocVerification docVerification, MultipartFile file);
}
