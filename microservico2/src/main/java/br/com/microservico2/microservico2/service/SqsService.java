package br.com.microservico2.microservico2.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.microservico2.microservico2.model.Pagamento;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

@Service
@RequiredArgsConstructor
public class SqsService{

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper; 
    private final RestTemplate restTemplate;
    private final String nomeFila = "fila-sqs";

    public void enviarMensagemFila(Pagamento pagamento){
       String filaUrl ;
       filaUrl = this.criaFilaSeExistir(nomeFila);
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
   
    public void criarFila(String nomeFila) {
        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName(nomeFila)
                .build();
    
        CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
        System.out.println("Fila criada: " + createQueueResponse.queueUrl());
    }

    @Scheduled(fixedRate = 5000)
    public void consumirMensageFila(){
        String filaUrl = this.criaFilaSeExistir(nomeFila);

        ReceiveMessageRequest receberSolicitacaoMensagem = ReceiveMessageRequest.builder()
                .queueUrl(filaUrl)
                .maxNumberOfMessages(1)
                .waitTimeSeconds(10)
                .build();

        List<Message> messagensRecebidas = sqsClient.receiveMessage(receberSolicitacaoMensagem).messages();
        
        System.out.printf("Messagem da Fila: %s%n" ,messagensRecebidas);
        
        if(messagensRecebidas.size() == 0){
            return;
        }

        this.processarMensagem(messagensRecebidas, filaUrl);
    }

    private void processarMensagem(List<Message> messagensRecebidas ,String filaUrl){
        for (Message mensagem : messagensRecebidas) {
            try {
                Pagamento pagamento = objectMapper.readValue(mensagem.body(), Pagamento.class);
                System.out.println("Mensagem da fila antes de ser processada pagamento: " + pagamento.getValor() + " Valor "+pagamento.getDescricao());
                this.enviarMensagemMricroservico1(pagamento);
                System.out.println("Mensagem da fila depois de ser processada pagamento");
                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(filaUrl)
                        .receiptHandle(mensagem.receiptHandle())
                        .build());

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarMensagemMricroservico1(Pagamento pagamento){
        String microservico1Url = "http://microservice1:8080/m1/resposta/";
        
        try {
           ResponseEntity<String> response = restTemplate.postForEntity(microservico1Url, pagamento, String.class);
           System.out.println("Teste resposta "+ response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String criaFilaSeExistir(String nomeFila){
        String filaUrl;
        try {
         
            filaUrl = this.buscarUrlFila(nomeFila);
            System.out.println("Fila já existe -> "+ filaUrl);
           return filaUrl;
        } catch (QueueDoesNotExistException e) {

            criarFila(nomeFila);
            filaUrl = this.buscarUrlFila(nomeFila);
            System.out.println("Fila não existe / fila criada -> "+ filaUrl);
            return filaUrl;
        }
    }

    private String buscarUrlFila(String nomeFila){
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(nomeFila).build()).queueUrl();
    }


}    

