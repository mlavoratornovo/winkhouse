package winkhouse.helper;

import java.util.ArrayList;
import java.util.Comparator;

import winkhouse.vo.TipologieColloquiVO;


public class TipologieColloquiHelper {

	public TipologieColloquiHelper() {

	}

	private Comparator<TipologieColloquiVO> comparer = new Comparator<TipologieColloquiVO>(){

		@Override
		public int compare(TipologieColloquiVO o1, TipologieColloquiVO o2) {
			if((o1.getCodTipologiaColloquio() == null) || (o2.getCodTipologiaColloquio() == null)){
				return -1;
			}else{
				if (o1.getCodTipologiaColloquio().intValue() == o2.getCodTipologiaColloquio().intValue()){
					return 0;
				}else if (o1.getCodTipologiaColloquio().intValue() > o2.getCodTipologiaColloquio().intValue()){
					return 1;
				}else{
					return -1;
				}
			}
		}
		
	};
	
	public ArrayList<TipologieColloquiVO> updateDatiBase(ArrayList<TipologieColloquiVO> listaTipologieColloqui){
		
		ArrayList<TipologieColloquiVO> returnValue = null;
		/*
		TipologieColloquiDAO ccDAO = new TipologieColloquiDAO();
		ArrayList<TipologieColloquiVO> dbSource = ccDAO.list();
		
		Iterator<TipologieColloquiVO> it = listaTipologieColloqui.iterator();
		Transaction tx = HibernateSessionManager.getInstance().getSession().beginTransaction();
		
		
		boolean op = true;
		while (it.hasNext()){
			TipologieColloquiVO ccVO = (TipologieColloquiVO)it.next();
			op = ccDAO.saveUpdate(ccVO, tx);
			if (!op){
				break;
			}
		}
		
		if (op){
			Collections.sort(listaTipologieColloqui, comparer);
			it = dbSource.iterator();
			while (it.hasNext()) {
				TipologieColloquiVO ccVO = (TipologieColloquiVO) it.next();
				int index = Collections.binarySearch(listaTipologieColloqui, ccVO, comparer);
				if (index < 0){
					op = ccDAO.delete(ccVO, tx);
				}
				if (!op){
					break;
				}
			}
		}
		
		if (!op){
			tx.rollback();
			MessageDialog.openError(null,
					"Errore inserimento / aggiornamento",
					"Errore inserimento / aggiornamento tipologia colloquio");

		}else{
			tx.commit();
			
		}
		returnValue = ccDAO.list();*/
		return returnValue;
	}
		
	
}
