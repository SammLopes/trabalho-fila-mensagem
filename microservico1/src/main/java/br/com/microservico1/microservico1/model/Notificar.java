package br.com.microservico1.microservico1.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificar {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    private boolean notificado = false;
   
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "notificar")
    private List<Pagamento> veiculos;

}   
