package br.com.microservico1.microservico1.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Pagamento {
    private Double valor;
    private String descricao;
}
