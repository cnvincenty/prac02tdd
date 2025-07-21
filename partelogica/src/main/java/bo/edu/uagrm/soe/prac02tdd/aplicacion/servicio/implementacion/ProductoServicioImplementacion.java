package bo.edu.uagrm.soe.prac02tdd.aplicacion.servicio.implementacion;

import java.util.List;

import org.springframework.stereotype.Service;

import bo.edu.uagrm.soe.prac02tdd.aplicacion.otd.ProductoOTD;
import bo.edu.uagrm.soe.prac02tdd.aplicacion.servicio.ProductoServicio;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.Grupoproducto;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.Producto;
import bo.edu.uagrm.soe.prac02tdd.excepcion.RecursoNoEncontradoException;
import bo.edu.uagrm.soe.prac02tdd.infraestructura.GrupoproductoRepositorio;
import bo.edu.uagrm.soe.prac02tdd.infraestructura.ProductoRepositorio;

@Service
public class ProductoServicioImplementacion implements ProductoServicio{

    private final ProductoRepositorio repositorio;

    private final GrupoproductoRepositorio grupoproductoRepositorio;

    public ProductoServicioImplementacion(ProductoRepositorio repositorio,
            GrupoproductoRepositorio grupoproductoRepositorio) {
        this.repositorio = repositorio;
        this.grupoproductoRepositorio = grupoproductoRepositorio;
    }

    @Override
    public List<ProductoOTD> obtenerTodos() {
        return repositorio.findAll().stream()
                .map(this::aOTD)
                .toList();
    }

    @Override
    public ProductoOTD obtenerPorId(Long id) {
        Producto salida = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        return aOTD(salida);
    }

    @Override
    public ProductoOTD crear(ProductoOTD otd) {
        Producto salida = aENTIDAD(otd);
        return aOTD(repositorio.save(salida));
    }

    @Override
    public ProductoOTD actualizar(Long id, ProductoOTD otd) {
        Producto salida = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        salida.setNombre(otd.getNombre());
        salida.setPreciounitario(otd.getPreciounitario());
        salida.setDescuento(otd.getDescuento());
        salida.setUnidadMedida(otd.getUnidadMedida());

        if (otd.getGrupoproductoId() != null) {
            Grupoproducto grupoproducto = grupoproductoRepositorio.findById(otd.getGrupoproductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupoproducto no encontrado"));
            salida.setGrupoproducto(grupoproducto);
        }

        return aOTD(repositorio.save(salida));
    }

    @Override
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    private ProductoOTD aOTD(Producto entrada) {
        ProductoOTD salida = new ProductoOTD();
        salida.setId(entrada.getId());
        salida.setNombre(entrada.getNombre());
        salida.setPreciounitario(entrada.getPreciounitario());
        salida.setDescuento(entrada.getDescuento());
        salida.setUnidadMedida(entrada.getUnidadMedida());
        return salida;
    }

    private Producto aENTIDAD(ProductoOTD entrada) {
        Producto salida = new Producto();
        salida.setId(entrada.getId());
        salida.setNombre(entrada.getNombre());
        salida.setPreciounitario(entrada.getPreciounitario());
        salida.setDescuento(entrada.getDescuento());
        salida.setUnidadMedida(entrada.getUnidadMedida());
        if (entrada.getGrupoproductoId() != null) {
            Grupoproducto grupoproducto = grupoproductoRepositorio.findById(entrada.getGrupoproductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupoproducto no encontrado"));
            salida.setGrupoproducto(grupoproducto);
        }
        return salida;
    }
}
