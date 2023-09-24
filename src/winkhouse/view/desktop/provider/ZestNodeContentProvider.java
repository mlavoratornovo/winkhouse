package winkhouse.view.desktop.provider;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityRelationshipContentProvider;

import winkhouse.view.desktop.model.MyConnection;
import winkhouse.view.desktop.model.MyNode;

public class ZestNodeContentProvider extends ArrayContentProvider 
									 implements IGraphEntityRelationshipContentProvider {

	public ZestNodeContentProvider() {}

//	@Override
//	public Object[] getConnectedTo(Object arg0) {
//		
//		if (arg0 instanceof MyNode) {
//		      MyNode node = (MyNode) arg0;
//		      if (node.getConnectedTo() != null){
//		    	  return node.getConnectedTo().toArray();
//		      }else{
//		    	  return null;
//		      }
//		    	  
//		
//		}
//		
//		throw new RuntimeException("Type not supported");	
//	}

	@Override
	public Object[] getRelationships(Object source, Object dest) {
		ArrayList<MyConnection> connections = new ArrayList<MyConnection>();
		
		if (((MyNode)source).getConnectedTo().contains(dest)){
			connections.add(new MyConnection(source.toString()+"_"+ dest.toString(),
											 "",
											 (MyNode)source,
											 (MyNode)dest));
		}
	
	
//		if (((MyNode)dest).getConnectedTo().contains(source)){
//			connections.add(new MyConnection(source.toString()+"_"+ dest.toString(),
//											 "",
//											 (MyNode)source,
//											 (MyNode)dest));
//		}
		
		
		return connections.toArray();
	}


}
