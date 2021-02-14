package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AffittiRateVO;


public class AffittiRateModel extends AffittiRateVO {

	private AnagraficheModel anagrafica = null;
	private String nomeMese = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public AffittiRateModel() {
		
	}

	public AffittiRateModel(AffittiRateVO arVO) {
		super(arVO);
	}

	public AffittiRateModel(ResultSet rs) throws SQLException {
		super(rs);		
	}

	public AnagraficheModel getAnagrafica() {
		if (anagrafica == null){
			if (getCodAnagrafica() != null){
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

	public String getNomeMese() {
		nomeMese = WinkhouseUtils.getInstance()
								   .getMesi()
								   .get(getMese())
								   .getNome();
		return nomeMese;
	}

	@Override
	public String toString() {
		return "Mese : " + getNomeMese() + " " + 
			   "Data pagato : " + formatter.format(getDataPagato()) + " " + 
			   "Importo : " + getImporto() + " " + 
			   "Anagrafica : " + ((getAnagrafica() == null) ? "" : getAnagrafica().toString());
	}
	
}
