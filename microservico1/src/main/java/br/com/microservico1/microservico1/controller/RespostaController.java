package br.com.microservico1.microservico1.controller;


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
@RequestMapping("/resposta")
@RequiredArgsConstructor
public class RespostaController {
    
    private final RestTemplate restTemplate;

    @PostMapping("/")
    public ResponseEntity<Object> resposta(@RequestBody Pagamento pagamento){

        System.out.println("Pagamento recebido: Descrição " + pagamento.toString());

        return ResponseEntity.ok("Pagamento recebido: Descrição " + pagamento.getDescricao()+ " Valor " + pagamento.getValor());

    }
}
