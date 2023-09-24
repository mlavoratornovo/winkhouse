package winkhouse.wizard.colloqui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.util.CriteriaTableUtilsFactory;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.wizard.ColloquiWizard;



public class CriteriRicercaColloquio extends WizardPage {

	private Composite container = null;
	private TableViewer tvCriteri = null;
	private String[] desGetters = null;
	private String[] desTipologiaImmobile = null;
	private Integer[] codTipologiaImmobile = null;
	private String[] desStatoConservativo = null;
	private Integer[] codStatoConservativo = null;
	private String[] desRiscaldamenti = null;
	private Integer[] codRiscaldamenti = null;
	private String[] desAgenti = null;
	private Integer[] codAgenti = null;
	private ArrayList<ColloquiCriteriRicercaVO> criteri = null;
	private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd");

	
	public CriteriRicercaColloquio(String pageName) {
		super(pageName);
	}

	public CriteriRicercaColloquio(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	private Comparator<WinkhouseUtils.ObjectSearchGetters> comparatorImmobileSearchGetters = new Comparator<WinkhouseUtils.ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters arg0,
						   ObjectSearchGetters arg1) {
			return arg0.getMethodName().compareToIgnoreCase(arg1.getMethodName());			
		}
		
	};

	private Comparator c = new Comparator<String>(){

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
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
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = new ImageHyperlink(toolbar, SWT.WRAP);	
		ihNew.setToolTipText("nuovo criterio");
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((criteri.size() == 0) ||
					(((ColloquiCriteriRicercaVO)criteri.get(criteri.size() - 1)).getGetterMethodName() != null)){
					
					ColloquiCriteriRicercaVO crVO = new ColloquiCriteriRicercaVO();
					ColloquiCriteriRicercaModel crModel = new ColloquiCriteriRicercaModel(crVO); 
					
					criteri.add(crModel);
					reloadLineNumber();
					Collections.sort(criteri, comparatorLineNumber);
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
					criteri.remove((ColloquiCriteriRicercaVO)ss.getFirstElement());
					reloadLineNumber();
					Collections.sort(criteri, comparatorLineNumber);					
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
				if (((ColloquiWizard)getWizard()).checkCriteriSyntax(true)){
					SearchEngineImmobili sei = new SearchEngineImmobili(criteri);
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
				((ColloquiWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		CriteriaTableUtilsFactory ctuf = new CriteriaTableUtilsFactory();
		
		tvCriteri = ctuf.getSearchImmobiliCriteriaTable(container,
														((ColloquiWizard)getWizard()).getColloquio()
				   																	 .getCriteriRicerca());
		
		tvCriteri.setInput(((ColloquiWizard)getWizard()).getColloquio()
											 		    .getCriteriRicerca());

		setPageComplete(true);
		setControl(container);
		
	}

	
	public void loadCriteriColloquio(){
		criteri = new ArrayList<ColloquiCriteriRicercaVO>();
		((ColloquiWizard)getWizard()).getColloquio().setCriteriRicerca(criteri);
	}
	
	private void reloadLineNumber(){
		if (criteri != null){
			int count = 0;
			for (Iterator<ColloquiCriteriRicercaVO> iterator = criteri.iterator(); iterator.hasNext();) {
				ColloquiCriteriRicercaVO ccrVO = iterator.next(); 
				ccrVO.setLineNumber(count);
				count++;
			}
		}
	}
	
	private ColloquiCriteriRicercaVO getPrevCriterioByLineNumber(Integer lineNumber){
		ColloquiCriteriRicercaVO ccrVO = new ColloquiCriteriRicercaVO();
		ccrVO.setLineNumber(lineNumber);
		Collections.sort(criteri, comparatorLineNumber);
		int index = Collections.binarySearch(criteri, ccrVO, comparatorLineNumber);
		if (index > 0){
			return criteri.get(index-1);
		}else{
			return criteri.get(index);
		}
	}
}

