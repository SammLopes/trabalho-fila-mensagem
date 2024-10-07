package br.com.microservico1.microservico1.controller.RespostaController;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import br.com.microservico1.microservico1.model.Pagamento;
import br.com.microservico1.microservico1.services.Consumer;

import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class RespostaControllerImpl implements RespostaControllerSpec{
    
    private final Consumer consumer;

    public ResponseEntity<Object> resposta(){

    
        Pagamento pagamento = new Pagamento();
        System.out.println("Pagamento recebido: Descrição " + pagamento.toString());

    
        return ResponseEntity.ok("Pagamento recebido: Descrição " + pagamento.getDescricao()+ " Valor " + pagamento.getValor());

    }
}
