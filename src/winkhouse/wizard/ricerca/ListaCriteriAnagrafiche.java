package winkhouse.wizard.ricerca;

import java.text.SimpleDateFormat;
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

import winkhouse.Activator;
import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.helper.RicercheHelper;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.RicercheModel;
import winkhouse.orm.Classicliente;
import winkhouse.util.CriteriaTableUtilsFactory;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.RicercheVO;
import winkhouse.wizard.PopUpEditRicerca;
import winkhouse.wizard.PopUpRicercaRicerche;
import winkhouse.wizard.RicercaWizard;



public class ListaCriteriAnagrafiche extends WizardPage{

	private Composite container = null;
	private TableViewer tvCriteri = null;
	private String[] desGetters = null;
	private String[] desClassiClienti = null;
	private Integer[] codClassiClienti = null;
	private String[] desAgenti = null;
	private Integer[] codAgenti = null;
	private Label lRicercaSelectedName = null;	
	private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd");


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

	private Comparator<WinkhouseUtils.ObjectSearchGetters> comparatorAnagraficaSearchGetters = new Comparator<WinkhouseUtils.ObjectSearchGetters>(){

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
		setTitle(getName());
		fillDescrizioniGetters();
		fillClassiClienti();
		fillDescrizioniAgenti();
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
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((RicercaWizard)getWizard()).getRicerca()
												 .getCriteriAnagrafiche()
												 .size() == 0) ||
					(((ColloquiCriteriRicercaVO)((RicercaWizard)getWizard()).getRicerca()
							 											    .getCriteriAnagrafiche()
							 											    .get(((RicercaWizard)getWizard())
							 											    .getRicerca()
							 											    .getCriteriAnagrafiche().size() - 1))
							 											    .getGetterMethodName() != null)){
					
					ColloquiCriteriRicercaVO crVO = new ColloquiCriteriRicercaVO();
					ColloquiCriteriRicercaModel crModel = new ColloquiCriteriRicercaModel(crVO);
					
					((RicercaWizard)getWizard()).getRicerca()
					 							.getCriteriAnagrafiche()
					 							.add(crModel);
					
					reloadLineNumber();
					Collections.sort(((RicercaWizard)getWizard()).getRicerca()
							 									 .getCriteriAnagrafiche(), 
							 		 comparatorLineNumber);
					
					tvCriteri.setInput(((RicercaWizard)getWizard()).getRicerca()
							   									   .getCriteriAnagrafiche());
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
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setToolTipText("cancella criterio");
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				StructuredSelection ss = (StructuredSelection)tvCriteri.getSelection();
				if ((ss != null) && (ss.getFirstElement() != null)){
					((RicercaWizard)getWizard()).getRicerca()
					 							.getCriteriAnagrafiche()
					 							.remove((ColloquiCriteriRicercaVO)ss.getFirstElement());
					reloadLineNumber();
					Collections.sort(((RicercaWizard)getWizard()).getRicerca()
							 									 .getCriteriAnagrafiche(), 
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
		ihCheck.setImage(Activator.getImageDescriptor("/icons/spellcheck.png").createImage());
		ihCheck.setToolTipText("controllo sintassi");
		ihCheck.setHoverImage(Activator.getImageDescriptor("/icons/spellcheck_over.png").createImage());
		ihCheck.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (((RicercaWizard)getWizard()).checkCriteriSyntax(true,WinkhouseUtils.ANAGRAFICHE)){
					SearchEngineAnagrafiche sei = new SearchEngineAnagrafiche(((RicercaWizard)getWizard()).getRicerca()
							 																			  .getCriteriAnagrafiche());
					if (sei.verifyQuery()){
						MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_INFORMATION);
						mb.setText("Interrogazione dati");
						mb.setMessage("Interrogazione generata corretta");			
						mb.open();

					}else{
						MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_ERROR);
						mb.setText("Interrogazione dati");
						mb.setMessage("Interrogazione generata non corretta");			
						mb.open();						
					}
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

		Label lseparator = new Label(toolbar,SWT.FLAT);
		lseparator.setImage(Activator.getImageDescriptor("icons/separator.png").createImage());
		
		ImageHyperlink ihOpen = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihOpen.setImage(Activator.getImageDescriptor("/icons/fileopen.png").createImage());
		ihOpen.setToolTipText("apri ricerca salvata");
		ihOpen.setHoverImage(Activator.getImageDescriptor("/icons/fileopen_over.png").createImage());
		ihOpen.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				int tiporicerca;
				if(((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.PERMESSI){
					tiporicerca = RicercheVO.PERMESSI_ANAGRAFICHE;
					
				}else{
					tiporicerca = RicercheVO.RICERCHE_ANAGRAFICHE;
				}
				PopUpRicercaRicerche puRR = new PopUpRicercaRicerche(tiporicerca,
													 				 (ListaCriteriAnagrafiche)((RicercaWizard)getWizard()).getPage("Lista criteri selezione anagrafiche"),
													 				 "setRicerca");
				
				((RicercaWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		

		ImageHyperlink ihSave = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihSave.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihSave.setToolTipText("salva ricerca");
		ihSave.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihSave.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((RicercaWizard)getWizard()).getRicerca()
												 .getCriteriAnagrafiche() != null) &&
					(((RicercaWizard)getWizard()).getRicerca()
												 .getCriteriAnagrafiche().size() != 0)){
					int tiporicerca;
					if(((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.PERMESSI){
						tiporicerca = RicercheVO.PERMESSI_ANAGRAFICHE;
						
					}else{
						tiporicerca = RicercheVO.RICERCHE_ANAGRAFICHE;
					}
					
					PopUpEditRicerca puER = new PopUpEditRicerca();
					getRicerca().setTipo(tiporicerca);
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
		ihCancellaRicerca.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaRicerca.setToolTipText("cancella ricerca");
		ihCancellaRicerca.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaRicerca.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (((RicercaWizard)getWizard()).getRicerca().getRicerca() != null){
					RicercheHelper rh = new RicercheHelper();
					if (!rh.deleteRicerca(((RicercaWizard)getWizard()).getRicerca().getRicerca(),((RicercaWizard)getWizard()).getWiztype())){
						MessageDialog.openError(PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(), 
												"Errore cancellazione ricerca", 
												"Si � verificato un errore nella cancellazione della ricerca");		
					}else{
						MessageDialog.openInformation(PlatformUI.getWorkbench()
								  								.getActiveWorkbenchWindow()
								  								.getShell(), 
								  					  "Cancellazione ricerca", 
													  "cancellazione ricerca eseguita con successo");		

						lRicercaSelectedName.setText("");
						((RicercaWizard)getWizard()).getRicerca().setRicerca(null);
						
						if (((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.PERMESSI){
							((RicercaWizard)getWizard()).getRicerca().setCriteriAnagrafiche(new ArrayList());
						}
						
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
		
		CriteriaTableUtilsFactory ctuf = new CriteriaTableUtilsFactory();
		
		tvCriteri = ctuf.getSearchAnagraficheCriteriaTable(container,
														   ((RicercaWizard)getWizard()).getRicerca()
				   																	   .getCriteriAnagrafiche());
		
		tvCriteri.setInput(((RicercaWizard)getWizard()).getRicerca()
													   .getCriteriAnagrafiche());

		setPageComplete(true);
		setControl(container);
	}
	
	public void fillDescrizioniGetters(){						
		Collections.sort(WinkhouseUtils.getInstance().getAnagraficheSearchGetters(), comparatorAnagraficaSearchGetters);
		desGetters = new String[WinkhouseUtils.getInstance().getAnagraficheSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = WinkhouseUtils.getInstance().getAnagraficheSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetters[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	public void fillDescrizioniAgenti(){						
		desAgenti = new String[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		codAgenti = new Integer[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		Iterator<AgentiVO> it = MobiliaDatiBaseCache.getInstance().getAgenti().iterator();
		int count = 0;
		while(it.hasNext()){
			AgentiVO aVO = it.next();
			desAgenti[count] = aVO.getCognome() + " " + aVO.getNome();
			codAgenti[count] = aVO.getCodAgente();
			count++;
		}		
	}
	
	public void fillClassiClienti(){						
		desClassiClienti = new String[MobiliaDatiBaseCache.getInstance().getClassiClienti().size()];
		codClassiClienti = new Integer[MobiliaDatiBaseCache.getInstance().getClassiClienti().size()];
		Iterator<Classicliente> it = MobiliaDatiBaseCache.getInstance().getClassiClienti().iterator();
		int count = 0;
		while(it.hasNext()){
			Classicliente ccVO = it.next();
			desClassiClienti[count] = ccVO.getDescrizione();
			codClassiClienti[count] = ccVO.getId();
			count++;
		}		
	}

	public void loadCriteriColloquio(){		
		if (((RicercaWizard)getWizard()).getRicerca().getCriteriAnagrafiche() == null){
			((RicercaWizard)getWizard()).getRicerca().setCriteriAnagrafiche(new ArrayList<ColloquiCriteriRicercaVO>());	
		}
		
	}
	
	private void reloadLineNumber(){
		if (((RicercaWizard)getWizard()).getRicerca()
				 .getCriteriAnagrafiche() != null){
			int count = 0;
			for (Iterator<ColloquiCriteriRicercaVO> iterator = ((RicercaWizard)getWizard()).getRicerca()
					 																	   .getCriteriAnagrafiche()
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
		if (((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.PERMESSI){
			if (rm.getTipo() == RicercheVO.PERMESSI_IMMOBILI){
				rm.setTipo(RicercheVO.RICERCHE_IMMOBILI);
			}
			if (rm.getTipo() == RicercheVO.PERMESSI_ANAGRAFICHE){
				rm.setTipo(RicercheVO.RICERCHE_ANAGRAFICHE);
			}
			if (rm.getTipo() == RicercheVO.PERMESSI_IMMOBILI_AFFITTI){
				rm.setTipo(RicercheVO.RICERCHE_IMMOBILI_AFFITTI);
			}

		}		
		((RicercaWizard)getWizard()).getRicerca().setRicerca(rm);
		lRicercaSelectedName.setText(((RicercaWizard)getWizard()).getRicerca().getRicerca().getNome());
		lRicercaSelectedName.pack();
		lRicercaSelectedName.redraw();		
		((RicercaWizard)getWizard()).getRicerca()
									.setCriteriAnagrafiche((ArrayList)((RicercaWizard)getWizard()).getRicerca().getRicerca().getCriteri().clone());
		tvCriteri.setInput(((RicercaWizard)getWizard()).getRicerca()
				   									   .getCriteriAnagrafiche());
		((RicercaWizard)getWizard()).getContainer().updateButtons();
	}

	public RicercheModel getRicerca() {
		if (((RicercaWizard)getWizard()).getRicerca().getRicerca() == null){
			((RicercaWizard)getWizard()).getRicerca().setRicerca(new RicercheModel());
			((RicercaWizard)getWizard()).getRicerca().getRicerca().setTipo(EnvSettingsFactory.getInstance()
	   				   						  					  .getTipologieColloqui()
	   				   						  					  .get(0)
	   				   						  					  .getCodTipologiaColloquio());
		}
		ArrayList al = (ArrayList)((RicercaWizard)getWizard()).getRicerca()
        													  .getCriteriAnagrafiche().clone();

		ArrayList <ColloquiCriteriRicercaModel> alm = new ArrayList<ColloquiCriteriRicercaModel>();
		Iterator it = al.iterator();
		while (it.hasNext()) {
			ColloquiCriteriRicercaModel object = (ColloquiCriteriRicercaModel) it.next();
			object.setCodColloquio(null);
			alm.add(object);
		}
		((RicercaWizard)getWizard()).getRicerca().getRicerca().setCriteri(alm);			
		
		return ((RicercaWizard)getWizard()).getRicerca().getRicerca();
	}

}
