package winkhouse.export.exporter;

import org.eclipse.jface.resource.ImageDescriptor;

public interface IWinkPlugin {

	String getText();
	
	ImageDescriptor getImage();
	
	String getTooltip();
	
	void run();
	
}
