package br.com.microservico2.microservico2.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Pagamento {
    private Double valor;
    private String descricao;
}    


