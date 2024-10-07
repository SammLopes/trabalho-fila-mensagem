package br.com.microservico2.microservico2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservico2.microservico2.model.Pagamento;
import br.com.microservico2.microservico2.service.SqsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Slf4j
@RequiredArgsConstructor
public class NotificarControllerImpl implements NotificarControllerSpec{
    
    private final SqsService sqsService;

    @PostMapping("/")
    public ResponseEntity<Object> notificar(@RequestBody Pagamento request) {
        log.info("Mensagem recebida no endpoint");
        this.sqsService.enviarMensagemFila(request);
        log.info("Mensagem enviada ");
        return  ResponseEntity.ok("Notificação enviada para o SQS.");
    }
    

}
