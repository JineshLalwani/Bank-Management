package banking.management.controller;

import banking.management.config.SQSConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UniController {

    @Autowired
    private SQSConfiguration sqsConfiguration;

    @GetMapping("/msg")
    public String amazonsqs(@RequestParam String msg){
          //sqsConfiguration.producer("Jinesh queue");
           return sqsConfiguration.producer(sqsConfiguration.createSQSQueue("Jinesh_queue"),msg);
        //return sqsConfiguration.consumer();


    }
}