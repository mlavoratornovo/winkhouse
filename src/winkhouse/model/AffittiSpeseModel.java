package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.vo.AffittiSpeseVO;


public class AffittiSpeseModel extends AffittiSpeseVO {

	private AnagraficheModel anagrafica = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public AffittiSpeseModel() {
	}

	public AffittiSpeseModel(AffittiSpeseVO asVO) {
		super(asVO);
	}

	public AffittiSpeseModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	public AnagraficheModel getAnagrafica() {
		if (anagrafica == null){
			if (super.getCodAnagrafica() != null){
				AnagraficheDAO aDAO = new AnagraficheDAO();
				anagrafica = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
																	   super.getCodAnagrafica());
			}
		}
		return anagrafica;
	}

	public void setAnagrafica(AnagraficheModel anagrafica) {
		if (anagrafica != null){
			setCodAnagrafica(anagrafica.getCodAnagrafica());
		}else{
			setCodAnagrafica(0);
		}
		this.anagrafica = anagrafica;
	}

	@Override
	public String toString() {
		return "Descrizione : " + getDescrizione() + " " + 
			   "Importo : " + getImporto() + " " +
			   "Versato : " + getVersato() + " " +
			   "Data pagato : " + formatter.format(getDataPagato()) + " " +
			   "Anagraficha : " + ((getAnagrafica() == null)?"":getAnagrafica().toString());
	}


}
