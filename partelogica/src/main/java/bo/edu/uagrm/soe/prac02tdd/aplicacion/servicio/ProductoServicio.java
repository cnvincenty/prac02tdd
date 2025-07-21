package bo.edu.uagrm.soe.prac02tdd.aplicacion.servicio;

import java.util.List;

import bo.edu.uagrm.soe.prac02tdd.aplicacion.otd.ProductoOTD;

public interface ProductoServicio {

    List<ProductoOTD> obtenerTodos();

    ProductoOTD obtenerPorId(Long id);

    ProductoOTD crear(ProductoOTD otd);

    ProductoOTD actualizar(Long id, ProductoOTD otd);

    void eliminar(Long id);
}
