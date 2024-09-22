package br.com.microservico2.microservico2.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.microservico2.microservico2.model.Pagamento;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;

@Service
@RequiredArgsConstructor
public class SqsService{

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper; 
    private final String nomeFila = "fila-sqs";

    public void enviarMensagemFila(Pagamento pagamento){
        
        // String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName("fila-sqs").build()).queueUrl();
        String filaUrl ;
        
         try {
            filaUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(nomeFila).build()).queueUrl();
        } catch (QueueDoesNotExistException e) {

            criarFila(nomeFila);
            filaUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(nomeFila).build()).queueUrl();
        }
        System.out.println("URL da fila criada ou caso ela exista: " + filaUrl);
        try {

            String menssagem = objectMapper.writeValueAsString(pagamento);
            System.out.println("Menssagem a ser enviada " +menssagem);
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(filaUrl)
                    .messageBody(menssagem)
                    .build());
            
        } catch (JsonProcessingException  e) {
            e.printStackTrace();
        }
    }
    //"http://sqs.sa-east-1.localhost.localstack.cloud:4566/000000000000/fila-sqs"
    //fila-sqs
    public void criarFila(String nomeFila) {
        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName(nomeFila)
                .build();
    
        CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
        System.out.println("Fila criada: " + createQueueResponse.queueUrl());
    }

}    

