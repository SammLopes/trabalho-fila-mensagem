package br.com.microservico2.microservico2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservico2.microservico2.model.Pagamento;
import br.com.microservico2.microservico2.service.SqsService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/notificar")
@RequiredArgsConstructor
public class NotificarController {
    
    private final SqsService sqsService;

    @PostMapping("/")
    public ResponseEntity<Object> postMethodName(@RequestBody Pagamento request) {
        
        this.sqsService.enviarMensagemFila(request);
        System.out.println("Mensagem enviada ");
        return  ResponseEntity.ok("Notificação enviada para o SQS.");
    }
    

}
