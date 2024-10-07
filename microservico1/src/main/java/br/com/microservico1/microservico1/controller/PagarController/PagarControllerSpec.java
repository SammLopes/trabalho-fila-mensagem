package br.com.microservico1.microservico1.controller.PagarController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.microservico1.microservico1.model.Pagamento;

@RequestMapping("/pagar")
public interface PagarControllerSpec {

    @PostMapping("/")
    public ResponseEntity<Object> pagar(@RequestBody Pagamento pagamento);
}
