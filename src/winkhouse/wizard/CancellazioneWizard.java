package winkhouse.wizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.wizard.cancellazioni.ListaOggetti;
import winkhouse.wizard.cancellazioni.ListaOggettiEdit;
import winkhouse.wizard.cancellazioni.SelezioneTipologia;


public class CancellazioneWizard extends Wizard {

	private SelezioneTipologia selezioneTipologia = null;
	private ListaOggetti listaOggetti = null;
	private ListaOggettiEdit listaOggettiEdit = null;
	private CancellazioneModel cancellazioneModel = null;
	
	public CancellazioneWizard() {
		super();
	}
	
	public CancellazioneWizard(String tipoOggetto, String descrizione) {
		setWindowTitle("Cancellazione " + tipoOggetto + " :" + descrizione);
	}
	
	private class CancellazioneModel{
		
		public final static int CANCELLA_TUTTO = 1;
		public final static int CANCELLA_MANUALE = 2;
		public final static int STORICO = 3;
				
		private int tipoCancellazione = CANCELLA_TUTTO;
		
		private ArrayList<AnagraficheModel> anagrafiche = null;
		private ArrayList<ImmobiliModel> immobili = null;
		private ArrayList<ColloquiModel> colloqui = null;
		
		public CancellazioneModel(){}

		public int getTipoCancellazione() {
			return tipoCancellazione;
		}

		public void setTipoCancellazione(int tipoCancellazione) {
			this.tipoCancellazione = tipoCancellazione;
		};
	}

	@Override
	public boolean performFinish() {

		return false;
	}

	@Override
	public boolean canFinish() {
		if ((getContainer().getCurrentPage() instanceof ListaOggetti) ||
			(getContainer().getCurrentPage() instanceof ListaOggettiEdit)){
			return true;
		}else{
			return false;
		}
				
	}

	@Override
	public void addPages() {
		selezioneTipologia = new SelezioneTipologia("Selezione tipologia cancellazione");
		addPage(selezioneTipologia);
		listaOggetti = new ListaOggetti("Lista degli elementi in cancellazione");
		addPage(listaOggetti);
		listaOggettiEdit = new ListaOggettiEdit("Selezione elementi in cancellazione");
		addPage(listaOggettiEdit);
	}

	public CancellazioneModel getCancellazioneModel() {
		if (cancellazioneModel == null){
			cancellazioneModel = new CancellazioneModel();
		}
		return cancellazioneModel;
	}

	public void setCancellazioneModel(CancellazioneModel cancellazioneModel) {
		this.cancellazioneModel = cancellazioneModel;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage returnValue = null;
		if (page instanceof SelezioneTipologia){
			if (cancellazioneModel.getTipoCancellazione() == CancellazioneModel.CANCELLA_TUTTO){
				returnValue = listaOggetti;
			}
			if (cancellazioneModel.getTipoCancellazione() == CancellazioneModel.CANCELLA_MANUALE){
				returnValue = listaOggettiEdit;
			}
			if (cancellazioneModel.getTipoCancellazione() == CancellazioneModel.STORICO){
				returnValue = listaOggetti;
			}
		}
		return returnValue;
	}
	
	

}
