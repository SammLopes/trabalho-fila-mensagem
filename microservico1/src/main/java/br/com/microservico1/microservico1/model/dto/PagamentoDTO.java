package br.com.microservico1.microservico1.model.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Component
@Data
public class PagamentoDTO {

    private Double valor;
    private String descricao;
}
