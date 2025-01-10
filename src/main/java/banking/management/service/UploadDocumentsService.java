package banking.management.service;

import banking.management.model.UploadDocuments;

import java.io.IOException;
import java.util.List;

public interface UploadDocumentsService {
    UploadDocuments saveDocuments(UploadDocuments uploadDocuments);

    Object getDocuments(String keyword) throws IOException;

    Object getDocuments();
}
