package winkhouse.xmlser.wizard.exporter.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.export.WrongCriteriaSequenceException;
import winkhouse.export.helpers.AnagraficheHelper;
import winkhouse.export.helpers.ExportedRicercheHelper;
import winkhouse.export.models.ObjectSearchGetters;
import winkhouse.export.utils.CriteriaTablesHelper;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.RicercheModel;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.RicercheVO;
import winkhouse.wizard.RicercaWizard;
import winkhouse.Activator;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;
import winkhouse.xmlser.wizard.exporter.PopUpEditRicerca;
import winkhouse.xmlser.wizard.exporter.PopUpRicercaRicerche;




public class ListaCriteriAnagrafiche extends WizardPage{

	private Composite container = null;
	private TableViewer tvCriteri = null;
	private String[] desGetters = null;
	private String[] desClassiClienti = null;
	private Integer[] codClassiClienti = null;
	private String[] desAgenti = null;
	private Integer[] codAgenti = null;
	private Label lRicercaSelectedName = null;
	private RicercheModel ricerca = null;

//	private ArrayList<ColloquiCriteriRicercaVO> criteri = null;

	
	public ListaCriteriAnagrafiche(String pageName) {
		super(pageName);
	}
	
	private Comparator c = new Comparator<String>(){

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	};

	public ListaCriteriAnagrafiche(String pageName, String title,
								   ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	private Comparator<ObjectSearchGetters> comparatorAnagraficaSearchGetters = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters arg0,
						   ObjectSearchGetters arg1) {
			return arg0.getMethodName().compareToIgnoreCase(arg1.getMethodName());			
		}
		
	};

	private Comparator<ColloquiCriteriRicercaVO> comparatorLineNumber = new Comparator<ColloquiCriteriRicercaVO>(){

		@Override
		public int compare(ColloquiCriteriRicercaVO o1, ColloquiCriteriRicercaVO o2) {
			int returnValue=0;
			if (o1.getLineNumber().intValue() > o2.getLineNumber().intValue()){
				return 1;
			}else if (o1.getLineNumber().intValue() < o2.getLineNumber().intValue()){
				return -1;
			}
			return 0;
			
		}
		
	};
	
	@Override
	public void createControl(Composite parent) {
		
		setTitle(((ExporterWizard)getWizard()).getVersion());
		
		loadCriteriColloquio();
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		Composite toolbar = new Composite(container,SWT.NONE);
		toolbar.setLayout(new GridLayout(9,false));
		GridData gdtoolbar = new GridData();
		gdtoolbar.grabExcessHorizontalSpace = true;
		gdtoolbar.horizontalAlignment = SWT.FILL;
		toolbar.setLayoutData(gdtoolbar);		

		ImageHyperlink ihNew = new ImageHyperlink(toolbar, SWT.WRAP);	
		ihNew.setToolTipText("nuovo criterio");
		ihNew.setImage(Activator.getImageDescriptor("/icons/wizardexport/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((ExporterWizard)getWizard()).getExporterVO()
												  .getCriteriRicerca()
												  .size() == 0) ||
					(((ColloquiCriteriRicercaVO)((ExporterWizard)getWizard()).getExporterVO()
																			 .getCriteriRicerca()
																			 .get(((ExporterWizard)getWizard()).getExporterVO()
																			 .getCriteriRicerca().size() - 1)).getGetterMethodName() != null)){
					
					ColloquiCriteriRicercaVO crVO = new ColloquiCriteriRicercaVO();
					ColloquiCriteriRicercaModel crModel = new ColloquiCriteriRicercaModel(crVO);
					((ExporterWizard)getWizard()).getExporterVO()
											.getCriteriRicerca()
											.add(crModel);					
					reloadLineNumber();
					Collections.sort(((ExporterWizard)getWizard()).getExporterVO()
															 .getCriteriRicerca(), 
								 comparatorLineNumber);
					tvCriteri.refresh();
					TableItem ti = tvCriteri.getTable().getItem(tvCriteri.getTable().getItemCount()-1);
					Object[] sel = new Object[1];
					sel[0] = ti.getData();
					
					StructuredSelection ss = new StructuredSelection(sel);
					
					tvCriteri.setSelection(ss, true);
					
					Event ev = new Event();
					ev.item = ti;
					ev.data = ti.getData();
					ev.widget = tvCriteri.getTable();
					tvCriteri.getTable().notifyListeners(SWT.Selection, ev);

				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash.png").createImage());
		ihCancella.setToolTipText("cancella criterio");
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash_hover.png").createImage());
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				StructuredSelection ss = (StructuredSelection)tvCriteri.getSelection();
				if ((ss != null) && (ss.getFirstElement() != null)){
					((ExporterWizard)getWizard()).getExporterVO()
					 							 .getCriteriRicerca()
					 							 .remove((ColloquiCriteriRicercaVO)ss.getFirstElement());
					reloadLineNumber();
					Collections.sort(((ExporterWizard)getWizard()).getExporterVO()
							 									  .getCriteriRicerca(), 
							 		 comparatorLineNumber);					
					tvCriteri.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
		
		});
		
		ImageHyperlink ihCheck = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihCheck.setImage(Activator.getImageDescriptor("/icons/wizardexport/spellcheck.png").createImage());
		ihCheck.setToolTipText("controllo sintassi");
		ihCheck.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/spellcheck_over.png").createImage());
		ihCheck.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				AnagraficheHelper ah = new AnagraficheHelper();
				try{
					ah.getAnagraficheByProperties(((ExporterWizard)getWizard()).getExporterVO()
																	   	       .getCriteriRicerca());
					MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_INFORMATION);
					mb.setText("Interrogazione dati");
					mb.setMessage("Interrogazione generata corretta");			
					mb.open();

				}catch(WrongCriteriaSequenceException wcse){
					MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_ERROR);
					mb.setText("Interrogazione dati");
					mb.setMessage("Interrogazione generata non corretta");			
					mb.open();												
				}
					
				((ExporterWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		Label lseparator = new Label(toolbar,SWT.FLAT);
		lseparator.setImage(Activator.getImageDescriptor("icons/wizardexport/separator.png").createImage());
		
		ImageHyperlink ihOpen = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihOpen.setImage(Activator.getImageDescriptor("/icons/wizardexport/fileopen.png").createImage());
		ihOpen.setToolTipText("apri ricerca salvata");
		ihOpen.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/fileopen_over.png").createImage());
		ihOpen.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				PopUpRicercaRicerche puRR = new PopUpRicercaRicerche(RicercheVO.RICERCHE_ANAGRAFICHE,
													 				 (ListaCriteriAnagrafiche)((ExporterWizard)getWizard()).getPage("Lista criteri selezione anagrafiche"),
													 				 "setRicerca");
				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		

		ImageHyperlink ihSave = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihSave.setImage(Activator.getImageDescriptor("/icons/wizardexport/document-save.png").createImage());
		ihSave.setToolTipText("salva ricerca");
		ihSave.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/document-save_over.png").createImage());
		ihSave.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((ExporterWizard)getWizard()).getExporterVO()
												  .getCriteriRicerca() != null) &&
					(((ExporterWizard)getWizard()).getExporterVO()
												  .getCriteriRicerca().size() != 0)){
					
					PopUpEditRicerca puER = new PopUpEditRicerca();
					getRicerca().setTipo(RicercheVO.RICERCHE_ANAGRAFICHE);
					puER.setRicerca(getRicerca());
					((RicercaWizard)getWizard()).getContainer().updateButtons();					
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench()
  														.getActiveWorkbenchWindow()
  														.getShell(), 
  											  "Salvataggio ricerca", 
											  "Inserire almeno un criterio");							
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		

		ImageHyperlink ihCancellaRicerca = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaRicerca.setImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash.png").createImage());
		ihCancellaRicerca.setToolTipText("cancella ricerca");
		ihCancellaRicerca.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash_hover.png").createImage());
		ihCancellaRicerca.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (ricerca != null){
					ExportedRicercheHelper rh = new ExportedRicercheHelper();
					if (!rh.deleteRicerca(ricerca)){
						MessageDialog.openError(PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(), 
												"Errore cancellazione ricerca", 
												"Si è verificato un errore nella cancellazione della ricerca");		
					}else{
						MessageDialog.openInformation(PlatformUI.getWorkbench()
								  								.getActiveWorkbenchWindow()
								  								.getShell(), 
								  					  "Cancellazione ricerca", 
													  "cancellazione ricerca eseguita con successo");		

						lRicercaSelectedName.setText("");
						ricerca = null;
					}
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench()
							  							.getActiveWorkbenchWindow()
							  							.getShell(), 
							  				  "Selezione ricerca", 
											  "Selezionare la ricerca da cancellare");		
					
				}

				((RicercaWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		Label lRicercaSelected = new Label(toolbar, SWT.FLAT);
		lRicercaSelected.setText("Ricerca selezionata : ");
		
		lRicercaSelectedName = new Label(toolbar, SWT.FLAT);
		GridData gdRicercaSelected = new GridData();
		gdRicercaSelected.grabExcessHorizontalSpace = true;
		gdRicercaSelected.horizontalAlignment = SWT.FILL;
		lRicercaSelectedName.setLayoutData(gdRicercaSelected);				

		tvCriteri = new CriteriaTablesHelper().getAnagraficheCriteriaTV(container, 
																		((ExporterWizard)getWizard()).getExporterVO()
																									 .getCriteriRicerca());
		
		tvCriteri.setInput(((ExporterWizard)getWizard()).getExporterVO()
				 										.getCriteriRicerca());
		
		setPageComplete(true);
		setControl(container);
	}
	
	public void loadCriteriColloquio(){		
	//	((ExporterWizard)getWizard()).getExporterVO().setCriteriRicerca(new ArrayList<CriteriRicercaModel>());
	}
	
	private void reloadLineNumber(){
		if (((ExporterWizard)getWizard()).getExporterVO()
				 						 .getCriteriRicerca() != null){
			int count = 0;
			for (Iterator<ColloquiCriteriRicercaModel> iterator = ((ExporterWizard)getWizard()).getExporterVO()
					 																	       .getCriteriRicerca()
					 																	       .iterator(); iterator.hasNext();) {
				ColloquiCriteriRicercaVO ccrVO = iterator.next(); 
				ccrVO.setLineNumber(count);
				count++;
			}
		}
	}
	
	private ColloquiCriteriRicercaVO getPrevCriterioByLineNumber(Integer lineNumber){
		ColloquiCriteriRicercaVO ccrVO = new ColloquiCriteriRicercaVO();
		ccrVO.setLineNumber(lineNumber);
		Collections.sort(((RicercaWizard)getWizard()).getRicerca()
				 .getCriteriAnagrafiche(), comparatorLineNumber);
		int index = Collections.binarySearch(((RicercaWizard)getWizard()).getRicerca()
				 														 .getCriteriAnagrafiche(), 
				 							 ccrVO, 
				 							 comparatorLineNumber);
		if (index > 0){
			return (ColloquiCriteriRicercaVO)((RicercaWizard)getWizard()).getRicerca()
			 															 .getCriteriAnagrafiche()
			 															 .get(index-1);
		}else{
			return (ColloquiCriteriRicercaVO)((RicercaWizard)getWizard()).getRicerca()
			 															 .getCriteriAnagrafiche()
			 															 .get(index);
		}
	}
	
	public void setRicerca(RicercheModel rm){
		ricerca = rm;
		lRicercaSelectedName.setText(ricerca.getNome());
		lRicercaSelectedName.pack();
		lRicercaSelectedName.redraw();		
		((ExporterWizard)getWizard()).getExporterVO()
									 .setCriteriRicerca((ArrayList)ricerca.getCriteri().clone());
		tvCriteri.setInput(((ExporterWizard)getWizard()).getExporterVO()
				   									    .getCriteriRicerca());
	}

	public RicercheModel getRicerca() {
		if (ricerca == null){
			ricerca = new RicercheModel();
			ricerca.setTipo(EnvSettingsFactory.getInstance()
	   				   						  .getTipologieColloqui()
	   				   						  .get(0)
	   				   						  .getCodTipologiaColloquio());
		}
		ArrayList al = (ArrayList)((ExporterWizard)getWizard()).getExporterVO()
        													   .getCriteriRicerca().clone();

		ArrayList <ColloquiCriteriRicercaModel> alm = new ArrayList<ColloquiCriteriRicercaModel>();
		Iterator it = al.iterator();
		while (it.hasNext()) {
			ColloquiCriteriRicercaModel object = new ColloquiCriteriRicercaModel((ColloquiCriteriRicercaVO) it.next());
			object.setCodColloquio(0);
			alm.add(object);
		}
		ricerca.setCriteri(alm);			
		
		return ricerca;
	}

}
