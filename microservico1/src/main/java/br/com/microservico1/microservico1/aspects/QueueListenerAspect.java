package br.com.microservico1.microservico1.aspects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import br.com.microservico1.microservico1.annotations.QueueListenerAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class QueueListenerAspect {
    
    private final SqsClient sqsClient;
    private final Environment env;


    @Around("@annotation(queueListenerAnnotation)")
    public Object processarFila(ProceedingJoinPoint joinPoint, QueueListenerAnnotation queueListenerAnnotation) throws Throwable{

        String[] value = Arrays.stream(queueListenerAnnotation.value())
        .map(env::resolvePlaceholders)
        .toArray(String[]::new);
        String pollTimeout = queueListenerAnnotation.pollTimeoutSeconds();
        String maxMessages = queueListenerAnnotation.maxMessagesPerPoll();
        String visibilityTimeout = queueListenerAnnotation.messageVisibilitySeconds();

        log.info("Value: " + Arrays.toString(value));
        log.info("Poll Timeout: " + pollTimeout);
        log.info("Max Messages per Poll: " + maxMessages);
        log.info("Message Visibility Timeout: " + visibilityTimeout);

        log.info("Informações  valor: " + value[0].toString() + " tempos de espera em um poll: "+ pollTimeout+" maximo de messagens: "+maxMessages +" Tempo de visibilidade: "+visibilityTimeout );
        
        String queueUrl = this.criaFilaSeExistir(value[0]);
        Object result = new Object();
        while(true){
            List<Message> messagesConsumedMessage = this.consumirMensageFila(queueUrl, maxMessages, visibilityTimeout, pollTimeout);
            
            if(messagesConsumedMessage.isEmpty()){
                log.info("Nenhuma mensagem encontrada na fila. Aguardando...");
                try{
                    Thread.sleep(Long.parseLong(pollTimeout) * 1000);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }
         
            Object[] messagesConsumed = new Object[] {
                messagesConsumedMessage.stream().map(Message::body).collect(Collectors.toList())
            };
    
            // log.info("Teste apos executar o metodo "+ messagesConsumed.length);
            result = joinPoint.proceed( messagesConsumed);
            messagesConsumedMessage.stream()
            .map(Message::receiptHandle)
            .forEach(receiptHandle -> this.deleteMessage(queueUrl , receiptHandle));
          
        }
        return result;
    }
    
    private void deleteMessage(String urlQueue , String receiptHandle){
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
        .queueUrl(urlQueue)
        .receiptHandle(receiptHandle)
        .build();

        sqsClient.deleteMessage(deleteMessageRequest);
        log.info("Mensagem deletada com sucesso: " + receiptHandle);
    }
    
    //@Scheduled(fixedRate = 5000)
    private List<Message> consumirMensageFila(String nameQueue, String maxNumber, String visibilityTimeout, String pollTimeout){

        ReceiveMessageRequest receberSolicitacaoMensagem = this.buildReceiveMessageRequest(nameQueue, maxNumber, visibilityTimeout, pollTimeout);
        log.info("Solicitação "+ receberSolicitacaoMensagem.toString());
        List<Message> messagensRecebidas = sqsClient.receiveMessage(receberSolicitacaoMensagem).messages();
        
        System.out.printf("Messagem da Fila: %s%n" ,messagensRecebidas);
        
        if(messagensRecebidas.size() == 0){
            return new ArrayList<>();
        }

        return messagensRecebidas;
    }

    private ReceiveMessageRequest buildReceiveMessageRequest(String nameQueue, String maxNumber, String visibilityTimeout, String pollTimeout) {

        ReceiveMessageRequest.Builder builder = ReceiveMessageRequest.builder()
                .queueUrl(nameQueue);
        
        if (maxNumber != null && !maxNumber.isEmpty()) {
            builder.maxNumberOfMessages(Integer.parseInt(maxNumber));
        } else {
            builder.maxNumberOfMessages(1); 
        }
    
        if (visibilityTimeout != null && !visibilityTimeout.isEmpty()) {
            builder.visibilityTimeout(Integer.parseInt(visibilityTimeout));
        } else {
            builder.visibilityTimeout(30); 
        }
    
        if (pollTimeout != null && !pollTimeout.isEmpty()) {
            builder.waitTimeSeconds(Integer.parseInt(pollTimeout)); 
        } else {
            builder.waitTimeSeconds(0); 
        }
    
        return builder.build();
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

    private void criarFila(String nomeFila) {
        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName(nomeFila)
                .build();
    
        CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
        System.out.println("Fila criada: " + createQueueResponse.queueUrl());
    }

    private String buscarUrlFila(String nomeFila){
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(nomeFila).build()).queueUrl();
    }
}
