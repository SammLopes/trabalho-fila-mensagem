package br.com.microservico1.microservico1.model.repository;

import br.com.microservico1.microservico1.model.Notificar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificarRepository extends JpaRepository<Notificar, Long> {
}