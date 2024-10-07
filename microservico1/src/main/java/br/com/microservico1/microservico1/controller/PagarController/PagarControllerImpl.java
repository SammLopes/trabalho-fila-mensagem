package br.com.microservico1.microservico1.controller.PagarController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import br.com.microservico1.microservico1.model.Pagamento;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class PagarControllerImpl implements PagarControllerSpec {
    
    private final RestTemplate restTemplate;

    public ResponseEntity<Object> pagar(@RequestBody Pagamento pagamento){

        String urlM2 = "http://microservice2:8081/m2/notificar/";
        System.out.println("Teste valores de pagamento " + pagamento);
        ResponseEntity<String> response = restTemplate.postForEntity(urlM2 ,pagamento, String.class);
        System.out.printf("Teste endpoint pagar ", response.getBody());
        return ResponseEntity.ok("Pagamento processado e notificação enviada: Descrição " + pagamento.getDescricao()+ " Valor " + pagamento.getValor());

    }

}
