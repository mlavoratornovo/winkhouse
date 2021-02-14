package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.ColloquiCriteriRicercaDAO;
import winkhouse.vo.RicercheVO;


public class RicercheModel extends RicercheVO {

	private ArrayList<ColloquiCriteriRicercaModel> criteri = null;
	
	public RicercheModel() {
		super();
	}

	public RicercheModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	/**
	 * Ritorna la lista dei criteri di ricerca salvati dall'utente per la ricerca di appartenenza
	 * @return ArrayList<CriteriRicercaModel>
	 */
	public ArrayList<ColloquiCriteriRicercaModel> getCriteri() {
		if (criteri == null){
			if (getCodRicerca() != null){
				ColloquiCriteriRicercaDAO ccrDAO = new ColloquiCriteriRicercaDAO();
				criteri = ccrDAO.getColloquiCriteriRicercaByRicerca(ColloquiCriteriRicercaModel.class.getName(), getCodRicerca());
				for (ColloquiCriteriRicercaModel criterio : criteri) {
					criterio.setTypeSearch(this.getTipo());
				}
			}
		}
		return criteri;
	}
	
	public void setCriteri(ArrayList<ColloquiCriteriRicercaModel> criteriRicerca){
		criteri = criteriRicerca;
	}

	
	@Override
	public boolean equals(Object obj) {
		
		boolean returnValue = false;
		
		if (obj instanceof RicercheModel){
			if (((RicercheModel)obj).getDescrizione().equalsIgnoreCase(this.getDescrizione())){
				
				if (((RicercheModel)obj).getNome().equalsIgnoreCase(this.getNome())){
					
					if(((RicercheModel)obj).getTipo() == this.getTipo()){
						
						Iterator<ColloquiCriteriRicercaModel> it = ((RicercheModel)obj).getCriteri().iterator();
						while(it.hasNext()){
							Iterator<ColloquiCriteriRicercaModel> ite = getCriteri().iterator();
							if (!it.next().equals(ite.next())){
								returnValue = false;
								break;
							}
						}
					}
				}
			}
		}else{
			return super.equals(obj);
		}
		return returnValue;
		
	}

	
}
