package winkhouse.view.desktop.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.swt.graphics.Image;

public class NodeFigure extends ImageFigure {
	
	private String text = null;
	
	public NodeFigure() {
	}

	public NodeFigure(Image image,String text) {
		super(image);
		this.text = text;
	}

	public NodeFigure(Image image, int alignment) {
		super(image, alignment);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.drawText(compactText(this.text), super.getLocation().x+10, super.getLocation().y+10);
	}

	private String[] compactTextUnit(String text,String output){
		
		String[] returnValue = new String[2];
		
		int x = 22 - output.length();
		if (text.length() <= output.length()){
			output += text;
			text = "";
		}else{
			if (x < text.length()){
				output += text.substring(0, x);
				text = text.substring(x);				
			}else{
				output += text;
				text = "";
			}
			
		}
		
		returnValue[0] = output;
		returnValue[1] = text;
		
		return returnValue;

	}
	
	private String compactText(String text){
		
		String work_text = "";
		
		text = text.replaceAll("\r\n", " ");
		
		String[] work_text_array = new String[6];
		
		work_text_array[0] = "";
		work_text_array[1] = "";
		work_text_array[2] = "";
		work_text_array[3] = "";
		work_text_array[4] = "";
		work_text_array[5] = "";
					
		int rowcounts = text.toCharArray().length / 22;		
		int b = 0;
		int e = 22;
		
		rowcounts = (rowcounts == 0)? 1 : rowcounts;
		rowcounts = (rowcounts > 6)? 6 : rowcounts;
		
		for (int i = 0; i <= rowcounts - 1; i++) {
			
			if (text.length() < 22){
				work_text_array[i] = text;
			}else{
				work_text_array[i] = text.substring(b, e);
				b = e;
				e = e + 22;
			}
			
		}
		
		for (String wta : work_text_array) {
			work_text += wta + "\n";
		}
		
		return work_text;
	}
}
