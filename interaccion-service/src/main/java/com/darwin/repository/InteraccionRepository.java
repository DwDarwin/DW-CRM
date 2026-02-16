package com.darwin.repository;

import com.darwin.entity.Interaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InteraccionRepository extends JpaRepository<Interaccion, Long> {

    List<Interaccion> findByContactoId(Long contactoId);

    List<Interaccion> findByFechaSeguimientoBefore(LocalDateTime fecha);
}
