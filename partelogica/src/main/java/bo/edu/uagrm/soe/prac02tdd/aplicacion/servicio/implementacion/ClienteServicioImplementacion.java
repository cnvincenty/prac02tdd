package bo.edu.uagrm.soe.prac02tdd.aplicacion.servicio.implementacion;

import java.util.List;

import org.springframework.stereotype.Service;

import bo.edu.uagrm.soe.prac02tdd.aplicacion.otd.ClienteOTD;
import bo.edu.uagrm.soe.prac02tdd.aplicacion.servicio.ClienteServicio;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.Cliente;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.Grupocliente;
import bo.edu.uagrm.soe.prac02tdd.excepcion.RecursoNoEncontradoException;
import bo.edu.uagrm.soe.prac02tdd.infraestructura.ClienteRepositorio;
import bo.edu.uagrm.soe.prac02tdd.infraestructura.GrupoclienteRepositorio;

@Service
public class ClienteServicioImplementacion implements ClienteServicio{

    private final ClienteRepositorio repositorio;

    private final GrupoclienteRepositorio grupoclienteRepositorio;

    public ClienteServicioImplementacion(ClienteRepositorio repositorio,
            GrupoclienteRepositorio grupoclienteRepositorio) {
        this.repositorio = repositorio;
        this.grupoclienteRepositorio = grupoclienteRepositorio;
    }

    @Override
    public List<ClienteOTD> obtenerTodos() {
        return repositorio.findAll().stream()
                .map(this::aOTD)
                .toList();
    }

    @Override
    public ClienteOTD obtenerPorId(Long id) {
        Cliente salida = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        return aOTD(salida);
    }

    @Override
    public ClienteOTD buscarPorDip(String dip) {
        Cliente salida = repositorio.buscarPorDip(dip)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        return aOTD(salida);
    }

    @Override
    public ClienteOTD crear(ClienteOTD otd) {
        Cliente salida = aENTIDAD(otd);
        return aOTD(repositorio.save(salida));
    }

    @Override
    public ClienteOTD actualizar(Long id, ClienteOTD otd) {
        Cliente salida = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        salida.setNombre(otd.getNombre());
        salida.setDip(otd.getDip());

        if (otd.getGrupoclienteId() != null) {
            Grupocliente grupocliente = grupoclienteRepositorio.findById(otd.getGrupoclienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupocliente no encontrado"));
            salida.setGrupocliente(grupocliente);
        }

        return aOTD(repositorio.save(salida));
    }

    @Override
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    private ClienteOTD aOTD(Cliente entrada) {
        ClienteOTD salida = new ClienteOTD();
        salida.setId(entrada.getId());
        salida.setNombre(entrada.getNombre());
        salida.setDip(entrada.getDip());
        salida.setGrupoclienteId(entrada.getGrupocliente() != null ? entrada.getGrupocliente().getId() : null);
        return salida;
    }

    private Cliente aENTIDAD(ClienteOTD entrada) {
        Cliente salida = new Cliente();
        salida.setId(entrada.getId());
        salida.setNombre(entrada.getNombre());
        salida.setDip(entrada.getDip());
        if (entrada.getGrupoclienteId() != null) {
            Grupocliente grupocliente = grupoclienteRepositorio.findById(entrada.getGrupoclienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupocliente no encontrado"));
            salida.setGrupocliente(grupocliente);
        }
        return salida;
    }

}
