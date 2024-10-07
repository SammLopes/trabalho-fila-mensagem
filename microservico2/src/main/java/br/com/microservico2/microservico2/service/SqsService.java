package br.com.microservico2.microservico2.service;

import java.util.List;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.microservico2.microservico2.model.Pagamento;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.annotation.PostConstruct;
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
@Slf4j
@RequiredArgsConstructor
public class SqsService{

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper; 
    private final RestTemplate restTemplate;

    @Value("${sqsQueueName}")
    private String nomeFila ;

    public void enviarMensagemFila(Pagamento pagamento){
        try {
            String menssagem = objectMapper.writeValueAsString(pagamento);
            log.info("Menssagem a ser enviada " +menssagem);
            sqsTemplate.send(nomeFila, menssagem);
            
        } catch (JsonProcessingException  e) {
            e.printStackTrace();
        }
    }

    // @SqsListener("${sqsQueueName}")
    public void consumirMensageFila(String pagamentoJson){

        log.info("Pagamento Recebido " + pagamentoJson);
        Pagamento pagamento = this.convertJson(pagamentoJson);
        this.processarMensagem(pagamento);
    
    }

    private Pagamento convertJson(String pagamentoJson){
        try {
            Pagamento pagamento = objectMapper.readValue(pagamentoJson, Pagamento.class);
            return pagamento;
        } catch (Exception e) {
            log.error("Erro ao converter JSON para Pagamento: " + e.getMessage());
            return null;
        }
    }

    private void processarMensagem(Pagamento pagamento){

         log.info("Mensagem da fila antes de ser processada pagamento: " + pagamento.getValor() + " Valor "+pagamento.getDescricao());
         this.enviarMensagemMricroservico1(pagamento);
         log.info("Mensagem da fila depois de ser processada pagamento");
    
    }

    private void enviarMensagemMricroservico1(Pagamento pagamento){
        String microservico1Url = "http://microservice1:8080/m1/resposta/";
        
        try {
           ResponseEntity<String> response = restTemplate.postForEntity(microservico1Url, pagamento, String.class);
           log.info("Teste resposta "+ response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}    

