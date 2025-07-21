package bo.edu.uagrm.soe.prac02tdd.aplicacion.otd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteOTD {

    private Long id;

    private String nombre;

    private String dip;

    private Long grupoclienteId;
}
