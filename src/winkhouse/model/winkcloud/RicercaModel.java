package winkhouse.model.winkcloud;

import javolution.xml.XMLFormat;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.winkcloud.mobilecolumns.MobileColumns2MethodsAdapter;

public class RicercaModel {
	
	private String column_name = null;
	private String logic_operatore = null;
	private String matchType = null;
	private String value_a = null;
	private String value_da = null;
	private String searchType = null;
	
	public RicercaModel() {
		// TODO Auto-generated constructor stub
	}
	
	public String getColumn_name() {
		return column_name;
	}
	
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	
	public String getLogic_operatore() {
		return logic_operatore;
	}
	
	public void setLogic_operatore(String logic_operatore) {
		this.logic_operatore = logic_operatore;
	}
	
	public String getMatchType() {
		return matchType;
	}
	
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	
	public String getValue_a() {
		return value_a;
	}
	
	public void setValue_a(String value_a) {
		this.value_a = value_a;
	}
	
	public String getSearchType() {
		return searchType;
	}
	
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	public CriteriRicercaModel toCriteriRicercaModel(){
		
		CriteriRicercaModel returnValue = new CriteriRicercaModel();
		MobileColumns2MethodsAdapter mc2ma = new MobileColumns2MethodsAdapter((this.searchType.equalsIgnoreCase("immobili"))
																			  ? CloudQueryTypes.IMMOBILI
																			  : CloudQueryTypes.ANAGRAFICHE);
		returnValue.setFromValue(getValue_da());
		returnValue.setToValue(getValue_a());
		returnValue.setGetterMethodName(mc2ma.getMethod(getColumn_name()));
		returnValue.setLogicalOperator(getLogic_operatore());		
		
		return returnValue;
		
	}
	
	public String getValue_da() {
		return value_da;
	}
	
	public void setValue_da(String value_da) {
		this.value_da = value_da;
	}
	
	private static final XMLFormat<RicercaModel> SEARCHPARAM_XML = new XMLFormat<RicercaModel>(RicercaModel.class){
		
		public void write(RicercaModel o, OutputElement e) throws XMLStreamException{
			
		}
		
		public void read(InputElement e, RicercaModel o) throws XMLStreamException{
			
			o.setColumn_name(e.getAttribute("column_name", ""));
			o.setLogic_operatore(e.getAttribute("logic_operatore", ""));
			o.setMatchType(e.getAttribute("matchType", ""));
			o.setValue_da(e.getAttribute("value_da", ""));
			o.setValue_a(e.getAttribute("value_a", ""));
			o.setSearchType(e.getAttribute("searchType", ""));
			
		}
		
	}; 
}
