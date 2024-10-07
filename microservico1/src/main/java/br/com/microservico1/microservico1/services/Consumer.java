package br.com.microservico1.microservico1.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.microservico1.microservico1.annotations.QueueListenerAnnotation;
import br.com.microservico1.microservico1.model.Pagamento;
import br.com.microservico1.microservico1.model.dto.PagamentoDTO;
import br.com.microservico1.microservico1.model.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

    @Value("${sqsQueueName}")
    private String nomeFila;

    private final ObjectMapper objectMapper; 
    private final PagamentoRepository pagamentoRepository;

    @QueueListenerAnnotation(value = "${sqsQueueName}", pollTimeoutSeconds="5")
    public Object consumer(List<String> listPagamentoJson){

        log.info("Teste annotation  --- lista pagamento "+ listPagamentoJson);
        List<Pagamento> listPagamentos = listPagamentoJson.stream()
        .map(pagamentoJson -> convertJson(pagamentoJson))       
        .map(pagamentoDto -> this.convertDtoToEntity(pagamentoDto))
        .toList();
        log.info("Lista de pagamentos "+listPagamentos);
        
        // listPagamentoJson.stream().forEach( pagamento -> this.pagamentoRepository.save(pagamento));

        return listPagamentoJson;
    }

    private Pagamento convertDtoToEntity(PagamentoDTO pagamentoDTO) {
        if (pagamentoDTO == null) return null;
        Pagamento pagamento = new Pagamento();
        pagamento.setDescricao(pagamentoDTO.getDescricao());
        pagamento.setValor(pagamentoDTO.getValor());
        return pagamento;
    }

    private PagamentoDTO convertJson(String pagamentoJson){
        try {
            return objectMapper.readValue(pagamentoJson, PagamentoDTO.class);
        } catch (Exception e) {
            log.error("Erro ao converter JSON para Pagamento: " + e.getMessage());
            return null;
        }
    }

}
