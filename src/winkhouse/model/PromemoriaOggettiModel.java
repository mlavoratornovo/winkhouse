package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.vo.PromemoriaOggettiVO;
import winkhouse.wizard.RicercaWizard;

public class PromemoriaOggettiModel extends PromemoriaOggettiVO {

	public Object linkedObject = null;
	
	public PromemoriaOggettiModel() {
		// TODO Auto-generated constructor stub
	}

	public PromemoriaOggettiModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}

	
	public Object getLinkedObject() {
		if (linkedObject == null){
			if (getTipo() == RicercaWizard.IMMOBILI){
				ImmobiliDAO iDAO = new ImmobiliDAO();
				linkedObject = iDAO.getImmobileById(ImmobiliModel.class.getName(), getCodOggetto());
			}
			if (getTipo() == RicercaWizard.ANAGRAFICHE){
				AnagraficheDAO aDAO = new AnagraficheDAO();
				linkedObject = aDAO.getAnagraficheById(AnagraficheModel.class.getName(), getCodOggetto());
			}
		}
		return linkedObject;
	}

	
	public void setLinkedObject(Object linkedObject) {
		this.linkedObject = linkedObject;
	}

	
}
