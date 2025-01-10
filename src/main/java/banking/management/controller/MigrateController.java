package banking.management.controller;

import banking.management.service.MigrateService;
import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch.snapshot.CreateSnapshotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class MigrateController {
    @Autowired
    private MigrateService migrateService;

    @PostMapping("/migrate")
    public void migrate() throws IOException {
        migrateService.migrateData();
    }

    @GetMapping("/byBranch")
    public Object byBranch(@RequestParam String branch){
       return migrateService.findByBranch(branch);
    }

    @GetMapping("/getDocumentsMigrate")
    public Object getAllDocuments(@RequestParam String keyword) throws IOException {
        return migrateService.getDocumentsMigrate(keyword);
    }

    @GetMapping("/createIndex")
    public void createIndex(@RequestParam String keyword) throws IOException {
        migrateService.createIndexWithSettingsAndMappings(keyword);
    }

    @PostMapping("/reIndex")
    public void reIndex(@RequestParam(name = "src") String sourceIndex,@RequestParam(name = "des") String destIndex) throws IOException {
        migrateService.reindex(sourceIndex, destIndex);
    }

    @PostMapping("/create")
    public CreateSnapshotResponse createSnapshot(@RequestParam String repositoryName, @RequestParam String snapshotName) throws Exception {
        return migrateService.createSnapshot(repositoryName,snapshotName);
    }
}
