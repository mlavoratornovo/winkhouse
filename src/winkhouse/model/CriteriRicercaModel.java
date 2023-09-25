package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.vo.ColloquiCriteriRicercaVO;


public class CriteriRicercaModel extends ColloquiCriteriRicercaVO {

	public CriteriRicercaModel() {
		super();
	}

	public CriteriRicercaModel(ColloquiCriteriRicercaVO ccrVO) {
		setCodColloquio(ccrVO.getCodColloquio());
		setCodCriterioRicerca(ccrVO.getCodCriterioRicerca());
		setFromValue(ccrVO.getFromValue());
		setGetterMethodName(ccrVO.getGetterMethodName());
		setLineNumber(ccrVO.getLineNumber());
		setLogicalOperator(ccrVO.getLogicalOperator());
		setToValue(ccrVO.getToValue());
	}

	public CriteriRicercaModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	/**
	 * Ritorna la stringa di descrizione del metodo descritto dall'istanza della classe 
	 * @return
	 */
	public String getMethodDescription(){
		String returnValue = "";
		ObjectSearchGetters osg = WinkhouseUtils.getInstance()
												  .findObjectSearchGettersByMethodName(getGetterMethodName(), 
															 						   WinkhouseUtils.IMMOBILI, 
															 						   WinkhouseUtils.FUNCTION_SEARCH);
		returnValue = (osg != null)?osg.getDescrizione():"";
		return returnValue;
	}

}