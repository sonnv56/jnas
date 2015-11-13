package com.fit.process.cdi;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.InjectionPoint;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.cdi.condition.CDICustomQualifierCondition;
import com.fit.process.cdi.condition.CDINamedCondition;
import com.fit.process.cdi.condition.CDIObservesCondition;
import com.fit.process.cdi.condition.CDIProducesCondition;

public class CDIProcessor2 {
	private static final String INJEXTION_ANOTATION = "@Inject";
	private static final String PRODUCES_ANOTATION = "@Produces";
	/** Node goc project */
	private ProjectNode projectNode;

	public static void main(String[] args) {
//		String projectRootPath = "C://Users//son//Downloads//dukes-forest//dukes-forest";
//		String projectRootPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
		String projectRootPath = "C://Users//Chicky//Desktop//Workspace//Project//CIA//javaee-tutorial-6//examples//case-studies//dukes-forest";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		CDIProcessor2 processor = new CDIProcessor2();
		processor.setProjectNode(projectNode);

		processor.process();

	}
	
	public void process(){
		List<Node> namedNodes = Search.searchNode(projectNode, new CDINamedCondition());
		List<InjectionPoint> points = findInjectionPointsInInjector(namedNodes);
		updateInjectionPointsWithInjectee(points);
		updateProjectNode();
	}
	private void updateProjectNode() {
		
		
	}
	private void updateInjectionPointsWithInjectee(List<InjectionPoint> points) {
		for (InjectionPoint injectionPoint : points) {
			String statement = injectionPoint.getStatement();
			int index = statement.indexOf(INJEXTION_ANOTATION);
			if(index!=-1){										//Xac dinh co phai su dung @Injection
				index = index+INJEXTION_ANOTATION.length()+1;
				int index2 = statement.indexOf(" ", index);
				//Lay qualifier
				String qualifier = statement.substring(index, index2);
				if(qualifier.indexOf("@") != -1){				//Neu la qualifier
					System.out.println(qualifier);
					//Bao qualifier o class
					List<Node> qualifiers = Search.searchNode(projectNode,new CDICustomQualifierCondition(qualifier));
					//Khai bao qualifier bang @Produces
					if(qualifiers.size() == 0){
						qualifiers = Search.searchNode(projectNode,new CDIProducesCondition(qualifier));
					}
					//Khai bao qualifier bang @Observes
					if(qualifiers.size() == 0){	
						qualifiers = Search.searchNode(projectNode,new CDIObservesCondition(qualifier));
					}
					//Xac dinh chinh xac (truong hop co nhieu qualifiers giong nhau)
					injectionPoint.setInjectee(getExactNode(qualifiers));
				}
			}
		}
	}

	private Node getExactNode(List<Node> qualifiers) {
		if(qualifiers.size() > 0){
			return qualifiers.get(0);
		}
		return null;
	}

	private List<InjectionPoint> findInjectionPointsInInjector(List<Node> namedNodes) {
		List<InjectionPoint> points = new ArrayList<InjectionPoint>();
		for (Node node : namedNodes) {
			ClassFileParser classFileParser = new ClassFileParser(node.getPath());
			// Kiem tra cac field
			for (FieldDeclaration field : classFileParser.getListFieldDeclaration()) {
				System.out.println(field.toString());
				if(field.toString().trim().indexOf(INJEXTION_ANOTATION)!=-1){
					
					points.add(createAInjectionPoint(node, field.toString().trim()));
				}
			}
			// Kiem tra cac method
			for (MethodDeclaration method : classFileParser .getListMethodDeclaration()) {
				if(method.toString().trim().indexOf(INJEXTION_ANOTATION)!=-1 
						|| method.toString().trim().indexOf(PRODUCES_ANOTATION)!=-1){
					points.add(createAInjectionPoint(node, method.toString().trim()));
				}
			}
		}
		return points;
	}



	private InjectionPoint createAInjectionPoint(Node node, String trim) {
		InjectionPoint point = new InjectionPoint();
		point.setInjector(node);
		point.setStatement(trim);
		return point;
	}

	public void setProjectNode(ProjectNode projectNode) {
		this.projectNode = projectNode;
	}
	public ProjectNode getProjectNode() {
		return projectNode;
	}
}
