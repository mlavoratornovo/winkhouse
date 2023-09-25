package winkhouse.xmlser.models;

public class DatiBaseModel {

	private Integer codDatiBase = null;
	private String descrizione = null;
	
	public DatiBaseModel(Integer codDatiBase,String descrizione){
		this.codDatiBase = codDatiBase;
		this.descrizione = descrizione;
	}

	public Integer getCodDatiBase() {
		return codDatiBase;
	}

	public String getDescrizione() {
		return descrizione;
	}
	
}