package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AttributeDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.IPropertiesFieldDescriptor;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class ColloquiCriteriRicercaModel extends ColloquiCriteriRicercaVO 
										 implements ICriteriaTableModel {
	
	private IPropertiesFieldDescriptor pfd = null; 
	private IDaAValueObject	daValue = null;
	private IDaAValueObject	aValue = null;
	private int typeSearch = 0;	
	
	class InnerDaAValueObject implements IDaAValueObject{
		
		private String codValue = null;
		private String displayValue = null;
		
		public InnerDaAValueObject(String codValue, String displayValue){
		    this.codValue = codValue;
		    this.displayValue = displayValue;
		}
		
		@Override
		public String getDisplayValue() {
			return displayValue;
		}

		@Override
		public String getCodValue() {
			return codValue;
		}

		@Override
		public Object getBindValue() {
			return null;
		}
		
	}
	
	public ColloquiCriteriRicercaModel() {}

	public ColloquiCriteriRicercaModel(ColloquiCriteriRicercaVO ccrVO) {
		this.setCodColloquio(ccrVO.getCodColloquio());
		this.setCodRicerca(ccrVO.getCodRicerca());
		this.setCodCriterioRicerca(ccrVO.getCodCriterioRicerca());
		this.setGetterMethodName(ccrVO.getGetterMethodName());
		this.setFromValue(ccrVO.getFromValue());
		this.setToValue(ccrVO.getToValue());
		this.setLogicalOperator(ccrVO.getLogicalOperator());
		this.setIsPersonal(ccrVO.getIsPersonal());
	}
	
	public ColloquiCriteriRicercaModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	@Override
	public void setPropertiesFieldDescriptior(IPropertiesFieldDescriptor pfd) {
		this.pfd = pfd;
		this.setGetterMethodName(pfd.getMethodName());
		this.setIsPersonal(pfd.getIsPersonal());
	}

	@Override
	public IPropertiesFieldDescriptor getPropertiesFieldDescriptior() {
		if (
				(pfd == null) && (this.getGetterMethodName() != null) && (!this.getGetterMethodName().equalsIgnoreCase(""))
			){
			this.pfd = WinkhouseUtils.getInstance(). new ObjectSearchGetters();
			this.pfd.setMethodName(this.getGetterMethodName());
			this.pfd.setIsPersonal(this.getIsPersonal());
			if (this.pfd.getIsPersonal()){
				AttributeDAO aDAO = new AttributeDAO();
				AttributeModel am = aDAO.getAttributeByID(Integer.valueOf(this.getGetterMethodName()));
				this.pfd.setDescrizione(am.getAttributeName());
			}else{
				
				WinkhouseUtils.ObjectSearchGetters osg = null;
				
				if (typeSearch == RicercheModel.RICERCHE_IMMOBILI){
					osg = WinkhouseUtils.getInstance()
								  		.findObjectSearchGettersByMethodName(this.pfd.getMethodName(), 
								  			   								 WinkhouseUtils.IMMOBILI, 
								  											 WinkhouseUtils.FUNCTION_SEARCH);
					
				}
				if (typeSearch == RicercheModel.RICERCHE_ANAGRAFICHE){
					osg = WinkhouseUtils.getInstance()
								  		.findObjectSearchGettersByMethodName(this.pfd.getMethodName(), 
								  			   								 WinkhouseUtils.ANAGRAFICHE, 
								  											 WinkhouseUtils.FUNCTION_SEARCH);
					
				}
				if (typeSearch == RicercheModel.RICERCHE_IMMOBILI_AFFITTI){
					osg = WinkhouseUtils.getInstance()
								        .findObjectSearchGettersByMethodName(this.pfd.getMethodName(), 
								  			   								 WinkhouseUtils.AFFITTIIMMOBILI, 
								  											 WinkhouseUtils.FUNCTION_SEARCH);
					
				}
				
				if (osg != null){
					this.pfd.setDescrizione(osg.getDescrizione());
				}

				
			}
		}
		return pfd;
	}

	@Override
	public void setDaValue(IDaAValueObject value) {
		this.daValue = value;
		this.setFromValue(String.valueOf(value.getCodValue()));
	}

	@Override
	public IDaAValueObject getDaValue() {
		if ((this.daValue == null) && (this.getFromValue() != null) && (!this.getFromValue().equalsIgnoreCase(""))){
			if ((this.getTypeSearch() == RicercheModel.RICERCHE_IMMOBILI) || (this.getTypeSearch() == RicercheModel.RICERCHE_IMMOBILI_AFFITTI)){
				
				if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODTIPOLOGIA)){
					
					TipologieImmobiliDAO tidao = new TipologieImmobiliDAO();
					
					TipologieImmobiliVO tivo = (TipologieImmobiliVO)tidao.getTipologieImmobiliById(TipologieImmobiliVO.class.getName(), Integer.valueOf(this.getFromValue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), tivo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODCLASSEENERGETICA)){
					
					ClassiEnergeticheDAO cedao = new ClassiEnergeticheDAO();
					
					ClasseEnergeticaVO cevo = (ClasseEnergeticaVO)cedao.getClassiEnergeticheByID(ClasseEnergeticaVO.class.getName(), Integer.valueOf(this.getFromValue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), cevo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODRISCALDAMENTO)){
					
					RiscaldamentiDAO rdao = new RiscaldamentiDAO();
					
					RiscaldamentiVO rvo = (RiscaldamentiVO)rdao.getRiscaldamentiById(RiscaldamentiVO.class.getName(), Integer.valueOf(this.getFromValue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), rvo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODSTATO)){
					
					StatoConservativoDAO scdao = new StatoConservativoDAO();
					
					StatoConservativoVO scvo = (StatoConservativoVO)scdao.getStatoConservativoById(StatoConservativoVO.class.getName(), Integer.valueOf(this.getFromValue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), scvo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODAGENTEINSERITORE)){
					
					AgentiDAO adao = new AgentiDAO();
					
					AgentiVO avo = (AgentiVO)adao.getAgenteById(AgentiVO.class.getName(), Integer.valueOf(this.getFromValue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), avo.getCognome() + " " + avo.getNome());
				}else{
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), this.getFromValue());
					
				}				

				
			}else if (this.getTypeSearch() == RicercheModel.RICERCHE_ANAGRAFICHE){

				if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE)){
					
					ClassiClientiDAO ccdao = new ClassiClientiDAO();
					
					ClassiClientiVO ccvo = (ClassiClientiVO)ccdao.getClasseClienteById(ClassiClientiVO.class.getName(), Integer.valueOf(this.getFromValue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), ccvo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODAGENTEINSERITORE)){
					
					AgentiDAO adao = new AgentiDAO();
					
					AgentiVO avo = (AgentiVO)adao.getAgenteById(AgentiVO.class.getName(), Integer.valueOf(this.getFromValue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), avo.getCognome() + " " + avo.getNome());
				}else{
					
					this.daValue = new InnerDaAValueObject(this.getFromValue(), this.getFromValue());
					
				}								
				
			}else{
				this.daValue = new InnerDaAValueObject(this.getFromValue(), this.getFromValue());
			}
			
		}
		return daValue;
	}

	@Override
	public void setAValue(IDaAValueObject value) {
		this.aValue = value;
		this.setToValue(value.getCodValue());
	}

	@Override
	public IDaAValueObject getAValue() {
		if ((this.aValue == null) && (this.getToValue() != null) && (!this.getToValue().equalsIgnoreCase(""))){
			this.aValue = new InnerDaAValueObject(this.getFromValue(), this.getToValue());
		}
		return aValue;
	}

	public int getTypeSearch() {
		return typeSearch;
	}

	public void setTypeSearch(int typeSearch) {
		this.typeSearch = typeSearch;
	}

	
	@Override
	public boolean equals(Object obj) {
		
		ColloquiCriteriRicercaModel compareObj = null;
		
		boolean returnValue = false;
		
		if (obj instanceof ArrayList){
			compareObj = (ColloquiCriteriRicercaModel)((ArrayList)obj).get(0);
		}
		if (obj instanceof ColloquiCriteriRicercaModel){
			compareObj = (ColloquiCriteriRicercaModel)obj;
		}
		
		if (((ColloquiCriteriRicercaModel)compareObj).getFromValue().equalsIgnoreCase(this.getFromValue())){
			if (((ColloquiCriteriRicercaModel)compareObj).getToValue().equalsIgnoreCase(this.getToValue())){
				if (((ColloquiCriteriRicercaModel)compareObj).getGetterMethodName().equalsIgnoreCase(this.getGetterMethodName())){
					if (((ColloquiCriteriRicercaModel)compareObj).getLogicalOperator().equalsIgnoreCase(this.getLogicalOperator())){
						returnValue = true;
					}
				}
			}
			
		}
		
		return returnValue;
	}

	
	
}
