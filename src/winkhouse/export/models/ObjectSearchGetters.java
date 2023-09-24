package winkhouse.export.models;

/**
 * Classe che rappresenta la mappatura dei metodi degli oggetti di winkhouse (Immobili, Anagrafica, ecc...)
 * 
 * @author Administrator
 *
 */
public class ObjectSearchGetters {

	private Integer key = null;
	private String methodName = null;
	private String descrizione = null;
	private String columnName = null;
	private String parametrizedTypeName = null;
	private Boolean isPersonal = null;
	
	public ObjectSearchGetters(){
	}
	
	/**
	 * Costruttore della classe di mappatura dei metodi
	 * 
	 * @param key, un progressivo intero univoco della classe
	 * @param methodName, il nome del metodo di GET che rappresenta il punto di accesso al field privato della classe
	 * @param descrizione, la descrizione del metodo intesa come es: getIndirizzo, il methodName, Indirizzo, la descrizione 
	 * @param columnName, il nome della colonna nel database che rappresenta il field della classe
	 * @param parametrizedTypeName, se il field corrisponde ad una chiave esterna rappresenta il nome della classe che rappresenta
	 * una riga nella tabella collegata
	 */
	public ObjectSearchGetters(Integer key,String methodName,String descrizione,String columnName,String parametrizedTypeName){
		setMethodName(methodName);
		setDescrizione(descrizione);
		setKey(key);
		setColumnName(columnName);
		setParametrizedTypeName(parametrizedTypeName);
		setIsPersonal(false);
	}

	public ObjectSearchGetters(Integer key, String methodName, String descrizione, String columnName, String parametrizedTypeName, Boolean isPersonal) {
		setMethodName(methodName);
		setDescrizione(descrizione);
		setKey(key);
		setColumnName(columnName);
		setParametrizedTypeName(parametrizedTypeName);
		setIsPersonal(isPersonal);
	}

	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getParametrizedTypeName() {
		return parametrizedTypeName;
	}

	public void setParametrizedTypeName(String parametrizedTypeName) {
		this.parametrizedTypeName = parametrizedTypeName;
	}

	public Boolean getIsPersonal() {
		return isPersonal;
	}

	public void setIsPersonal(Boolean isPersonal) {
		this.isPersonal = isPersonal;
	}

	
}
