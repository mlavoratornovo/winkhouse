package winkhouse.dao;


public class CauseColloquiDAO {

	public final static String LISTA_CAUSECOLLOQUI = "from CauseColloquiVO";
	public final static String FIND_CAUSECOLLOQUI_BY_NAME = "from CauseColloquiVO where descrizione = ?";
	/*
	public ArrayList<CauseColloquiVO> list(){
		Transaction tx = null;
		ArrayList elems = new ArrayList<CauseColloquiVO>();
		try {			
			tx = HibernateSessionManager.getInstance().getSession().beginTransaction();
				
			elems = new ArrayList<CauseColloquiVO>(HibernateSessionManager.getInstance().getSession().createQuery(LISTA_CAUSECOLLOQUI).list());

			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elems;
	}
	
	public boolean saveUpdate(CauseColloquiVO ccVO,Transaction tx){
		Transaction trans = null;
		try {			
			trans = (tx == null)?HibernateSessionManager.getInstance().getSession().beginTransaction():tx;				
			HibernateSessionManager.getInstance().getSession().saveOrUpdate(ccVO);
			if (tx == null){
				trans.commit();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}			
	}
	
	public boolean delete(CauseColloquiVO ccVO,Transaction tx){
		Transaction trans = null;
		try {			
			trans = (tx == null)?HibernateSessionManager.getInstance().getSession().beginTransaction():tx;				
			HibernateSessionManager.getInstance().getSession().delete(ccVO);
			if (tx == null){
				trans.commit();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}			
	}
	
	public ArrayList<CauseColloquiVO> findByName(String name){
		Transaction trans = null;
		ArrayList elems = new ArrayList<CauseColloquiVO>();
		try {			
			trans = HibernateSessionManager.getInstance().getSession().beginTransaction();				
			Query q = HibernateSessionManager.getInstance().getSession().createQuery(FIND_CAUSECOLLOQUI_BY_NAME);
			q.setString(0, name);
			elems = new ArrayList<CauseColloquiVO>(q.list());
			trans.commit();			
		} catch (Exception e) {
			e.printStackTrace();
			
		}	
		return elems;
		
	}

	*/
}
