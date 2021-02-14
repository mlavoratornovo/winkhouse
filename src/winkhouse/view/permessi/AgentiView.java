package winkhouse.view.permessi;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.dao.AgentiDAO;
import winkhouse.view.permessi.handler.DettaglioPermessiAgentiHandler;
import winkhouse.vo.AgentiVO;

public class AgentiView extends ViewPart {

	public final static String ID = "winkhouse.agentiview";
	
	private TableViewer viewer;
	private Image agentiImg = Activator.getImageDescriptor("icons/looknfeel.png").createImage();
	public AgentiView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				AgentiVO aVO = (AgentiVO)((StructuredSelection)viewer.getSelection()).getFirstElement();
				DettaglioPermessiAgenteView div = DettaglioPermessiAgentiHandler.getInstance()
																		        .getDettaglioPermessiAgenti(aVO);

				
			}
		});
		
		TableColumn tcNome = new TableColumn(viewer.getTable(),SWT.CENTER,0);
		tcNome.setText("");
		tcNome.setWidth(25);
		
		TableColumn tcCognome = new TableColumn(viewer.getTable(),SWT.CENTER,1);
		tcCognome.setText("Nome");
		tcCognome.setWidth(150);
		
		TableColumn tcCitta = new TableColumn(viewer.getTable(),SWT.CENTER,2);
		tcCitta.setText("Cognome");
		tcCitta.setWidth(150);
		viewer.setInput(new Object());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	class ViewContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {			
			return new AgentiDAO().list(AgentiVO.class.getName()).toArray();
		}

		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		
	}

	class ViewLabelProvider implements ITableLabelProvider {
		
		@Override
		public void addListener(ILabelProviderListener listener) {}

		@Override
		public void dispose() {}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0){
				return agentiImg;
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch(columnIndex){
			case 1 : return ((AgentiVO)element).getNome();
			case 2 : return ((AgentiVO)element).getCognome();
			default:return "";
			}
		}
	}

}
