package com.fit.process.cdi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.InjectionPoint;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.cdi.condition.CDINamedCondition;

public class CDIProcessor2 {
	private static final String INJEXTION_ANOTATION = "@Inject";
	private static final String PRODUCES_ANOTATION = "@Produces";
	private static final String OBSERVES_ANOTATION = "@Observes";
	/** Node goc project */
	private ProjectNode projectNode;

	public static void main(String[] args) {
		String projectRootPath = "C://Users//son//Downloads//dukes-forest//dukes-forest";
//		String projectRootPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		CDIProcessor2 processor = new CDIProcessor2();
		processor.setProjectNode(projectNode);

		processor.process();

	}
	
	public void process(){
		List<InjectionPoint> points = findInjectionPointsInInjector();
		updateInjectionPointsWithInjectee(points);
		updateProjectNode();
	}
	private void updateProjectNode() {
		
	}
	private void updateInjectionPointsWithInjectee(List<InjectionPoint> points) {
		
	}

	private List<InjectionPoint> findInjectionPointsInInjector() {
		List<InjectionPoint> points = new ArrayList<InjectionPoint>();
		List<Node> namedNodes = Search.searchNode(projectNode, new CDINamedCondition());
		InjectionPoint point = null;
		for (Node node : namedNodes) {
			ClassFileParser classFileParser = new ClassFileParser(node.getPath());
			// Kiem tra cac field
			for (FieldDeclaration field : classFileParser.getListFieldDeclaration()) {
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
