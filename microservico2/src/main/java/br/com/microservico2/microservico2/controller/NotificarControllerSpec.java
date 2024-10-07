package br.com.microservico2.microservico2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.microservico2.microservico2.model.Pagamento;

@RequestMapping("/notificar")
public interface NotificarControllerSpec {

    @PostMapping("/")
    public ResponseEntity<Object> notificar(@RequestBody Pagamento request);

}
