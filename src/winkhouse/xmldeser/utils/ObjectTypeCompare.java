package winkhouse.xmldeser.utils;

public interface ObjectTypeCompare {

	public final static String NUOVO_INSERIMENTO = "Nuovo inserimento";
	public final static String AGGIORNAMENTO = "Aggiornamento";
	
	public int getTypeValue();
	
	public int getUniqueKey();
	
	public String getImportOperation();
	
}
