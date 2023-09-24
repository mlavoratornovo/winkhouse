package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AbbinamentiDAO;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.ContattiDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.engine.search.ImmobiliSearchEngine;
import winkhouse.helper.ProfilerHelper;
import winkhouse.orm.Anagrafiche;
import winkhouse.util.IEntityAttribute;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClassiClientiVO;


public class AnagraficheModel extends AnagraficheVO implements IEntityAttribute{

	private AgentiVO agenteInseritore = null;
	private ClassiClientiVO classeCliente = null;
	private ArrayList contatti = null;
	private ArrayList colloqui = null;
	private ArrayList colloquiReport = null;
	private ArrayList immobili = null;
	private ArrayList abbinamenti = null;
	private EntityModel entity = null;
	
	public AnagraficheModel() {
		super();
	}

	public AnagraficheModel(AnagraficheVO anagraficheVO) {		
		setCap(anagraficheVO.getCap());
		setCitta(anagraficheVO.getCitta());
		setCodAgenteInseritore(anagraficheVO.getCodAgenteInseritore());
		setCodAnagrafica(anagraficheVO.getCodAnagrafica());
		setCodClasseCliente(anagraficheVO.getCodClasseCliente());
		setCognome(anagraficheVO.getCognome());
		setCommento(anagraficheVO.getCommento());
		setDataInserimento(anagraficheVO.getDataInserimento());
		setIndirizzo(anagraficheVO.getIndirizzo());
		setNcivico(anagraficheVO.getNcivico());
		setNome(anagraficheVO.getNome());
		setProvincia(anagraficheVO.getProvincia());
		setStorico(anagraficheVO.getStorico());
		setRagioneSociale(anagraficheVO.getRagioneSociale());
		setCodiceFiscale(anagraficheVO.getCodiceFiscale());
		setPartitaIva(anagraficheVO.getPartitaIva());
	}
	
	public AnagraficheModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public AnagraficheModel(Anagrafiche rs) {
		super(rs);
	}

	public AgentiVO getAgenteInseritore() {
		if (agenteInseritore == null){
			if (getCodAgenteInseritore() != null){
				AgentiDAO agentiDAO = new AgentiDAO();
				Object o = agentiDAO.getAgenteById("winkhouse.vo.AgentiVO", getCodAgenteInseritore());
				agenteInseritore = (o != null)?(AgentiVO)o:null;				
			}
		}
		return agenteInseritore;
	}

	public void setAgenteInseritore(AgentiVO agenteInseritore) {
		this.agenteInseritore = agenteInseritore;
		if (agenteInseritore != null){
			this.setCodAgenteInseritore(agenteInseritore.getCodAgente());
		}else{
			this.setCodAgenteInseritore(null);
		}
	}

	public ClassiClientiVO getClasseCliente() {
		if (classeCliente == null){
			if (getCodClasseCliente() != null){
				ClassiClientiDAO classiClientiDAO = new ClassiClientiDAO();
				Object o = classiClientiDAO.getClasseClienteById("winkhouse.vo.ClassiClientiVO", getCodClasseCliente());
				classeCliente = (o != null)?(ClassiClientiVO)o:null;				
			}
		}
		return classeCliente;
	}
	
	public void setClasseCliente(ClassiClientiVO classeCliente) {
		this.classeCliente = classeCliente;
		if (classeCliente != null){
			this.setCodClasseCliente(classeCliente.getCodClasseCliente());
		}else{
			this.setCodClasseCliente(null);
		}
	}

	public ArrayList getContatti() {
		if (contatti == null){
			ContattiDAO cDAO = new ContattiDAO();
			if ((super.getCodAnagrafica() != null) && (super.getCodAnagrafica().intValue() > 0)){
				contatti = cDAO.listByAnagrafica(ContattiModel.class.getName(), super.getCodAnagrafica());
			}else{
				contatti = new ArrayList();
			}
		}
		return contatti;
	}

	public void setContatti(ArrayList contatti) {
		this.contatti = contatti;
	}

	public ArrayList getColloqui() {
		if (colloqui == null){
			colloqui = new ArrayList();
			ColloquiModelAnagraficaCollection cmac = new ColloquiModelAnagraficaCollection(this);
			ColloquiModelRicercaCollection cmrc = new ColloquiModelRicercaCollection(this,null);
			colloqui.add(cmac);
			colloqui.add(cmrc);
		}
		return colloqui;
	}
	
	public ArrayList getColloquiReport(){
		if (colloquiReport == null){
			colloquiReport = new ArrayList();
			ColloquiModelAnagraficaCollection cmac = new ColloquiModelAnagraficaCollection(this);			
			colloquiReport.addAll(cmac.getColloqui());
			ColloquiDAO cDAO = new ColloquiDAO();
			colloquiReport.addAll(cDAO.getColloquiByAnagraficaRicerca(ColloquiModel.class.getName(), getCodAnagrafica()));
		}
		return colloquiReport;
	}

	public void setColloqui(ArrayList colloqui) {
		this.colloqui = colloqui;
	}
	
	public String getDescrizioneClasseCliente(){
		if (getClasseCliente() != null){
			return getClasseCliente().getDescrizione();
		}else{
			return "";
		}
	}
	
	public String getNomeCognomeAgenteInseritore(){
		if (getAgenteInseritore() != null){
			return getAgenteInseritore().getNome() + " " + getAgenteInseritore().getCognome(); 
		}else{
			return "";
		}
	}

	public String getNomeAgenteInseritore(){
		if (getAgenteInseritore() != null){
			return getAgenteInseritore().getNome(); 
		}else{
			return "";
		}
	}

	public String getCognomeAgenteInseritore(){
		if (getAgenteInseritore() != null){
			return getAgenteInseritore().getCognome(); 
		}else{
			return "";
		}
	}

	@Override
	public String toString() {
		return getCognome() + " " + getNome() + " " + getRagioneSociale();
	}

	public ArrayList getImmobili() {
		if (immobili == null){
			if ((getCodAnagrafica() != null) && 
				(getCodAnagrafica() != 0)){
				ImmobiliDAO iDAO = new ImmobiliDAO();
				ArrayList tmp_immobili = iDAO.getImmobiliByAnagraficaPropietario(ImmobiliModel.class.getName(), 
																   				 this.getCodAnagrafica());
				immobili = ProfilerHelper.getInstance().filterImmobili(tmp_immobili, false);				
			}else{
				immobili = new ArrayList();
			}
		}
		return immobili;
	}

	public void resetCache(){
		agenteInseritore = null;
		classeCliente = null;
		contatti = null;
		colloqui = null;
		immobili = null;
		abbinamenti = null;
	}

	public ArrayList getAbbinamenti() {
		if (abbinamenti == null){
			AbbinamentiDAO aDAO = new AbbinamentiDAO();
			abbinamenti = aDAO.findAbbinamentiByCodAnagrafica(AbbinamentiModel.class.getName(), getCodAnagrafica());
			
			
			ArrayList immobili = new ArrayList();
/*			ImmobiliSearchEngine ise = new ImmobiliSearchEngine(WinkhouseUtils.getInstance().getLastCodAnagraficaSelected(),
																immobili);
*/
			ImmobiliSearchEngine ise = new ImmobiliSearchEngine(this.getCodAnagrafica(),
															    immobili);
			
			
			ise.run();
			Iterator it = immobili.iterator();
			while (it.hasNext()){
				ImmobiliModel im = (ImmobiliModel)it.next();
				AbbinamentiModel am = new AbbinamentiModel();
				am.setCodAnagrafica(this.getCodAnagrafica());
				am.setCodImmobile(im.getCodImmobile());
				am.setCodAbbinamento(0);
				abbinamenti.add(am);
			}
			
		}
		return abbinamenti;
	}
	
	public EntityModel getEntity() {
		
		if (entity == null){
			
			EntityDAO eDAO = new EntityDAO();
			entity = eDAO.getEntityByClassName(AnagraficheVO.class.getName());
			
		}
		return entity;
	}

	public void setEntity(EntityModel entity) {
		this.entity = entity;
	}

	@Override
	public Integer getIdInstanceObject() {	
		return getCodAnagrafica();
	}

	
	public ArrayList<AttributeModel> getAttributes() {
		
		if (getEntity() != null){
			return getEntity().getAttributes();
			
		}
		return null;
	}

	


}
