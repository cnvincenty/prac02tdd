package bo.edu.uagrm.soe.prac02tdd.infraestructura;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.dip) = LOWER(:dip)")
    Optional<Cliente> buscarPorDip(@Param("dip") String dip);
}
