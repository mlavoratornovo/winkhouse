package winkhouse.orm;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.orm.auto._Colloqui;
import winkhouse.util.WinkhouseUtils;

public class Colloqui extends _Colloqui {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
	
    public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}

	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}

	public int getCodColloquio() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
	
	@Override
	public String toString() {	
		String dataColloquio = "";
		if (this.getDatacolloquio() != null) {
			dataColloquio = formatter.format(Date.from(this.getDatacolloquio().atZone(ZoneId.systemDefault()).toInstant())) + 
			" " + formatterTime.format(Date.from(this.getDatacolloquio().atZone(ZoneId.systemDefault()).toInstant()));
		} 
		return "Codice : " + this.getCodColloquio() + " - " + 
			   "Data colloquio : " + dataColloquio + " - " + 
			   "Tipologia : " + (this.codtipologiacolloquio == null? "":EnvSettingsFactory.getInstance().getTipologiaColloquioById(codtipologiacolloquio));
	}
}
