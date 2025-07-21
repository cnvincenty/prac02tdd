package bo.edu.uagrm.soe.prac02tdd.aplicacion.servicio.implementacion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.edu.uagrm.soe.prac02tdd.aplicacion.otd.FacturaVentaItemOTD;
import bo.edu.uagrm.soe.prac02tdd.aplicacion.otd.FacturaVentaOTD;
import bo.edu.uagrm.soe.prac02tdd.aplicacion.servicio.FacturaVentaServicio;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.Cliente;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.FacturaVenta;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.FacturaVentaItem;
import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.Producto;
import bo.edu.uagrm.soe.prac02tdd.excepcion.RecursoNoEncontradoException;
import bo.edu.uagrm.soe.prac02tdd.infraestructura.ClienteRepositorio;
import bo.edu.uagrm.soe.prac02tdd.infraestructura.FacturaVentaRepositorio;
import bo.edu.uagrm.soe.prac02tdd.infraestructura.ProductoRepositorio;

@Service
public class FacturaVentaServicioImplementacion implements FacturaVentaServicio {

    @Autowired
    private FacturaVentaRepositorio facturaVentaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Override
    @Transactional
    public FacturaVentaOTD registrarFacturaVenta(FacturaVentaOTD facturaVentaOTD) {
        // Obtener el cliente
        Cliente cliente = clienteRepositorio.findById(facturaVentaOTD.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        // Crear la factura
        FacturaVenta facturaVenta = new FacturaVenta();
        facturaVenta.setCliente(cliente);
        facturaVenta.setAlmacen(facturaVentaOTD.getAlmacen());
        facturaVenta.setCondicionPago(facturaVentaOTD.getCondicionPago());

        // Agregar los items
        for (FacturaVentaItemOTD itemOTD : facturaVentaOTD.getItems()) {
            Producto producto = productoRepositorio.findById(itemOTD.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

            FacturaVentaItem item = new FacturaVentaItem();
            item.setProducto(producto);
            item.setCantidad(itemOTD.getCantidad());
            item.setPrecioUnitario(itemOTD.getPrecioUnitario());

            facturaVenta.agregarItem(item);
        }

        // Calcular totales y descuentos
        facturaVenta.calcularTotales();

        // Guardar la factura
        facturaVenta = facturaVentaRepositorio.save(facturaVenta);

        // Convertir a DTO y devolver
        return convertirADTO(facturaVenta);
    }

    @Override
    public FacturaVentaOTD obtenerFacturaVenta(Long id) {
        FacturaVenta facturaVenta = facturaVentaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada"));
        return convertirADTO(facturaVenta);
    }

    @Override
    public List<FacturaVentaOTD> listarFacturasVenta() {
        return facturaVentaRepositorio.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private FacturaVentaOTD convertirADTO(FacturaVenta facturaVenta) {
        FacturaVentaOTD dto = new FacturaVentaOTD();
        dto.setId(facturaVenta.getId());
        dto.setFecha(facturaVenta.getFecha());
        dto.setClienteId(facturaVenta.getCliente().getId());
        dto.setClienteNombre(facturaVenta.getCliente().getNombre());
        dto.setClienteGrupo(facturaVenta.getCliente().getGrupocliente() != null ?
                facturaVenta.getCliente().getGrupocliente().getNombre() : null);
        dto.setAlmacen(facturaVenta.getAlmacen());
        dto.setCondicionPago(facturaVenta.getCondicionPago());
        dto.setTotal(facturaVenta.getTotal());

        for (FacturaVentaItem item : facturaVenta.getItems()) {
            FacturaVentaItemOTD itemDTO = new FacturaVentaItemOTD();
            itemDTO.setProductoId(item.getProducto().getId());
            itemDTO.setProductoNombre(item.getProducto().getNombre());
            itemDTO.setProductoGrupo(item.getProducto().getGrupoproducto() != null ?
                    item.getProducto().getGrupoproducto().getNombre() : null);
            itemDTO.setCantidad(item.getCantidad());
            itemDTO.setPrecioUnitario(item.getPrecioUnitario());
            itemDTO.setPorcentajeDescuento(item.getPorcentajeDescuento());
            itemDTO.setMontoDescuento(item.getMontoDescuento());
            itemDTO.setSubtotal(item.getSubtotal());

            dto.getItems().add(itemDTO);
        }

        return dto;
    }
}