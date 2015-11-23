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
import com.fit.process.Processor;
import com.fit.process.cdi.condition.CDIBeanCondition;
import com.fit.process.cdi.condition.CDICustomQualifierCondition;
import com.fit.process.cdi.condition.CDIDefaultCondition;
import com.fit.process.cdi.condition.CDINamedCondition;
import com.fit.process.cdi.condition.CDIObservesCondition;
import com.fit.process.cdi.condition.CDIProducesCondition;
import com.fit.util.Utils;
/**
 * @author sonnguyen
 * CDI Processor handles Java CDI
 * */
public class CDIProcessor extends Processor{

	public static void main(String[] args) {
//		String projectRootPath = "C://Users//son//Downloads//dukes-forest//dukes-forest";
//		String projectRootPath = "C://Users//Chicky//Documents//NetBeansProjects//CIASample";
		String projectRootPath = "//Users//sonnguyen//Google Drive//Share//CDITest//dukes-forest";
		ProjectNode projectNode = ProjectLoader.load(projectRootPath);
		CDIProcessor processor = new CDIProcessor();
		processor.setProjectNode(projectNode);

		processor.process();
		
	}
	/**
	 * Workflow:
	 * <ol>
	 * <li>Search named node (@Named) (injector)</li>
	 * <li>Find injection point in each named node</li>
	 * <li>Find injectee</li>
	 * <li>Update relationship between nodes</li>
	 * </ol>
	 * */
	@Override
	public void process(){
		List<Node> namedNodes = Search.searchNode(projectNode, new CDINamedCondition());
		List<InjectionPoint> points = findInjectionPointsInInjector(namedNodes);
		System.out.println(points.size());
		points = updateInjectionPointsWithInjectee(points);
		updateProjectNode(points);
	}
	/**
	 * Apply injected relationship
	 * @param points injection points
	 * */
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
	/**
	 * Find injectee
	 * @param points injection points 
	 * */
	private List<InjectionPoint> updateInjectionPointsWithInjectee(List<InjectionPoint> points) {
		List<InjectionPoint> result = new ArrayList<InjectionPoint>();
		for (int i =  0; i < points.size(); i++) {
			InjectionPoint injectionPoint  = points.get(i);
			String statement = injectionPoint.getStatement().toString();
			int index = statement.indexOf(CDIConst.INJECT_ANNOTATION);
			if(index!=-1){										//Xac dinh co phai su dung @Injection
				index = index+CDIConst.INJECT_ANNOTATION.length()+1;
				int index2 = statement.indexOf(" ", index);
				//Lay qualifier
				String qualifier = statement.substring(index, index2);
				List<Node> callees = new ArrayList<Node>();
				if(qualifier.indexOf(CDIConst.ANNOTATION_PREFIX) != -1){				//Neu la qualifier
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
						callees = findCalleesInDefaultMethodCase(injectionPoint);
					}
					//Khai bao bang bean.xml
					if(callees.size() == 0){
						String beanAlternative = findDefaultClassInBeansXML(CDIConst.CDI_ALTERNATIVES_ELEMENT);
						System.out.println(beanAlternative);
					}
					//Khai bao bang bean.xml
					if(callees.size() == 0){
						String beanAlternative = findDefaultClassInBeansXML(CDIConst.CDI_INTERCEPTORS_ELEMENT);
						System.out.println(beanAlternative);
					}
				}
				//Xac dinh chinh xac (truong hop co nhieu qualifiers giong nhau)
				Node node = getExactNode(callees);
				injectionPoint.setInjectee(node);
				result.add(injectionPoint);
			}
		}
		return result;
	}
	private List<Node> findCalleesInDefaultMethodCase(InjectionPoint injectionPoint) {
		List<Node> callees = new ArrayList<Node>();
		MethodDeclaration method = (MethodDeclaration) injectionPoint.getStatement();
		@SuppressWarnings("unchecked")
		List<SingleVariableDeclaration> parameters = method.parameters();
		for (SingleVariableDeclaration p : parameters) {
			if(p.toString().indexOf(CDIConst.ANNOTATION_PREFIX)==-1){
				String type = p.getType().toString();
				callees = Search.searchNode(projectNode, new CDIDefaultCondition(type));
			}else{
				String customQualifier = extractQualifierFromParameter(p.toString());
				callees = Search.searchNode(projectNode,new CDICustomQualifierCondition(customQualifier));
			}
		}
		return callees;
	}
	/**
	 * Extract qualifier from parameter
	 * @param parameter
	 * @return qualifier
	 * */
	private String extractQualifierFromParameter(String string) {
		String qualifier = "";
		int index1 = string.indexOf(CDIConst.ANNOTATION_PREFIX);
		int index2 = string.indexOf(" ", index1);
		qualifier = string.substring(index1, index2).trim();
		return qualifier;
	}

	/**
	 * Xu ly truong hop tim chinh xac vi tri cua injectee
	 * @param qualifiers
	 * @param node
	 * */
	private Node getExactNode(List<Node> qualifiers) {
		if(qualifiers.size() > 0){
			return qualifiers.get(0);
		}
		return new ClassNode();
	}
	/**
	 * Find injection point in named node (injector)
	 * @param namedNodes injector
	 * @return list of injection points
	 * */
	private List<InjectionPoint> findInjectionPointsInInjector(List<Node> namedNodes) {
		List<InjectionPoint> points = new ArrayList<InjectionPoint>();
		for (Node node : namedNodes) {
			ClassFileParser classFileParser = new ClassFileParser(node.getPath());
			// Kiem tra cac field
			List<InjectionPoint> fieldPoints = findInjectionInFelds(node, classFileParser);
			points.addAll(fieldPoints);
			// Kiem tra cac method
			List<InjectionPoint> methodPoints = findInjectionInMethods(node, classFileParser);
			points.addAll(methodPoints);
		}
		System.out.println(points.size());
		return points;
	}
	/**
	 * Find injection point in all methods in a node
	 * @param node
	 * @param classFileParser
	 * @return list injection points
	 * */
	private List<InjectionPoint> findInjectionInMethods(Node node, ClassFileParser classFileParser) {
		List<InjectionPoint> result = new ArrayList<InjectionPoint>();
		for (MethodDeclaration method : classFileParser .getListMethodDeclaration()) {
			String methodStr = method.toString().trim();
			if(methodStr.indexOf(CDIConst.INJECT_ANNOTATION)!=-1 || methodStr.indexOf(CDIConst.RESOURCE_ANOTATION)!=-1){	//Theo cach dung @Injection
				result.add(createAInjectionPoint(node, method));
			}else{
				if(method.toString().trim().indexOf(CDIConst.PRODUCES_ANNOTATION)!=-1){	//Theo cach dung @Produces
					@SuppressWarnings("unchecked")
					List<SingleVariableDeclaration> parameters = method.parameters();
					for (SingleVariableDeclaration p : parameters) {			//Kiem tra tung parameters
						if (p.toString().indexOf(CDIConst.ANNOTATION_PREFIX)==-1) {					//Kiem tra annotation
							result.add(createAInjectionPoint(node, method));
							break;
						}
					}
				}
			}
		}
		return result;
	}
	/**
	 * Find injection point in all fields in a node
	 * @param node
	 * @param classFileParser
	 * @return list injection points
	 * */
	private List<InjectionPoint> findInjectionInFelds(Node node, ClassFileParser classFileParser) {
		List<InjectionPoint> result = new ArrayList<InjectionPoint>();
		for (FieldDeclaration field : classFileParser.getListFieldDeclaration()) {
			String fieldStr = field.toString().trim();
			if(fieldStr.indexOf(CDIConst.INJECT_ANNOTATION)!=-1 || fieldStr.indexOf(CDIConst.RESOURCE_ANOTATION)!=-1){
				result.add(createAInjectionPoint(node, field));
			}
		}
		return result;
	}
	/**
	 * Create a new injection point
	 * @param node
	 * @param trim
	 * @return injection point
	 * */
	private InjectionPoint createAInjectionPoint(Node node, ASTNode trim) {
		InjectionPoint point = new InjectionPoint();
		point.setInjector(node);
		point.setStatement(trim);
		return point;
	}
	/**
	 * Find default class in beans.xml
	 * @return package
	 * */
	private String findDefaultClassInBeansXML(String annotation){
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
				NodeList nList = doc.getElementsByTagName(annotation);
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
