package winkhouse.view.preference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;

public class WinkCloudPage extends PreferencePage {
	
	private Form form = null;
	private TableViewer turls = null;
	private ArrayList<String> urlsvalue = new ArrayList<String>();
	private TextCellEditor tceurl = null;
	private Image addbutton = Activator.getImageDescriptor("icons/filenew.png").createImage();
	private Image delbutton = Activator.getImageDescriptor("icons/edittrash.png").createImage();
	private Image winkcloudbutton = Activator.getImageDescriptor("icons/winkclouid.png").createImage();
	private Text winkID = null;
	
	public WinkCloudPage() {
	}

	public WinkCloudPage(String title) {
		super(title);
	}

	public WinkCloudPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 8;
		gridLayout.horizontalSpacing = 0;

		form.getBody().setLayout(gridLayout);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.minimumHeight = 25;
		GridData gridDataHV = new GridData(SWT.FILL, SWT.FILL, true, true);

		
		toolkit.paintBordersFor(form.getBody());
		
		Label l_uuid = toolkit.createLabel(form.getBody(), "ID WinkCloud");
		l_uuid.setBackground(l_uuid.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		Composite cid = toolkit.createComposite(form.getBody());
		cid.setLayout(new GridLayout(2,false));
		cid.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		cid.setBackground(cid.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		winkID = toolkit.createText(cid, WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudid"), SWT.FLAT);
		winkID.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		
		Button getid = toolkit.createButton(cid, null, SWT.FLAT);
		getid.setImage(winkcloudbutton);
		getid.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				winkID.setText(UUID.randomUUID().toString().replace("-", ""));
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		Label l_login = toolkit.createLabel(form.getBody(), "Url servizio winkcloud");
		l_login.setBackground(l_login.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Composite cbuttons = toolkit.createComposite(form.getBody());
		cbuttons.setLayout(new RowLayout());
		cbuttons.setBackground(cbuttons.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Button add = toolkit.createButton(cbuttons, null, SWT.FLAT);
		add.setImage(addbutton);
		add.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				urlsvalue = (urlsvalue == null)? new ArrayList<String>():urlsvalue;
				urlsvalue.add("nuovo url");
				turls.setInput(urlsvalue);
				
				TableItem ti = turls.getTable().getItem(turls.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();
		
				StructuredSelection ss = new StructuredSelection(sel);
				
				turls.setSelection(ss, true);

			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		Button del = toolkit.createButton(cbuttons, null, SWT.FLAT);
		del.setImage(delbutton);
		del.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				if (turls.getSelection() != null && ((StructuredSelection)turls.getSelection()).getFirstElement() != null){
					
					for (Iterator iterator = urlsvalue.iterator(); iterator.hasNext();) {
						String string = (String) iterator.next();
						if (string.equalsIgnoreCase((String)((StructuredSelection)turls.getSelection()).getFirstElement())){
							urlsvalue.remove(string);
							break;
						}						
					}
					
					turls.setInput(urlsvalue);
					
				}else{
					MessageDialog.openWarning(turls.getTable().getShell(), "Eliminazione url", "Selezionare un url da eliminare");
				}	
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		turls = new TableViewer(form.getBody());
		turls.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
			@Override
			public void dispose() {
			}
			
			@Override
			public Object[] getElements(Object inputElement) {	
				return (inputElement instanceof ArrayList)?(((ArrayList<String>)inputElement).toArray()):null;
			}
		});		
		
		turls.getTable().setLayoutData(gridDataHV);
		turls.getTable().setLinesVisible(true);		
		turls.getTable().setHeaderVisible(true);
		
	    TableColumn column = new TableColumn(turls.getTable(),SWT.CENTER,0);
	    column.setText("Url");
	    column.setWidth(300);
	    
	    tceurl = new TextCellEditor(turls.getTable());
	    
	    TableViewerColumn tvc = new TableViewerColumn(turls, column);
	    tvc.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				String url = ((String)cell.getElement());
				cell.setText(url);				
			}
		});
	    
	    tvc.setEditingSupport(new EditingSupport(turls) {
			
			@Override
			protected void setValue(Object element, Object value) {
				
				for (int i = 0; i < urlsvalue.size(); i++) {
					if (urlsvalue.get(i).equalsIgnoreCase((String)element)){
						urlsvalue.set(i, (String)value);
						break;
					}
				}
				turls.refresh();
						
			}
			
			@Override
			protected Object getValue(Object element) {				
				return element;
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return tceurl;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
	    
	    String urls = WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudurls");
	    if ((urls != null) && (urls.trim().equalsIgnoreCase(""))){
	    	urls = WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString("winkcloudurls");
	    }
	    String[] values = urls.split(";");
	    if (values.length == 1 && values[0].equalsIgnoreCase("")){
	    	urlsvalue = null;	
	    }else{
	    	urlsvalue = new ArrayList<String>(Arrays.asList(values));
	    }
	    
	    
	    turls.setInput(urlsvalue);

		return form;
	}

	@Override
	protected void performApply() {
		
		StringBuffer stringvalue = new StringBuffer();
		
		for (Iterator iterator = urlsvalue.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			stringvalue.append(string);
			stringvalue.append(";");			
		}
		
		if (stringvalue.length()>0){
			stringvalue = stringvalue.deleteCharAt(stringvalue.length()-1);	
		}
				
		WinkhouseUtils.getInstance().getPreferenceStore().setValue("winkcloudurls", stringvalue.toString());
		WinkhouseUtils.getInstance().getPreferenceStore().setValue("winkcloudid", winkID.getText().trim());
		
		try {
			WinkhouseUtils.getInstance().getPreferenceStore().save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
