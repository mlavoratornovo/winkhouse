package winkhouse.wizard;

import java.util.Iterator;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import winkhouse.Activator;
import winkhouse.action.colloqui.SalvaColloquioAction;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.ColloquiModel;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.wizard.colloqui.AllegatiColloquio;
import winkhouse.wizard.colloqui.CriteriRicercaColloquio;
import winkhouse.wizard.colloqui.DatiComuniColloquio;
import winkhouse.wizard.colloqui.InserimentoAgenti;
import winkhouse.wizard.colloqui.InserimentoAggiornamentoAnagrafica;
import winkhouse.wizard.colloqui.RiassuntoColloquio;
import winkhouse.wizard.colloqui.SelezioneImmobile;
import winkhouse.wizard.colloqui.SelezioneTipoColloquio;
import winkhouse.wizard.colloqui.VarieColloqui;



public class ColloquiWizard extends Wizard {

	public final static String ID = "winkhouse.wizardcolloqui";
	
	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage() instanceof RiassuntoColloquio){
			return true;
		}else{
			return false;
		}
	}

	private InserimentoAgenti inserimentoAgenti = null;
	private AllegatiColloquio allegatiColloquio = null;
	private CriteriRicercaColloquio criteriRicercaColloquio = null; 
	private DatiComuniColloquio datiComuniColloquio = null;
	private InserimentoAggiornamentoAnagrafica inserimentoAggiornamentoAnagrafica = null;
	private SelezioneTipoColloquio selezioneTipoColloquio = null;
	private VarieColloqui varieColloqui = null;
	private SelezioneImmobile selezioneImmobile = null;
	private RiassuntoColloquio riassuntoColloquio = null;
	
	private ColloquiModel colloquio = null;
	private boolean wizardResult = true;
		
	public ColloquiWizard() {
		setWindowTitle("Inserimento colloquio");
		
	}

	@Override
	public boolean performFinish() {
	
		SalvaColloquioAction sca = new SalvaColloquioAction(colloquio);
		sca.run();
		return wizardResult;
		
	}

	@Override
	public void addPages() {
		selezioneTipoColloquio = new SelezioneTipoColloquio("Selezione tipologia colloquio",
															"Selezione tipologia colloquio",
															Activator.getImageDescriptor("icons/wizardcolloqui/scelta.png"));
		selezioneTipoColloquio.setDescription("Seleziona il tipo di colloquio da inserire");
		addPage(selezioneTipoColloquio);
		
		datiComuniColloquio = new DatiComuniColloquio("Dati generali colloquio",
													  "Dati generali colloquio",
													  Activator.getImageDescriptor("icons/wizardcolloqui/datigenerali.png"));
		datiComuniColloquio.setDescription("Inserimento dati generali : data, agente, descrizione ...");
		addPage(datiComuniColloquio);
		
		inserimentoAggiornamentoAnagrafica = new InserimentoAggiornamentoAnagrafica("Selezione - Inserimento anagrafica",
																					"Selezione - Inserimento anagrafica",
																					Activator.getImageDescriptor("icons/wizardcolloqui/anagrafiche.png"));
		inserimentoAggiornamentoAnagrafica.setDescription("Selezione / Inserimento anagrafiche che partecipano al colloquio");
		addPage(inserimentoAggiornamentoAnagrafica);
		
		criteriRicercaColloquio = new CriteriRicercaColloquio("Requisiti ricerca immobili",
															  "Requisiti ricerca immobili",
															  Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
		criteriRicercaColloquio.setDescription("Inserire i criteri di ricerca per gli immobili");
		addPage(criteriRicercaColloquio);
		
		varieColloqui = new VarieColloqui("Varie ed eventuali",
										  "Varie ed eventuali",
										  Activator.getImageDescriptor("icons/wizardcolloqui/varie.png"));
		varieColloqui.setDescription("Inserimento commenti da parte dell'agenzia e del cliente");
		addPage(varieColloqui);
		
		allegatiColloquio = new AllegatiColloquio("Documenti allegati al colloquio",
												  "Documenti allegati al colloquio",
												  Activator.getImageDescriptor("icons/wizardcolloqui/allegati.png"));
		allegatiColloquio.setDescription("Docuementi inerenti al colloquio");
		addPage(allegatiColloquio);
		
		inserimentoAgenti = new InserimentoAgenti("Agenti presenti al colloquio",
												  "Agenti presenti al colloquio",
												  Activator.getImageDescriptor("icons/wizardcolloqui/agenti.png"));
		inserimentoAgenti.setDescription("Agenti presenti al colloquio o che vengono interessati dal colloquio");
		addPage(inserimentoAgenti);
		
		selezioneImmobile = new SelezioneImmobile("Selezione immobile visita",
												  "Selezione immobile visita",
												  Activator.getImageDescriptor("icons/wizardricerca/gohome.png"));
		selezioneImmobile.setDescription("Selezinare l'immobile da visionare");
		addPage(selezioneImmobile);
		
		riassuntoColloquio = new RiassuntoColloquio("Riassunto",
													"Riassunto",
													Activator.getImageDescriptor("icons/wizardricerca/riepilogo.png"));
		riassuntoColloquio.setDescription("Riepilogo dei dati inseriti");
		addPage(riassuntoColloquio);
		
	}

	public ColloquiModel getColloquio() {
		if (colloquio == null){
			colloquio = new ColloquiModel();
		}
		return colloquio;
	}

	public void setColloquio(ColloquiModel colloquio) {
		this.colloquio = colloquio;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		IWizardPage returnValue = null;
		if (page instanceof SelezioneTipoColloquio){
			if((getColloquio().getTipologia().getCodTipologiaColloquio().intValue() == 1) || 
			   (getColloquio().getTipologia().getCodTipologiaColloquio().intValue() == 3)){
				returnValue = datiComuniColloquio;
				
			}else{
				returnValue = selezioneImmobile;
			}	
		} 
		if ((page instanceof DatiComuniColloquio)){
			if((getColloquio().getTipologia().getCodTipologiaColloquio().intValue() == 1) || 
			   (getColloquio().getTipologia().getCodTipologiaColloquio().intValue() == 3)){
				if (colloquio.getAgenteInseritore() != null){
					returnValue = inserimentoAggiornamentoAnagrafica;
				}
			}else{
				if (colloquio.getAgenteInseritore() != null){
					returnValue = inserimentoAgenti;
				}
			}
		}
		
		if (page instanceof SelezioneImmobile){
			if ((colloquio.getCodImmobileAbbinato() != null) &&
				(colloquio.getCodImmobileAbbinato() != 0)){	
				returnValue = inserimentoAggiornamentoAnagrafica;
			}
		}
		
		if (page instanceof InserimentoAggiornamentoAnagrafica){
			if(getColloquio().getTipologia().getCodTipologiaColloquio().intValue() == 1){
				if ((colloquio.getAnagrafiche() != null) && 
					(colloquio.getAnagrafiche().size() > 0)){
					returnValue = criteriRicercaColloquio;
				}
			}
			if(getColloquio().getTipologia().getCodTipologiaColloquio().intValue() == 2){
				if ((colloquio.getAnagrafiche() != null) && 
					(colloquio.getAnagrafiche().size() > 0)){
					returnValue = datiComuniColloquio;
				}
			}						
			if(getColloquio().getTipologia().getCodTipologiaColloquio().intValue() == 3){
				if ((colloquio.getAnagrafiche() != null) && 
					(colloquio.getAnagrafiche().size() > 0)){
					returnValue = inserimentoAgenti;
				}
			}						
		}
		if (page instanceof CriteriRicercaColloquio){
			if (colloquio.getCriteriRicerca() != null){
				if (colloquio.getCriteriRicerca().size() > 0){
					if (colloquio.getCriteriRicerca().size() == 1){
						if (((ColloquiCriteriRicercaVO)colloquio.getCriteriRicerca()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("(") ||
							((ColloquiCriteriRicercaVO)colloquio.getCriteriRicerca()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase(")") ||
							((ColloquiCriteriRicercaVO)colloquio.getCriteriRicerca()
																.get(0)).getGetterMethodName()
																		.equalsIgnoreCase("")																		
						   ){
							
						}else{
							if (checkCriteriSyntax(false)){
								returnValue = varieColloqui;
							}							
						} 
					}else{
						if (checkCriteriSyntax(false)){
							returnValue = varieColloqui;
						}
					}						
				}
			}
			
		}
		if (page instanceof InserimentoAgenti){ 
			returnValue = varieColloqui;
		}

		if (page instanceof VarieColloqui){
				returnValue = allegatiColloquio;
		}

		if (page instanceof AllegatiColloquio){
			riassuntoColloquio.showRiassunto();
			returnValue = riassuntoColloquio;
	}
		
		return returnValue;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		return super.getPreviousPage(page);
	}

	public boolean checkCriteriSyntax(boolean showpopup){
		boolean returnValue = true;
		int numOpen = 0;
		int numClose = 0;
		if ((colloquio != null) && (colloquio.getCriteriRicerca() != null)){
			Iterator it = colloquio.getCriteriRicerca().iterator();
			String prev = "";
			while (it.hasNext()){
				ColloquiCriteriRicercaVO ccrVO = (ColloquiCriteriRicercaVO)it.next();
				if (ccrVO.getGetterMethodName().equalsIgnoreCase("(")){
					if (!prev.equalsIgnoreCase(")")){
						numOpen++;					 
					}else{
						returnValue = false;
						break;
					}					
				}
				if (ccrVO.getGetterMethodName().equalsIgnoreCase(")")){
					if (!prev.equalsIgnoreCase("(")){
						numClose++;						 
					}else{
						returnValue = false;
						break;
					}															
				}	
				prev = ccrVO.getGetterMethodName();
			}
		}
		if (returnValue){
			returnValue = (numOpen == numClose)?true:false;
			if (!returnValue){
				if (showpopup){
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("Errore sintassi");
					mb.setMessage("Numero parentesi aperte diverse da numero parentesi chiuse");			
					mb.open();							
				}
			}else{
				SearchEngineImmobili sei = new SearchEngineImmobili(colloquio.getCriteriRicerca());
				if (sei.verifyQuery()){
					if (showpopup){
						MessageBox mb = new MessageBox(this.getShell(),SWT.ICON_INFORMATION);
						mb.setText("Sintassi corretta");
						mb.setMessage("La sintassi delle condizioni inserite risulta corretta");			
						mb.open();
					}
				}else{
					if (showpopup){
						MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
						mb.setText("Errore sintassi");
						mb.setMessage("Richiesta generata non corretta, \n controllare i criteri inseriti");			
						mb.open();
					}
				}	
			}			
		}else{
			if (showpopup){
				MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
				mb.setText("Errore sequenza parentesi");
				mb.setMessage("Sequenza parentesi non corretta");			
				mb.open();							
			}			
		}
		return returnValue;
	}
}
