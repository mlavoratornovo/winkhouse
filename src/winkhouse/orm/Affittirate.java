package winkhouse.orm;

import winkhouse.orm.auto._Affittirate;
import winkhouse.util.WinkhouseUtils;

public class Affittirate extends _Affittirate {

    private static final long serialVersionUID = 1L;
    private String nomeMese = null;
    
	public String getNomeMese() {
		nomeMese = WinkhouseUtils.getInstance()
								   .getMesi()
								   .get(getMese())
								   .getNome();
		return nomeMese;
	}
}
