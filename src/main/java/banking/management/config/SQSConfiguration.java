package banking.management.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class SQSConfiguration {
    @Value("${MDS_SQS_URL}")
    private String sqsUrl;

    @Value("${AWS_ACCESS_KEY}")
    private String awsAccessKey;

    @Value("${AWS_SECRET_KEY}")
    private String awsSecretKey;

    @Value("${AWS_REGION}")
    private String awsRegion;

    private AWSCredentials awsCredentials(){
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }

    public AmazonSQS sqsclientBuilder(){
        return AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials())).
                withRegion(Regions.AP_SOUTH_1).build();
    }
    public String createSQSQueue(String queueName){
        AmazonSQS sqs = sqsclientBuilder();
//        queueName = "my-standard-queue";  // setting up here so to get the difference between FIFO and standard writing style
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        return  sqs.createQueue(createQueueRequest).getQueueUrl();

    }
    public void createFIFOqueue(String queueName){
        Map<String, String> queueAttributes = new HashMap<>();
        queueAttributes.put(QueueAttributeName.FifoQueue.toString(), "true");
        queueAttributes.put(QueueAttributeName.ContentBasedDeduplication.toString(), "true");  // Messages with identical content
        AmazonSQS sqs = sqsclientBuilder();                               //  sent within the 5-minute deduplication interval are treated as duplicates and only one copy is delivered
        queueName="FIFO_QUEUE";  // do I have to add .FIFO suffix to the queue name
        CreateQueueRequest createFifoQueueRequest = new CreateQueueRequest(queueName);
        createFifoQueueRequest.setAttributes(queueAttributes);
        String fifoqueueurl = sqs.createQueue(createFifoQueueRequest).getQueueUrl();
    }
    public String producer(String sqsrl,String message){
        AmazonSQS sqs = sqsclientBuilder();
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        MessageAttributeValue messageAttributeValue = new MessageAttributeValue();
        messageAttributeValue.setStringValue("This is an attribute");
        messageAttributeValue.setDataType("String");
        messageAttributes.put("AttributeOne", messageAttributeValue);
        SendMessageRequest sendMessageRequest = new SendMessageRequest(sqsrl, message).withDelaySeconds(30).withMessageAttributes(messageAttributes);
        SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);  // something to ask
        return sqs.sendMessage(sendMessageRequest).getMessageId();
    }

    public String producer(String message){
        AmazonSQS sqs = sqsclientBuilder();
//        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
//        MessageAttributeValue messageAttributeValue = new MessageAttributeValue();
//                messageAttributeValue.setStringValue("This is an attribute");
//                messageAttributeValue.setDataType("String");
//        messageAttributes.put("AttributeOne", messageAttributeValue);
        SendMessageRequest sendMessageRequest = new SendMessageRequest(sqsUrl, message).withDelaySeconds(30);
        SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);  // something to ask
        return sqs.sendMessage(sendMessageRequest).getMessageId();
    }
    public List<Message> consumer(){
        AmazonSQS sqs = sqsclientBuilder();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl).withWaitTimeSeconds(10).withMaxNumberOfMessages(10);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        for(Message message : messages){
            System.out.println(message.getBody());
            // when message is perfectly processed then dequeue
//            dequeue(message);
        }
        return messages;
    }
    public void dequeue(Message message){
        AmazonSQS sqs = sqsclientBuilder();
        sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(sqsUrl).withReceiptHandle(message.getReceiptHandle()));

    }
    public void deadletterqueue(){
        AmazonSQS sqs = sqsclientBuilder();
        CreateQueueRequest createDeadLetterQueueRequest = new  CreateQueueRequest();
        createDeadLetterQueueRequest.setQueueName("DEAD_LETTER_QUEUE_NAME");

        String deadLetterQueueUrl = sqs.createQueue(createDeadLetterQueueRequest).getQueueUrl();

        GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest();
        getQueueAttributesRequest.setQueueUrl(deadLetterQueueUrl);
        getQueueAttributesRequest.setAttributeNames(Collections.singleton(QueueAttributeName.QueueArn.toString()));

        GetQueueAttributesResult deadLetterQueueAttributes = sqs.getQueueAttributes(getQueueAttributesRequest);

//        set this newly created queue to be our original standard queue’s dead letter queue:

        Map<String, String> attributes = new HashMap<>();
        attributes.put(QueueAttributeName.RedrivePolicy.toString(), "{\"maxReceiveCount\":\"5\", \"deadLetterTargetArn\":\""
                + "deadLetterQueueARN" + "\"}");

        SetQueueAttributesRequest queueAttributesRequest = new SetQueueAttributesRequest();
        queueAttributesRequest.setQueueUrl(sqsUrl) ; //url should be of the one of whom this dead letter is made
        queueAttributesRequest.setAttributes(attributes);

        sqs.setQueueAttributes(queueAttributesRequest);

    }


//    Short polling (default) returns immediately, even if the message queue is empty.
//    Long polling doesn't return a response until a message arrives in the queue or the long poll times out.


//    Enabling Long Polling:
//    To enable long polling, set WaitTimeSeconds to a value greater than 0 (up to 20 seconds maximum).
//    This tells SQS to wait up to the specified number of seconds for messages to arrive in the queue before returning a response.


//    Benefits of Long Polling:
//    Reduces the number of empty responses when there are no messages available.
//    Eliminates false empty responses when messages are available but not included in the response.
//    Returns messages as soon as they become available, instead of waiting for the full timeout period.
}