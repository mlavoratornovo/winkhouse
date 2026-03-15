package winkhouse.orm;

import java.util.ArrayList;

import org.apache.cayenne.Cayenne;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AttributeDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.model.AttributeModel;
import winkhouse.orm.auto._Colloquicriteriricerca;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.IPropertiesFieldDescriptor;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class Colloquicriteriricerca extends _Colloquicriteriricerca implements ICriteriaTableModel{
	private IPropertiesFieldDescriptor pfd = null; 
	private IDaAValueObject	daValue = null;
	private IDaAValueObject	aValue = null;
	private int typeSearch = 0;	
    private static final long serialVersionUID = 1L; 
    private Integer lineNumber = null;
    
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
	public int getCodColloquicriteriricerca() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public IDaAValueObject getAValue() {
		if ((this.aValue == null) && (this.getTovalue() != null) && (!this.getTovalue().equalsIgnoreCase(""))){
			this.aValue = new InnerDaAValueObject(this.getFromvalue(), this.getTovalue());
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
	public IDaAValueObject getDaValue() {
		if ((this.daValue == null) && (this.getFromvalue() != null) && (!this.getFromvalue().equalsIgnoreCase(""))){
			if ((this.getTypeSearch() == Ricerche.RICERCHE_IMMOBILI) || (this.getTypeSearch() == Ricerche.RICERCHE_IMMOBILI_AFFITTI)){
				
				if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODTIPOLOGIA)){
					
					TipologieImmobiliDAO tidao = new TipologieImmobiliDAO();
					
					Tipologieimmobili tivo = tidao.getTipologieImmobiliById(Integer.valueOf(this.getFromvalue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), tivo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODCLASSEENERGETICA)){
					
					ClassiEnergeticheDAO cedao = new ClassiEnergeticheDAO();
					
					Classienergetiche cevo = (Classienergetiche)cedao.getClassiEnergeticheByID(Integer.valueOf(this.getFromvalue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), cevo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODRISCALDAMENTO)){
					
					RiscaldamentiDAO rdao = new RiscaldamentiDAO();
					
					Riscaldamenti rvo = (Riscaldamenti)rdao.getRiscaldamentiById(Integer.valueOf(this.getFromvalue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), rvo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODSTATO)){
					
					StatoConservativoDAO scdao = new StatoConservativoDAO();
					
					Statoconservativo scvo = (Statoconservativo)scdao.getStatoConservativoById(Integer.valueOf(this.getFromvalue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), scvo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODAGENTEINSERITORE)){
					
					AgentiDAO adao = new AgentiDAO();
					
					Agenti avo = adao.getAgenteById(Integer.valueOf(this.getFromvalue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), avo.getCognome() + " " + avo.getNome());
				}else{
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), this.getFromvalue());
					
				}				

				
			}else if (this.getTypeSearch() == Ricerche.RICERCHE_ANAGRAFICHE){

				if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE)){
					
					ClassiClientiDAO ccdao = new ClassiClientiDAO();
					
					Classicliente ccvo = ccdao.getClasseClienteById(Integer.valueOf(this.getFromvalue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), ccvo.getDescrizione());
					
				}else if (this.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODAGENTEINSERITORE)){
					
					AgentiDAO adao = new AgentiDAO();
					
					Agenti avo = (Agenti)adao.getAgenteById(Integer.valueOf(this.getFromvalue()));
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), avo.getCognome() + " " + avo.getNome());
				}else{
					
					this.daValue = new InnerDaAValueObject(this.getFromvalue(), this.getFromvalue());
					
				}								
				
			}else{
				this.daValue = new InnerDaAValueObject(this.getFromvalue(), this.getFromvalue());
			}
			
		}
		return daValue;

	}

	@Override
	public String getLogicalOperator() {
		// TODO Auto-generated method stub
		return this.logicaloperator;
	}

	@Override
	public IPropertiesFieldDescriptor getPropertiesFieldDescriptior() {
		if (
				(pfd == null) && (this.getGettermethodname() != null) && (!this.getGettermethodname().equalsIgnoreCase(""))
			){
			this.pfd = WinkhouseUtils.getInstance(). new ObjectSearchGetters();
			this.pfd.setMethodName(this.getGettermethodname());
			this.pfd.setIsPersonal(this.ispersonal);
			if (this.pfd.getIsPersonal()){
				AttributeDAO aDAO = new AttributeDAO();
				AttributeModel am = aDAO.getAttributeByID(Integer.valueOf(this.getGettermethodname()));
				this.pfd.setDescrizione(am.getAttributeName());
			}else{
				
				WinkhouseUtils.ObjectSearchGetters osg = null;
				
				if (typeSearch == Ricerche.RICERCHE_IMMOBILI){
					osg = WinkhouseUtils.getInstance()
								  		.findObjectSearchGettersByMethodName(this.pfd.getMethodName(), 
								  			   								 WinkhouseUtils.IMMOBILI, 
								  											 WinkhouseUtils.FUNCTION_SEARCH);
					
				}
				if (typeSearch == Ricerche.RICERCHE_ANAGRAFICHE){
					osg = WinkhouseUtils.getInstance()
								  		.findObjectSearchGettersByMethodName(this.pfd.getMethodName(), 
								  			   								 WinkhouseUtils.ANAGRAFICHE, 
								  											 WinkhouseUtils.FUNCTION_SEARCH);
					
				}
				if (typeSearch == Ricerche.RICERCHE_IMMOBILI_AFFITTI){
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
	public void setAValue(IDaAValueObject arg0) {
		this.aValue = arg0;
		this.tovalue = arg0.getCodValue();
	}

	@Override
	public void setDaValue(IDaAValueObject arg0) {
		this.daValue = arg0;	
		this.fromvalue = String.valueOf(arg0.getCodValue());
	}

	@Override
	public void setLogicalOperator(String arg0) {
		this.setLogicalOperator(arg0);
		
	}

	@Override
	public void setPropertiesFieldDescriptior(IPropertiesFieldDescriptor arg0) {
		this.pfd = arg0;
		this.setGettermethodname(arg0.getMethodName());
		this.setIspersonal(arg0.getIsPersonal());
	}
    
	@Override
	public boolean equals(Object obj) {
		
		Colloquicriteriricerca compareObj = null;
		
		boolean returnValue = false;
		
		if (obj instanceof ArrayList){
			compareObj = (Colloquicriteriricerca)((ArrayList)obj).get(0);
		}
		if (obj instanceof Colloquicriteriricerca){
			compareObj = (Colloquicriteriricerca)obj;
		}
		
		if (((Colloquicriteriricerca)compareObj).getFromvalue()!= null && 
			((Colloquicriteriricerca)compareObj).getFromvalue().equalsIgnoreCase(this.getFromvalue())){
			if (((Colloquicriteriricerca)compareObj).getTovalue() != null && 
				((Colloquicriteriricerca)compareObj).getTovalue().equalsIgnoreCase(this.getTovalue())){
				if (((Colloquicriteriricerca)compareObj).getGettermethodname().equalsIgnoreCase(this.getGettermethodname())){
					if (((Colloquicriteriricerca)compareObj).getLogicalOperator() != null) {
						if (((Colloquicriteriricerca)compareObj).getLogicalOperator().equalsIgnoreCase(this.getLogicalOperator())){
							returnValue = true;
						}
					}else {
						returnValue = true;
					}
				}
			}
			
		}
		
		return returnValue;
	}
	
}
