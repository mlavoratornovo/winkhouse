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
import winkhouse.engine.search.SearchEngineColloqui;
import winkhouse.helper.RicercheHelper;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.orm.Agenti;
import winkhouse.orm.Colloquicriteriricerca;
import winkhouse.orm.Ricerche;
import winkhouse.util.CriteriaTableUtilsFactory;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.RicercheVO;
import winkhouse.wizard.PopUpEditRicerca;
import winkhouse.wizard.PopUpRicercaRicerche;
import winkhouse.wizard.RicercaWizard;



public class ListaCriteriColloqui extends WizardPage{

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

	
	public ListaCriteriColloqui(String pageName) {
		super(pageName);
	}
	
	private Comparator c = new Comparator<String>(){

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	};

	public ListaCriteriColloqui(String pageName, String title,
								   ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	private Comparator<WinkhouseUtils.ObjectSearchGetters> comparatorColloquiSearchGetters = new Comparator<WinkhouseUtils.ObjectSearchGetters>(){

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
//		fillClassiClienti();
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
												 .getCriteriColloqui()
												 .size() == 0) ||
					(((ColloquiCriteriRicercaVO)((RicercaWizard)getWizard()).getRicerca()
							 											    .getCriteriColloqui()
							 											    .get(((RicercaWizard)getWizard())
							 											    .getRicerca()
							 											    .getCriteriColloqui().size() - 1))
							 											    .getGetterMethodName() != null)){
					
					ColloquiCriteriRicercaVO crVO = new ColloquiCriteriRicercaVO();
					ColloquiCriteriRicercaModel crModel = new ColloquiCriteriRicercaModel(crVO);
					
					((RicercaWizard)getWizard()).getRicerca()
					 							.getCriteriColloqui()
					 							.add(crModel);
					
					reloadLineNumber();
					Collections.sort(((RicercaWizard)getWizard()).getRicerca()
							 									 .getCriteriColloqui(), 
							 		 comparatorLineNumber);
					
					tvCriteri.setInput(((RicercaWizard)getWizard()).getRicerca()
							   									   .getCriteriColloqui());
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
					 							.getCriteriColloqui()
					 							.remove((ColloquiCriteriRicercaVO)ss.getFirstElement());
					reloadLineNumber();
					Collections.sort(((RicercaWizard)getWizard()).getRicerca()
							 									 .getCriteriColloqui(), 
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
				if (((RicercaWizard)getWizard()).checkCriteriSyntax(true,WinkhouseUtils.COLLOQUI)){
					SearchEngineColloqui sei = new SearchEngineColloqui(((RicercaWizard)getWizard()).getRicerca()
							 																		.getCriteriColloqui());
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
					tiporicerca = RicercheVO.PERMESSI_COLLOQUI;
					
				}else{
					tiporicerca = RicercheVO.RICERCHE_COLLOQUI;
				}
				PopUpRicercaRicerche puRR = new PopUpRicercaRicerche(tiporicerca,
													 				 (ListaCriteriColloqui)((RicercaWizard)getWizard()).getPage("Lista criteri selezione colloqui"),
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
												 .getCriteriColloqui() != null) &&
					(((RicercaWizard)getWizard()).getRicerca()
												 .getCriteriColloqui().size() != 0)){
					int tiporicerca;
					if(((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.PERMESSI){
						tiporicerca = RicercheVO.PERMESSI_COLLOQUI;
						
					}else{
						tiporicerca = RicercheVO.RICERCHE_COLLOQUI;
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
				if (((RicercaWizard)getWizard()).getRicerca() != null){
					RicercheHelper rh = new RicercheHelper();
					if (!rh.deleteRicerca(((RicercaWizard)getWizard()).getRicerca(),((RicercaWizard)getWizard()).getWiztype())){
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
						((RicercaWizard)getWizard()).setRicerca(null);
						
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
		tvCriteri = ctuf.getSearchColloquiCriteriaTable(container, ((RicercaWizard)getWizard()).getRicerca()
				   																			   .getCriteriColloqui());
//		tvCriteri = ctuf.getSearchAnagraficheCriteriaTable(container,
//														   ((RicercaWizard)getWizard()).getRicerca()
//				   																	   .getCriteriColloqui());
		
		tvCriteri.setInput(((RicercaWizard)getWizard()).getRicerca()
													   .getCriteriColloqui());

		setPageComplete(true);
		setControl(container);
	}
	
	public void fillDescrizioniGetters(){						
		Collections.sort(WinkhouseUtils.getInstance().getColloquiSearchGetters(), comparatorColloquiSearchGetters);
		desGetters = new String[WinkhouseUtils.getInstance().getColloquiSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = WinkhouseUtils.getInstance().getColloquiSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetters[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	public void fillDescrizioniAgenti(){						
		desAgenti = new String[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		codAgenti = new Integer[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		Iterator<Agenti> it = MobiliaDatiBaseCache.getInstance().getAgenti().iterator();
		int count = 0;
		while(it.hasNext()){
			Agenti aVO = it.next();
			desAgenti[count] = aVO.getCognome() + " " + aVO.getNome();
			codAgenti[count] = aVO.getCodAgente();
			count++;
		}		
	}
	
//	public void fillClassiClienti(){						
//		desClassiClienti = new String[MobiliaDatiBaseCache.getInstance().getClassiClienti().size()];
//		codClassiClienti = new Integer[MobiliaDatiBaseCache.getInstance().getClassiClienti().size()];
//		Iterator<ClassiClientiVO> it = MobiliaDatiBaseCache.getInstance().getClassiClienti().iterator();
//		int count = 0;
//		while(it.hasNext()){
//			ClassiClientiVO ccVO = it.next();
//			desClassiClienti[count] = ccVO.getDescrizione();
//			codClassiClienti[count] = ccVO.getCodClasseCliente();
//			count++;
//		}		
//	}

	public void loadCriteriColloquio(){		
		if (((RicercaWizard)getWizard()).getRicerca().getCriteriColloqui() == null){
			((RicercaWizard)getWizard()).getRicerca().setCriteriColloqui(new ArrayList<ColloquiCriteriRicercaVO>());	
		}
		
	}
	
	private void reloadLineNumber(){
		if (((RicercaWizard)getWizard()).getRicerca()
				 .getCriteriColloqui() != null){
			int count = 0;
			for (Iterator<ColloquiCriteriRicercaVO> iterator = ((RicercaWizard)getWizard()).getRicerca()
					 																	   .getCriteriColloqui()
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
				 .getCriteriColloqui(), comparatorLineNumber);
		int index = Collections.binarySearch(((RicercaWizard)getWizard()).getRicerca()
				 														 .getCriteriColloqui(), 
				 							 ccrVO, 
				 							 comparatorLineNumber);
		if (index > 0){
			return (ColloquiCriteriRicercaVO)((RicercaWizard)getWizard()).getRicerca()
			 															 .getCriteriColloqui()
			 															 .get(index-1);
		}else{
			return (ColloquiCriteriRicercaVO)((RicercaWizard)getWizard()).getRicerca()
			 															 .getCriteriColloqui()
			 															 .get(index);
		}
	}
	
	public void setRicerca(Ricerche rm){
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
			if (rm.getTipo() == RicercheVO.PERMESSI_COLLOQUI){
				rm.setTipo(RicercheVO.RICERCHE_COLLOQUI);
			}

		}		
		((RicercaWizard)getWizard()).setRicerca(rm);
		lRicercaSelectedName.setText(((RicercaWizard)getWizard()).getRicerca().getNome());
		lRicercaSelectedName.pack();
		lRicercaSelectedName.redraw();		
		((RicercaWizard)getWizard()).getRicerca()
									.setCriteriAnagrafiche((ArrayList)((RicercaWizard)getWizard()).getRicerca().getCriteriColloqui().clone());
		tvCriteri.setInput(((RicercaWizard)getWizard()).getRicerca()
				   									   .getCriteriColloqui());
		((RicercaWizard)getWizard()).getContainer().updateButtons();
	}

	public Ricerche getRicerca() {
		if (((RicercaWizard)getWizard()).getRicerca() == null){
			((RicercaWizard)getWizard()).setRicerca(WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Ricerche.class));
			((RicercaWizard)getWizard()).getRicerca().setTipo(EnvSettingsFactory.getInstance()
	   				   						  					  .getTipologieColloqui()
	   				   						  					  .get(0)
	   				   						  					  .getCodTipologiaColloquio());
		}
		ArrayList al = (ArrayList)((RicercaWizard)getWizard()).getRicerca()
        													  .getCriteriColloqui().clone();

		ArrayList <Colloquicriteriricerca> alm = new ArrayList<Colloquicriteriricerca>();
		Iterator<Colloquicriteriricerca> it = al.iterator();
		while (it.hasNext()) {
			Colloquicriteriricerca object = it.next();
			object.setColloqui(null);
			alm.add(object);
		}
		((RicercaWizard)getWizard()).getRicerca().setCriteriColloqui(alm);			
		
		return ((RicercaWizard)getWizard()).getRicerca();
	}

}
