package com.fit.process.cdi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.ClassNode;
import com.fit.object.InjectionPoint;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.cdi.condition.CDIBeanCondition;
import com.fit.process.cdi.condition.CDICustomQualifierCondition;
import com.fit.process.cdi.condition.CDIDefaultCondition;
import com.fit.process.cdi.condition.CDINamedCondition;
import com.fit.process.cdi.condition.CDIObservesCondition;
import com.fit.process.cdi.condition.CDIProducesCondition;
import com.fit.util.Utils;

public class CDIProcessor2 {
	private static final String INJEXTION_ANOTATION = "@Inject";
	private static final String PRODUCES_ANOTATION = "@Produces";
	/** Node goc project */
	private ProjectNode projectNode;

	public static void main(String[] args) {
//		String projectRootPath = "C://Users//son//Downloads//dukes-forest//dukes-forest";
		String projectRootPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
//		String projectRootPath = "C://Users//Chicky//Desktop//Workspace//Project//CIA//javaee-tutorial-6//examples//case-studies//dukes-forest";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		CDIProcessor2 processor = new CDIProcessor2();
		processor.setProjectNode(projectNode);

		processor.process();
		
	}
	
	public void process(){
		List<Node> namedNodes = Search.searchNode(projectNode, new CDINamedCondition());
		List<InjectionPoint> points = findInjectionPointsInInjector(namedNodes);
		points = updateInjectionPointsWithInjectee(points);
		updateProjectNode(points);
	}
	private void updateProjectNode(List<InjectionPoint> points) {
	
		for (InjectionPoint injectionPoint : points) {
			Node caller = Utils.findNodeByPath(projectNode, injectionPoint.getInjector());
			Node callee = Utils.findNodeByPath(projectNode, injectionPoint.getInjectee());
			if(callee !=null && caller !=null){
				caller.getCallees().add(callee);
				callee.getCallers().add(caller);
			}
		}		
	}
	private List<InjectionPoint> updateInjectionPointsWithInjectee(List<InjectionPoint> points) {
		List<InjectionPoint> result = new ArrayList<InjectionPoint>();
		for (int i =  0; i < points.size(); i++) {
			InjectionPoint injectionPoint  = points.get(i);
			String statement = injectionPoint.getStatement().toString();
			int index = statement.indexOf(INJEXTION_ANOTATION);
			if(index!=-1){										//Xac dinh co phai su dung @Injection
				index = index+INJEXTION_ANOTATION.length()+1;
				int index2 = statement.indexOf(" ", index);
				//Lay qualifier
				String qualifier = statement.substring(index, index2);
				List<Node> callees = new ArrayList<Node>();
				if(qualifier.indexOf("@") != -1){				//Neu la qualifier
					//Bao qualifier o class
					callees = Search.searchNode(projectNode,new CDICustomQualifierCondition(qualifier));
					//Khai bao qualifier bang @Produces
					if(callees.size() == 0)
						callees = Search.searchNode(projectNode,new CDIProducesCondition(qualifier));
					//Khai bao qualifier bang @Observes
					if(callees.size() == 0)	
						callees = Search.searchNode(projectNode,new CDIObservesCondition(qualifier));
				}else{
					//Khai bao bang @Default
					if(injectionPoint.getStatement() instanceof FieldDeclaration){
						FieldDeclaration field = (FieldDeclaration) injectionPoint.getStatement();
						String type = field.getType().toString();
						callees = Search.searchNode(projectNode, new CDIDefaultCondition(type));
					}else{
						MethodDeclaration method = (MethodDeclaration) injectionPoint.getStatement();
						List<SingleVariableDeclaration> parameters = method.parameters();
						for (SingleVariableDeclaration p : parameters) {
							if(p.toString().indexOf("@")==-1){
								String type = p.getType().toString();
								callees = Search.searchNode(projectNode, new CDIDefaultCondition(type));
							}else{
								String customQualifier = extractQualifierFromParameter(p.toString());
								callees = Search.searchNode(projectNode,new CDICustomQualifierCondition(customQualifier));
							}
						}
					}
					if(callees.size() == 0){
						String beanAlternative = findDefaultClassInBeansXML();
					}
					//Khai bao bang bean.xml
				}
				//Xac dinh chinh xac (truong hop co nhieu qualifiers giong nhau)
				Node node = getExactNode(callees);
				injectionPoint.setInjectee(node);
				result.add(injectionPoint);
			}
		}
		return result;
	}
	private String extractQualifierFromParameter(String string) {
		String qualifier = "";
		int index1 = string.indexOf("@");
		int index2 = string.indexOf(" ", index1);
		qualifier = string.substring(index1, index2).trim();
		return qualifier;
	}

	/**
	 * Xu ly truong hop tim chinh xac vi tri cua injectee
	 * @param qualifiers
	 * */
	private Node getExactNode(List<Node> qualifiers) {
		if(qualifiers.size() > 0){
			return qualifiers.get(0);
		}
		return new ClassNode();
	}
	/**
	 * Tim cac injection point trong injector
	 * @param namedNodes injector
	 * */
	private List<InjectionPoint> findInjectionPointsInInjector(List<Node> namedNodes) {
		List<InjectionPoint> points = new ArrayList<InjectionPoint>();
		for (Node node : namedNodes) {
			ClassFileParser classFileParser = new ClassFileParser(node.getPath());
			// Kiem tra cac field
			for (FieldDeclaration field : classFileParser.getListFieldDeclaration()) {
//				System.out.println(field.toString());
				if(field.toString().trim().indexOf(INJEXTION_ANOTATION)!=-1){
					points.add(createAInjectionPoint(node, field));
				}
			}
			// Kiem tra cac method
			for (MethodDeclaration method : classFileParser .getListMethodDeclaration()) {
				if(method.toString().trim().indexOf(INJEXTION_ANOTATION)!=-1 ){
					points.add(createAInjectionPoint(node, method));
				}else{
					if(method.toString().trim().indexOf(PRODUCES_ANOTATION)!=-1){
						List<SingleVariableDeclaration> parameters = method .parameters();
						for (SingleVariableDeclaration p : parameters) {
							if (p.toString().indexOf("@")==-1) {
								points.add(createAInjectionPoint(node, method));
								break;
							}
						}
					}
				}
			}
		}
		return points;
	}



	private InjectionPoint createAInjectionPoint(Node node, ASTNode trim) {
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
	
	private String findDefaultClassInBeansXML(){
		String alternative = "alternatives";
		String cls = "";
		List<Node> list = Search.searchNode(projectNode, new CDIBeanCondition());
		if(list.size() > 0){
			String bean = list.get(0).getPath();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(bean);
				
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName(alternative);
				for (int temp = 0; temp < nList.getLength(); temp++) {
					org.w3c.dom.Node nNode = nList.item(temp);
					if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						return eElement.getTextContent();
					}
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
			
		}
		return cls;
	}
}
