package winkhouse.helper;


public class CauseColloquiHelper {

	public CauseColloquiHelper(){}
/*	
	private Comparator<CauseColloquiVO> comparer = new Comparator<CauseColloquiVO>(){

		@Override
		public int compare(CauseColloquiVO o1, CauseColloquiVO o2) {
			if((o1.getCodCausa() == null) || (o2.getCodCausa() == null)){
				return -1;
			}else{
				if (o1.getCodCausa().intValue() == o2.getCodCausa().intValue()){
					return 0;
				}else if (o1.getCodCausa().intValue() > o2.getCodCausa().intValue()){
					return 1;
				}else{
					return -1;
				}
			}
		}
		
	};
	
	public ArrayList<CauseColloquiVO> updateDatiBase(ArrayList<CauseColloquiVO> listaCauseColloqui){
		
		ArrayList<CauseColloquiVO> returnValue = null;
		
		CauseColloquiDAO ccDAO = new CauseColloquiDAO();
		ArrayList<CauseColloquiVO> dbSource = ccDAO.list();
		
		Iterator<CauseColloquiVO> it = listaCauseColloqui.iterator();
		Transaction tx = HibernateSessionManager.getInstance().getSession().beginTransaction();
		
		
		boolean op = true;
		while (it.hasNext()){
			CauseColloquiVO ccVO = (CauseColloquiVO)it.next();
			op = ccDAO.saveUpdate(ccVO, tx);
			if (!op){
				break;
			}
		}
		
		if (op){
			Collections.sort(listaCauseColloqui, comparer);
			it = dbSource.iterator();
			while (it.hasNext()) {
				CauseColloquiVO ccVO = (CauseColloquiVO) it.next();
				int index = Collections.binarySearch(listaCauseColloqui, ccVO, comparer);
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
			returnValue = listaCauseColloqui;
		}else{
			tx.commit();
			returnValue = ccDAO.list();
		}
		
		MobiliaDatiBaseCache.getInstance().setCauseColloqui(returnValue);
		return returnValue;
	}
*/		
}
