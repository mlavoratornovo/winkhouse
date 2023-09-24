package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.vo.ColloquiAnagraficheVO;


public class ColloquiAnagraficheModel_Ang extends ColloquiAnagraficheVO {

	public AnagraficheModel anagrafica = null;
	
	public ColloquiAnagraficheModel_Ang() {
	}
	
	public ColloquiAnagraficheModel_Ang(ColloquiAnagraficheVO caVO) {
		setCodAnagrafica(caVO.getCodAnagrafica());
		setCodColloquio(caVO.getCodColloquio());
		setCodColloquioAnagrafiche(caVO.getCodColloquioAnagrafiche());
		setCommento(caVO.getCommento());
	}	

	public ColloquiAnagraficheModel_Ang(ResultSet rs) throws SQLException {
		super(rs);
	}

	public AnagraficheModel getAnagrafica() {
		if (anagrafica == null){
			if ((super.getCodAnagrafica() != null) && (super.getCodAnagrafica() != 0)){
				AnagraficheDAO anagraficaDAO = new AnagraficheDAO();
				Object o = anagraficaDAO.getAnagraficheById(AnagraficheModel.class.getName(), super.getCodAnagrafica());
				if (o != null){
					anagrafica = (AnagraficheModel)o;
				}
			}
		}

		return anagrafica;
	}

	public void setAnagrafica(AnagraficheModel anagrafica) {
		this.anagrafica = anagrafica;
		setCodAnagrafica(anagrafica.getCodAnagrafica());
	}

	@Override
	public String toString() {
		return getAnagrafica().toString() + " " + getCommento();
	}

	public String getDesAnagrafica() {
		return getAnagrafica().toString();
	}

}
