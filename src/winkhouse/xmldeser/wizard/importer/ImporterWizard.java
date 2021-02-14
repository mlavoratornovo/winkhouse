package winkhouse.xmldeser.wizard.importer;

import java.util.Iterator;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import winkhouse.Activator;
import winkhouse.xmldeser.utils.ObjectTypeCompare;
import winkhouse.xmldeser.wizard.importer.pages.ImportPreviewPage;
import winkhouse.xmldeser.wizard.importer.pages.MergeConflictPage;
import winkhouse.xmldeser.wizard.importer.pages.ResultsPage;
import winkhouse.xmldeser.wizard.importer.pages.SelettoreOrigineImportazione;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;

public class ImporterWizard extends Wizard {	
	
	private SelettoreOrigineImportazione soi = null;
	private ImportPreviewPage ipp = null;
	private MergeConflictPage mcp = null;
	private ResultsPage rp = null;	
	private ImporterVO importer = null;
	private boolean fromWinkCloud = false;
	
	public ImporterWizard() {
	}

	
	@Override
	public boolean performFinish() {
		return true;
	}

	
	@Override
	public void addPages() {
		if (!isFromWinkCloud()){
			soi = new SelettoreOrigineImportazione("SelettoreOrigineImortazione", 
												   "Seleziona la cartella di origine da cui imporate i dati", 
												   Activator.getImageDescriptor("icons/wizardimport/folder64.png"));
			soi.setDescription("Seleziona la cartella di origine da cui imporate i dati");
			addPage(soi);
		}
		ipp = new ImportPreviewPage("ImporterPreviewPage",
									"Lista degli oggetti che saranno importati", 
									Activator.getImageDescriptor("icons/wizardimport/folder64.png"));
		addPage(ipp);
		
		mcp = new MergeConflictPage("MergerConflictPage", 
									"Oggetti presenti nella base dati",
									Activator.getImageDescriptor("icons/wizardimport/folder64.png"));
		addPage(mcp);
		
		rp = new ResultsPage("ResultPage", 
							 "Avvio importazione dati e risultati",
							 Activator.getImageDescriptor("icons/wizardimport/folder64.png"));
		addPage(rp);
		
		
	}

	
	@Override
	public boolean canFinish() {
		Iterator it = getImporterVO().getRisultati_selected().iterator();
		while (it.hasNext()) {
			ObjectTypeCompare object = (ObjectTypeCompare) it.next();
			if (object.getImportOperation() != null){
				return true;
			}
			
		}
		return false;
	}

	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		IWizardPage returnValue = null;
		
		if (page instanceof SelettoreOrigineImportazione){
			if (getImporterVO().getPathExportFile() != null){
				
				returnValue = ipp;
			}
		}
		
		if (page instanceof ImportPreviewPage){
			if (getImporterVO().getRisultati_selected().size() > 0){
				return mcp;
			}
		}
		
		if (page instanceof MergeConflictPage){
			if (getImporterVO().getRisultati_selected().size() > 0){
			/*ImporterHelper ih = new ImporterHelper();
			ih.checkExists(getImporterVO());
			mcp.setRisultati(getImporterVO().getRisultati_merge());*/
				getImporterVO().initSelectedHashMaps();
				returnValue = rp;			
				
			}
		}

//		if (page instanceof ResultsPage){
//			if (getImporterVO().getRisultati_selected().size() > 0){
//				
//				returnValue = rp;
//				
//			}
//		}
		
		
		return returnValue;
	}

	
	public ImporterVO getImporterVO(){
		if (importer == null){
			importer = new ImporterVO();
		}
		return importer;
	}

	public ImporterVO setImporterVO(ImporterVO ivo){
		importer = ivo;
		return importer;
	}
	
	public String getVersion(){		
		return "";
	}


	public boolean isFromWinkCloud() {
		return fromWinkCloud;
	}


	public void setFromWinkCloud(boolean fromWinkCloud) {
		this.fromWinkCloud = fromWinkCloud;
	}


	
	
}
