package winkhouse.model.winkcloud.mobilecolumns;

import java.util.HashMap;

import winkhouse.model.winkcloud.CloudQueryTypes;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ImmobiliMethodName;

public class MobileColumns2MethodsAdapter {

	private CloudQueryTypes tipo = null;
	private HashMap<String,String> contextMap = null;
	
	public MobileColumns2MethodsAdapter(CloudQueryTypes tipo) {
		this.tipo = tipo;
		if (this.tipo == CloudQueryTypes.IMMOBILI){
			fillImmobili();
		}
		if (this.tipo == CloudQueryTypes.ANAGRAFICHE){
			fillAnagrafiche();
		}		
	}
	
	protected void fillImmobili(){
		
		contextMap = new HashMap();
		
		contextMap.put(ImmobiliColumnNames.MQ, ImmobiliMethodName.GET_MQ);
		contextMap.put(ImmobiliColumnNames.PREZZO, ImmobiliMethodName.GET_PREZZO);
		contextMap.put(ImmobiliColumnNames.MUTUO, ImmobiliMethodName.GET_MUTUO);
		contextMap.put(ImmobiliColumnNames.SPESE, ImmobiliMethodName.GET_SPESE);
		contextMap.put(ImmobiliColumnNames.ZONA, ImmobiliMethodName.GET_ZONA);
		contextMap.put(ImmobiliColumnNames.CITTA, ImmobiliMethodName.GET_CITTA);
		contextMap.put(ImmobiliColumnNames.PROVINCIA, ImmobiliMethodName.GET_PROVINCIA);
		
		contextMap.put(ImmobiliColumnNames.CAP, ImmobiliMethodName.GET_CAP);
		contextMap.put(ImmobiliColumnNames.ANNOCOSTRUZIONE, ImmobiliMethodName.GET_ANNOCOSTRUZIONE);
		contextMap.put(ImmobiliColumnNames.DATALIBERO, ImmobiliMethodName.GET_DATALIBERO);
		
		contextMap.put(ImmobiliColumnNames.CODTIPOLOGIA, ImmobiliMethodName.GET_CODTIPOLOGIA);
		contextMap.put(ImmobiliColumnNames.CODSTATO, ImmobiliMethodName.GET_CODSTATO);
		contextMap.put(ImmobiliColumnNames.CODRISCALDAMENTO, ImmobiliMethodName.GET_CODRISCALDAMENTO);
		contextMap.put(ImmobiliColumnNames.CODAGENTEINSERITORE, ImmobiliMethodName.GET_CODAGENTEINSERITORE);
		contextMap.put(ImmobiliColumnNames.CODCLASSEENERGETICA, ImmobiliMethodName.GET_CODCLASSEENERGETICA);
		
		contextMap.put(ImmobiliColumnNames.RIF, ImmobiliMethodName.GET_RIF);
		contextMap.put(ImmobiliColumnNames.INDIRIZZO, ImmobiliMethodName.GET_INDIRIZZO);
		
	}

	protected void fillAnagrafiche(){
		
		contextMap = new HashMap();
		
		contextMap.put(AnagraficheColumnNames.CAP, AnagraficheMethodName.GET_CAP);
		contextMap.put(AnagraficheColumnNames.CITTA, AnagraficheMethodName.GET_CITTA);
		contextMap.put(AnagraficheColumnNames.CODAGENTEINSERITORE, AnagraficheMethodName.GET_CODAGENTEINSERITORE);
		contextMap.put(AnagraficheColumnNames.CODCLASSECLIENTE, AnagraficheMethodName.GET_CODCLASSECLIENTE);
		contextMap.put(AnagraficheColumnNames.CODICEFISCALE, AnagraficheMethodName.GET_CODICE_FISCALE);
		contextMap.put(AnagraficheColumnNames.PIVA, AnagraficheMethodName.GET_PARTITA_IVA);
		contextMap.put(AnagraficheColumnNames.CITTA, AnagraficheMethodName.GET_CITTA);
		contextMap.put(AnagraficheColumnNames.PROVINCIA, AnagraficheMethodName.GET_PROVINCIA);
		
		contextMap.put(AnagraficheColumnNames.COGNOME, AnagraficheMethodName.GET_COGNOME);
		contextMap.put(AnagraficheColumnNames.NOME, AnagraficheMethodName.GET_NOME);
		contextMap.put(AnagraficheColumnNames.RAGSOC, AnagraficheMethodName.GET_RAGIONESOCIALE);
		contextMap.put(AnagraficheColumnNames.INDIRIZZO, AnagraficheMethodName.GET_INDIRIZZO);
				
	}

	public String getMethod(String columnName){
		return contextMap.get(columnName);
	}
	
	
}
