//package banking.management.service;
//
//import banking.management.model.DocVerification;
//import banking.management.repository.DocVerificationRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Service
//public class DocVerificationServiceImpl {
//
//    @Autowired
//    private DocVerificationRepository docVerificationRepository;
//
//    private final Path fileStorageLocation = Paths.get("path/to/upload/directory").toAbsolutePath().normalize();
//
//    @Override
//    @Transactional
//    public DocVerification uploadFile(DocVerification docVerification, MultipartFile file) {
//
//        try {
//            DocVerification savedDocVerification = docVerificationRepository.save(docVerification);
//
//            return savedDocVerification;
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
//        }
//    }
//    }
//
//}
