package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import winkhouse.dao.AbbinamentiDAO;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AllegatiImmobiliDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.DatiCatastaliDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StanzeDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.engine.search.AnagraficheSearchEngine;
import winkhouse.engine.search.ImmobiliSearchEngine;
import winkhouse.helper.ProfilerHelper;
import winkhouse.orm.Agenti;
import winkhouse.orm.Immobili;
import winkhouse.util.IEntityAttribute;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologieImmobiliVO;

/**
 * Classe Model offre la risoluzione delle chiavi esterne della tabella IMMOBILI
 * ritornando il relativo VO o Model che rappresenta la riga della tabella collegata o per le
 * relazioni 1 a molti un ArrayList di VO o Model 
 * 
 * @author Michele Lavoratornovo
 *
 */
public class ImmobiliModel extends ImmobiliVO implements IEntityAttribute{

	private Agenti agenteInseritore = null;
	private AnagraficheModel anagrafica = null;
	private RiscaldamentiVO riscaldamento = null;
	private StatoConservativoVO statoConservativo = null;
	private TipologieImmobiliVO tipologiaImmobile = null;
	private ClasseEnergeticaVO classeEnergetica = null;
	private ArrayList stanze = null;
	private ArrayList immagini = null;
	private ArrayList colloqui = null;
	private ArrayList allegati = null;
	private ArrayList anagraficheAbbinate = null;
	private ArrayList<AnagraficheModel> anagrafichePropietarie = null;
	private Date periodoAffitto = null;
	private ArrayList colloquiVisiteReport = null;
	private ArrayList datiCatastali = null;
	private EntityModel entity = null;
	
	private String nomeCognomeAnagrafica = null;
	private String cittaAnagrafica = null;
	private String indirizzoAnagrafica = null;
	private String capAnagrafica = null;
	private String provinciaAnagrafica = null;	
	private String primoRecapitoAnagrafica = null;
	private String secondoRecapitoAnagrafica = null;
	private String codiceFiscaleAnagrafica = null;
	private String partitaIvaAnagrafica = null;
	
	public ImmobiliModel() {
		super();
	}
	
	public ImmobiliModel(ImmobiliVO iVO) {
		setAnnoCostruzione(iVO.getAnnoCostruzione());
		setCap(iVO.getCap());
		setCitta(iVO.getCitta());
		setCodAgenteInseritore(iVO.getCodAgenteInseritore());
		setCodAnagrafica(iVO.getCodAnagrafica());
		setCodImmobile(iVO.getCodImmobile());
		setCodRiscaldamento(iVO.getCodRiscaldamento());
		setCodStato(iVO.getCodStato());
		setCodTipologia(iVO.getCodTipologia());
		setDataInserimento(iVO.getDataInserimento());
		setDataLibero(iVO.getDataLibero());
		setDescrizione(iVO.getDescrizione());
		setIndirizzo(iVO.getIndirizzo());
		setNcivico(iVO.getNcivico());
		setMq(iVO.getMq());
		setMutuo(iVO.getMutuo());
		setMutuoDescrizione(iVO.getMutuoDescrizione());
		setPrezzo(iVO.getPrezzo());
		setProvincia(iVO.getProvincia());
		setSpese(iVO.getSpese());
		setStorico(iVO.getStorico());
		setAffittabile(iVO.getAffittabile());
		setVarie(iVO.getVarie());
		setVisione(iVO.getVisione());
		setZona(iVO.getZona());
		setRif(iVO.getRif());
		setCodClasseEnergetica(iVO.getCodClasseEnergetica());		
	}	

	public ImmobiliModel(ResultSet rs) throws SQLException {
		super(rs);		
	}
	
	public ImmobiliModel(Immobili rs){
		super(rs);
		this.getStanze();
	}

	public Agenti getAgenteInseritore() {
		if (agenteInseritore == null){			
			if ((this.getCodAgenteInseritore() != null) && 
				(this.getCodAgenteInseritore() != 0)){
				AgentiDAO agentiDAO = new AgentiDAO();
				Object o = agentiDAO.getAgenteById(AgentiVO.class.getName(), this.getCodAgenteInseritore());
				agenteInseritore = (o != null)?(Agenti)o:null;
			}
			
		}
		return agenteInseritore;
	}

	public void setAgenteInseritore(Agenti agenteInseritore) {
		this.agenteInseritore = agenteInseritore;
		if (agenteInseritore != null){
			this.setCodAgenteInseritore(agenteInseritore.getCodAgente());
		}else{
			this.setCodAgenteInseritore(null);
		}
	}

	public AnagraficheModel getAnagrafica() {
		if (anagrafica == null){
			if (getAnagrafichePropietarie() != null && getAnagrafichePropietarie().size() > 0){
				anagrafica = getAnagrafichePropietarie().get(0);
			}else{
				anagrafica = null;
			}			
		}
		return anagrafica;
	}

	public void setAnagrafica(AnagraficheModel anagrafica) {
		this.anagrafica = anagrafica;
		this.setCodAnagrafica((anagrafica!=null)?anagrafica.getCodAnagrafica():null);
	}

	public RiscaldamentiVO getRiscaldamento() {
		if (riscaldamento == null){
			if ((this.getCodRiscaldamento() != null) &&
				(this.getCodRiscaldamento() != 0)){
				RiscaldamentiDAO riscaldamentiDAO = new RiscaldamentiDAO();
				Object o = riscaldamentiDAO.getRiscaldamentiById(RiscaldamentiVO.class.getName(), this.getCodRiscaldamento());
				riscaldamento = (o != null)?(RiscaldamentiVO)o:null; 
			}
		}
		return riscaldamento;
	}

	public void setRiscaldamento(RiscaldamentiVO riscaldamento) {
		this.riscaldamento = riscaldamento;
		this.setCodRiscaldamento((riscaldamento!=null)?riscaldamento.getCodRiscaldamento():null);
	}

	public StatoConservativoVO getStatoConservativo() {
		if (statoConservativo == null){
			if ((this.getCodStato() != null) &&
				(this.getCodStato() != 0)){
				StatoConservativoDAO statoConservativoDAO = new StatoConservativoDAO();
				Object o = statoConservativoDAO.getStatoConservativoById(StatoConservativoVO.class.getName(), this.getCodStato());
				statoConservativo = (o != null)?(StatoConservativoVO)o:null; 
			}
		}
		
		return statoConservativo;
	}

	public void setStatoConservativo(StatoConservativoVO statoConservativo) {
		this.statoConservativo = statoConservativo;
		this.setCodStato((statoConservativo!=null)?statoConservativo.getCodStatoConservativo():null);
	}

	public TipologieImmobiliVO getTipologiaImmobile() {
		if (tipologiaImmobile == null){
			if ((this.getCodTipologia() != null) &&
				(this.getCodTipologia() != 0)){
				TipologieImmobiliDAO tipologieImmobiliDAO = new TipologieImmobiliDAO();
				Object o = tipologieImmobiliDAO.getTipologieImmobiliById(TipologieImmobiliVO.class.getName(), this.getCodTipologia());
				tipologiaImmobile = (o != null)?(TipologieImmobiliVO)o:null; 
			}
		}
		
		return tipologiaImmobile;
	}

	public void setTipologiaImmobile(TipologieImmobiliVO tipologiaImmobile) {
		this.tipologiaImmobile = tipologiaImmobile;
		this.setCodTipologia((tipologiaImmobile!=null)?tipologiaImmobile.getCodTipologiaImmobile():null);
	}

	public ClasseEnergeticaVO getClasseEnergetica() {
		if (classeEnergetica == null){
			if ((this.getCodClasseEnergetica() != null) &&
				(this.getCodClasseEnergetica() != 0)){
					ClassiEnergeticheDAO classiEnergeticheDAO = new ClassiEnergeticheDAO();
					Object o = classiEnergeticheDAO.getClassiEnergeticheByID(ClasseEnergeticaVO.class.getName(), this.getCodClasseEnergetica());
					classeEnergetica = (o != null)?(ClasseEnergeticaVO)o:null; 
				}
		}
		return classeEnergetica;
	}

	public void setClasseEnergetica(ClasseEnergeticaVO classeEnergetica) {
		this.classeEnergetica = classeEnergetica;		
		this.setCodClasseEnergetica((classeEnergetica!=null)?classeEnergetica.getCodClasseEnergetica():null);
	}
	
	public ArrayList getStanze() {
		if (getCodImmobile() != null){
			if (stanze == null) {
				StanzeDAO stanzeDAO = new StanzeDAO();
				stanze = stanzeDAO.listByImmobile(StanzeImmobiliModel.class.getName(), getCodImmobile());
			}
		}else{
			stanze = new ArrayList();
		}
		return stanze;
	}

	public void setStanze(ArrayList stanze) {
		this.stanze = stanze;
	}

	public ArrayList getImmagini() {
		
		if (getCodImmobile() != null){
			if (immagini == null){
				ImmaginiDAO immaginiDAO = new ImmaginiDAO();
				immagini = immaginiDAO.getImmaginiByImmobile(ImmagineModel.class.getName(), getCodImmobile());
			}
		}else{
			immagini = new ArrayList();
		}
		return immagini;
	}

	public void setImmagini(ArrayList immagini) {
		this.immagini = immagini;
	}

	public ArrayList getColloqui() {
		
		if (getCodImmobile() != null){
			if (colloqui == null){
				
					colloqui = new ArrayList<ColloquiModelVisiteCollection>();
					ColloquiModelVisiteCollection cmvc = new ColloquiModelVisiteCollection(this);
					colloqui.add(cmvc);				
				
			}
		}else{
			colloqui = new ArrayList();
		}
		return colloqui;
	}		

	public void setColloqui(ArrayList colloqui) {
		this.colloqui = colloqui;
	}
	
	public ArrayList<ColloquiModel> getColloquiVisiteReport() {
		
		if (getCodImmobile() != null){
			if (colloquiVisiteReport == null){
				ColloquiDAO cDAO = new ColloquiDAO();
				colloquiVisiteReport = cDAO.getColloquiByImmobile(ColloquiModel.class.getName(), 
														    	  getCodImmobile());
			}
		}else{
			colloquiVisiteReport = new ArrayList();
		}
		return colloquiVisiteReport;
	}
	
	public Integer getNumeroStanze(){
		return (getStanze() != null)? getStanze().size():new Integer(0);
	}

	@Override
	public String toString() {
		return getRif() + " - " + getCitta() + " " + getIndirizzo();
	}

	public ArrayList getAllegati() {
		if (getCodImmobile() != null){
			if (allegati == null){
				AllegatiImmobiliDAO aiDAO = new AllegatiImmobiliDAO();
				allegati = aiDAO.getAllegatiByImmobile(AllegatiImmobiliVO.class.getName(), getCodImmobile());
			}
		}else{
			allegati = new ArrayList();
		}
		return allegati;
	}

	public void setAllegati(ArrayList allegati) {
		this.allegati = allegati;
	}
	
	public void resetCache(){
		agenteInseritore = null;
		anagrafica = null;
		riscaldamento = null;
		statoConservativo = null;
		tipologiaImmobile = null;
		stanze = null;
		immagini = null;
		colloqui = null;
		allegati = null;
		classeEnergetica = null;
	}

	public ArrayList getAnagraficheAbbinate() {
		
		if (getCodImmobile() != null){
			
			if (anagraficheAbbinate == null){
				
				AbbinamentiDAO aDAO = new AbbinamentiDAO();
				anagraficheAbbinate = aDAO.findAbbinamentiByCodImmobile(AbbinamentiModel.class.getName(), super.getCodImmobile());
//				ArrayList anagrafiche = new ArrayList();

//				AnagraficheSearchEngine ise = new AnagraficheSearchEngine(this.getCodImmobile(),
//																		  anagrafiche);
//										
//				ise.run();
//				Iterator it = anagrafiche.iterator();
//				while (it.hasNext()){
//					AnagraficheModel im = (AnagraficheModel)it.next();
//					AbbinamentiModel am = new AbbinamentiModel();
//					am.setCodImmobile(this.getCodImmobile());
//					am.setCodAnagrafica(im.getCodAnagrafica());
//					anagraficheAbbinate.add(am);
//				}
							 						
			}
		}else{
			anagraficheAbbinate = new ArrayList();
		}
		return anagraficheAbbinate;
	}

	public Date getPeriodoAffitto() {
		return periodoAffitto;
	}

	public String getNomeCognomeAnagrafica() {
		if (nomeCognomeAnagrafica == null){
			if (getAnagrafica() != null){
				nomeCognomeAnagrafica = getAnagrafica().getNome() + " " +
										getAnagrafica().getCognome();
			}else{
				nomeCognomeAnagrafica = "";
			}
		}
		return nomeCognomeAnagrafica;
	}

	public String getCittaAnagrafica() {
		if (cittaAnagrafica == null){
			if (getAnagrafica() != null){
				cittaAnagrafica = getAnagrafica().getCitta();										
			}else{
				cittaAnagrafica = "";
			}
		}		
		return cittaAnagrafica;
	}

	public String getIndirizzoAnagrafica() {
		if (indirizzoAnagrafica == null){
			if (getAnagrafica() != null){
				indirizzoAnagrafica = getAnagrafica().getIndirizzo();										
			}else{
				indirizzoAnagrafica = "";
			}
		}				
		return indirizzoAnagrafica;
	}

	public String getPrimoRecapitoAnagrafica() {
		if (primoRecapitoAnagrafica == null){
			if (getAnagrafica() != null){
				ArrayList al = getAnagrafica().getContatti();
				if (al.size() > 0){
					primoRecapitoAnagrafica = (al.get(0) != null)
										      ? ((ContattiModel)al.get(0)).toString()
										      : "";
				}else{
					primoRecapitoAnagrafica = "";
				}
			}else{
				primoRecapitoAnagrafica = "";
			}
		}						
		return primoRecapitoAnagrafica;
	}

	public String getSecondoRecapitoAnagrafica() {
		if (secondoRecapitoAnagrafica == null){
			if (getAnagrafica() != null){
				ArrayList al = getAnagrafica().getContatti();
				if (al.size() > 0){
					secondoRecapitoAnagrafica = (al.get(1) != null)
										  	    ? ((ContattiModel)al.get(1)).toString()
										        : "";
				}else{
					secondoRecapitoAnagrafica = "";
				}
			}else{
				secondoRecapitoAnagrafica = "";
			}
		}						
		
		return secondoRecapitoAnagrafica;
	}

	public String getCodiceFiscaleAnagrafica() {
		if (codiceFiscaleAnagrafica == null){
			if (getAnagrafica() != null){
				codiceFiscaleAnagrafica = getAnagrafica().getCodiceFiscale();										
			}else{
				codiceFiscaleAnagrafica = "";
			}
		}						
		return codiceFiscaleAnagrafica;
	}

	public String getPartitaIvaAnagrafica() {
		if (partitaIvaAnagrafica == null){
			if (getAnagrafica() != null){
				partitaIvaAnagrafica = getAnagrafica().getPartitaIva();										
			}else{
				partitaIvaAnagrafica = "";
			}
		}								
		return partitaIvaAnagrafica;
	}

	public String getCapAnagrafica() {
		if (capAnagrafica == null){
			if (getAnagrafica() != null){
				capAnagrafica = getAnagrafica().getCap();										
			}else{
				capAnagrafica = "";
			}
		}
		return capAnagrafica;
	}

	public String getProvinciaAnagrafica() {
		if (provinciaAnagrafica == null){
			if (getAnagrafica() != null){
				provinciaAnagrafica = getAnagrafica().getProvincia();
			}else{
				provinciaAnagrafica = "";
			}			
		}
		return provinciaAnagrafica;
	}

	public ArrayList getDatiCatastali() {
			
		if (getCodImmobile() != null){
			if (datiCatastali == null){
				DatiCatastaliDAO dcDAO = new DatiCatastaliDAO();
				datiCatastali = dcDAO.findADatiCatastaliByCodImmobile(DatiCatastaliVO.class.getName(), getCodImmobile());
			}
		}else{
			datiCatastali = new ArrayList();
		}
		return datiCatastali;
	}

	public void setDatiCatastali(ArrayList datiCatastali) {
		this.datiCatastali = datiCatastali;
	}
	
	public EntityModel getEntity() {
		
		if (entity == null){
			
//			EntityDAO eDAO = new EntityDAO();
//			entity = eDAO.getEntityByClassName(ImmobiliVO.class.getName());
			
		}
		return entity;
	}

	public void setEntity(EntityModel entity) {
		this.entity = entity;
	}

	public ArrayList<AttributeModel> getAttributes() {
		
		if (getEntity() != null){
			return getEntity().getAttributes();
			
		}
		return null;
	}
	
	@Override
	public Integer getIdInstanceObject() {	
		return getCodImmobile();
	}

	public ArrayList<AnagraficheModel> getAnagrafichePropietarie() {
		
		if (getCodImmobile() != null){
			if (anagrafichePropietarie == null){
				
				AnagraficheDAO aDAO = new AnagraficheDAO();
				ArrayList<AnagraficheModel> tmp_anagrafiche = aDAO.getAnagraficheByCodImmobile(AnagraficheModel.class.getName(), getCodImmobile());
				anagrafichePropietarie = ProfilerHelper.getInstance().filterAnagrafiche(tmp_anagrafiche, false);
				
			}
		}else{
			if (anagrafichePropietarie == null){
				anagrafichePropietarie = new ArrayList<AnagraficheModel>();
			}
		}
		return anagrafichePropietarie;
	}

	public void setAnagrafichePropietarie(
			ArrayList<AnagraficheModel> anagrafichePropietarie) {
		this.anagrafichePropietarie = anagrafichePropietarie;
	}

	public String getDescrizioneAnagrafichePropietarie(){
		
		String returnValue = "";
		
		if (getAnagrafichePropietarie() != null){
			Iterator it = getAnagrafichePropietarie().iterator();
			while (it.hasNext()) {
				AnagraficheModel am = (AnagraficheModel) it.next();
				returnValue = returnValue + " - " + am.toString();
			}
		}
		
		return returnValue;
		
	}
	
	public void resolveDepedencies(){
		getAgenteInseritore();
//		getAnagrafica();
		getAnagrafichePropietarie();
//		getAttributes();
		getClasseEnergetica();
//		getColloqui();
		getDatiCatastali();
		getImmagini();
		getRiscaldamento();
		getStanze();
		getStatoConservativo();
	}
}
