package br.com.microservico1.microservico1.controller.RespostaController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.microservico1.microservico1.model.Pagamento;

@RequestMapping("/resposta")
public interface RespostaControllerSpec {
    
    @PostMapping("/")
    public ResponseEntity<Object> resposta();
}
