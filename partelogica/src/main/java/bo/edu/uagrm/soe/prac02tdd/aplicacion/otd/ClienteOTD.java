package bo.edu.uagrm.soe.prac02tdd.aplicacion.otd;

import bo.edu.uagrm.soe.prac02tdd.dominio.entidad.TipoDocumeto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteOTD {

    private Long id;

    private String codigo;

    private String nombre;

    private TipoDocumeto tipoDocumento;

    private String numeroDocumento;

    private String email;

    private Long grupoclienteId;
}
