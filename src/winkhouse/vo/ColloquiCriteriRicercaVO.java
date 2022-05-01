package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;


public class ColloquiCriteriRicercaVO implements Serializable{
	
	/**
	 * La classe rappresenta una condizione di ricerca da passare alle funzioni di ricerca per proprietà
	 * La classe può essere serializzata nella tabella COLLOQUICRITERIRICERCA
	 */
	
	private Integer codCriterioRicerca = null;
	private Integer codColloquio = null;
	private Integer codRicerca = null;
	private String getterMethodName = null;
	private String fromValue = null;
	private String toValue = null;
	private String logicalOperator = null;
	private Integer lineNumber = null;
	private Boolean isPersonal = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public ColloquiCriteriRicercaVO(){
		getterMethodName = "";
		fromValue = "";
		toValue = "";
		logicalOperator = "";	
		isPersonal = false;
	}

	public ColloquiCriteriRicercaVO(ResultSet rs) throws SQLException{
		codCriterioRicerca = rs.getInt("CODCRITERIORICERCA");
		codColloquio = rs.getInt("CODCOLLOQUIO");
		if (rs.wasNull()){
			codColloquio = null;
		}
		
		codRicerca = rs.getInt("CODRICERCA");
		if (rs.wasNull()){
			codRicerca = null;
		}
		
		getterMethodName = rs.getString("GETTERMETHODNAME");
		fromValue = rs.getString("FROMVALUE");
		toValue = rs.getString("TOVALUE");
		logicalOperator = rs.getString("LOGICALOPERATOR");
		isPersonal = rs.getBoolean("ISPERSONAL");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}
	
	public String getGetterMethodName() {
		return getterMethodName;
	}

	/**
	 * Il nome del metodo di get che verrà invocato per ottenere il valore che rappresenta il campo della classe
	 * @param String getterMethodName
	 */
	public void setGetterMethodName(String getterMethodName) {
		this.getterMethodName = getterMethodName;
	}

	/**
	 * Operatore logico con il quale verrà concatenata la condizione di ricerca (AND/OR)
	 * @return
	 */
	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	
	public Integer getCodCriterioRicerca() {
		return codCriterioRicerca;
	}

	public void setCodCriterioRicerca(Integer codCriterioRicerca) {
		this.codCriterioRicerca = codCriterioRicerca;
	}

	public Integer getCodColloquio() {
		return codColloquio;
	}

	public void setCodColloquio(Integer codColloquio) {
		this.codColloquio = codColloquio;
	}
	
	/**
	 * Valore con il quale va eseguito il match rispetto al valore che resituisce il metodo inserito 
	 * tramite setGetterMethodName,se viene valorizzato solo il campo fromValue il confronto viene eseguito 
	 * tramite il match puntuale, mentre se viene valorizzato anche il campo toValue viene considerato come
	 * valore di inizio di un intervallo di valori.
	 * @return String
	 */
	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	/**
	 * Se fromValue è valorizzato rappresenta il valore di fine entro il quale cercare il valore generato dall'invocazione
	 * del metodo ritornato da getGetterMethodname
	 * @return String
	 */
	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}
	
	/**
	 * Ritorna il numero di riga della condizione 
	 * @return
	 */
	public Integer getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * Imposta il numero di riga della condizione, il numero di riga vincola l'ordine in cui vengono
	 * processate le condizioni durante la ricerca
	 */

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public String toString() {
		String returnValue = "";
		ObjectSearchGetters osg = WinkhouseUtils.getInstance()
												.findObjectSearchGettersByMethodName(this.getterMethodName, 
														  							 WinkhouseUtils.IMMOBILI,
														  							 WinkhouseUtils.FUNCTION_SEARCH);
		if (osg == null){
			osg = WinkhouseUtils.getInstance()
					  			.findObjectSearchGettersByMethodName(this.getterMethodName, 
							  							   			 WinkhouseUtils.ANAGRAFICHE, 
							  							   			 WinkhouseUtils.FUNCTION_SEARCH);

		}
		if (osg != null){
			returnValue += osg.getDescrizione() + " da : " + 
						   this.getFromValue() + " a : " + 
						   this.getToValue(); 
		}else{
			returnValue += this.getterMethodName + " da : " + this.getFromValue() + " a : " + 
						   this.getToValue(); 
		}
		
		return returnValue;
	}

	public Integer getCodRicerca() {
		return codRicerca;
	}

	public void setCodRicerca(Integer codRicerca) {
		this.codRicerca = codRicerca;
	}

	public Boolean getIsPersonal() {
		if (isPersonal == null){
			isPersonal = false;
		}
		return isPersonal;
	}

	public void setIsPersonal(Boolean isPersonal) {
		this.isPersonal = isPersonal;
	}

	
	public Integer getCodUserUpdate() {
		return codUserUpdate;
	}

	
	public void setCodUserUpdate(Integer codUserUpdate) {
		this.codUserUpdate = codUserUpdate;
	}

	
	public Date getDateUpdate() {
		return dateUpdate;
	}

	
	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}


}
