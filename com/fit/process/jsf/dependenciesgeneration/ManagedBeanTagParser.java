package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;

import com.fit.loader.tree.ClassCondition;
import com.fit.loader.tree.Search;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNodeContainer;
import com.fit.process.jsf.object.ManagedBeanTag;

/**
 * Phan tich mot the managed bean d/n trong <managed-bean> de tao su phu thuoc
 * (JSF Config, Java class). Dong thoi, danh sach class la managed bean duoc
 * trich xuat
 * 
 * @author DucAnh
 *
 */
public class ManagedBeanTagParser extends DependenciesGeneration {
	private List<ManagedBeanNodeContainer> managedBeanNodeContainer = new ArrayList<>();
	private Node projectNode;
	private ManagedBeanTag mbTag;

	public ManagedBeanTagParser(ManagedBeanTag mb, Node projectNode) {
		this.projectNode = projectNode;
		this.mbTag = mb;
		dependenciesList = findDependencies();
	}

	@Override
	public List<Dependency> findDependencies() {
		List<Dependency> output = new ArrayList<>();
		// default code
		try {
			/** mot navigation co nhieu managed-bean */
			List<Node> dependencyMBClass = new ArrayList<>();

			/** mot managed-bean chi co mot managed-bean-name */
			org.w3c.dom.Node nNameNode = mbTag.getContent().getElementsByTagName(MANAGE_BEAN_NAME).item(0);
			String mbName = nNameNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");

			/** mot managed-bean chi co mot managed-bean-class */
			org.w3c.dom.Node nClassNode = mbTag.getContent().getElementsByTagName(MANAGE_BEAN_CLASS).item(0);
			String mbClass = nClassNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");
			mbClass = mbClass.replace(".", "/");

			/** voi moi managed-bean-class, chi co mot ClassNode thoa man */
			Node mbClassNode = Search.searchNode(projectNode, new ClassCondition(), mbClass).get(0);

			// update dependencyMBClass
			dependencyMBClass.add(mbClassNode);

			// update managed bean nodes list for using later (we need
			// collect all managed bean classes for generating connection
			// from web to these classes)
			ManagedBeanNodeContainer mbNodeContainer = new ManagedBeanNodeContainer((ClassNode) mbClassNode);
			mbNodeContainer.setName(mbName);
			managedBeanNodeContainer.add(mbNodeContainer);

			/** Tao lien ket */
			for (Node n : dependencyMBClass) {
				Dependency d = new Dependency();
				d.setBiPhuThuoc(n);
				d.setGayPhuThuoc(mbTag.getJSFConfig());
				output.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public List<ManagedBeanNodeContainer> getManagedBean() {
		return managedBeanNodeContainer;
	}

	private static final String MANAGE_BEAN = "managed-bean";
	private static final String MANAGE_BEAN_NAME = "managed-bean-name";
	private static final String MANAGE_BEAN_CLASS = "managed-bean-class";
}
