package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;

import com.fit.loader.tree.ClassCondition;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.process.jsf.JsfUtils;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanTag;
import com.fit.process.jsf.object.MbNodeContainer;

/**
 * Phan tich mot the managed bean d/n trong <managed-bean> de tao su phu thuoc
 * (JSF Config, Java class). Dong thoi, danh sach class la managed bean duoc
 * trich xuat
 * 
 * @author DucAnh
 *
 */
public class ManagedBeanTagParser extends DependenciesGeneration {
	private List<MbNodeContainer> mbNodeContainers_ = new ArrayList<>();
	private Node projectNode_;
	private ManagedBeanTag mbTag_;

	public ManagedBeanTagParser(ManagedBeanTag mb, Node projectNode) {
		this.projectNode_ = projectNode;
		this.mbTag_ = mb;
		dependencies = findDependencies();
	}

	@Override
	public List<Dependency> findDependencies() {
		List<Dependency> output = new ArrayList<>();
		// default code
		try {
			/** mot navigation co nhieu managed-bean */
			List<Node> dependencyMBClass = new ArrayList<>();

			/** mot managed-bean chi co mot managed-bean-name */
			final org.w3c.dom.Node nNameNode = mbTag_.getContent().getElementsByTagName(MANAGE_BEAN_NAME).item(0);
			final String mbName = nNameNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "");

			/** mot managed-bean chi co mot managed-bean-class */
			final org.w3c.dom.Node nClassNode = mbTag_.getContent().getElementsByTagName(MANAGE_BEAN_CLASS).item(0);
			final String mbClassPath = nClassNode.getTextContent().replace("\n", "").replace(" ", "").replace("\r", "")
					.replace(".", "/");

			/** voi moi managed-bean-class, chi co mot ClassNode thoa man */
					final Node mbClassNode = JsfUtils.searchNode(projectNode_, new ClassCondition(), mbClassPath).get(0);

			// update dependencyMBClass
			dependencyMBClass.add(mbClassNode);

			// update managed bean nodes list for using later (we need
			// collect all managed bean classes for generating connection
			// from web to these classes)
			final MbNodeContainer mbNodeContainer = new MbNodeContainer((ClassNode) mbClassNode);
			mbNodeContainer.setName(mbName);

			mbNodeContainers_.add(mbNodeContainer);

			/** Tao lien ket */
			for (Node n : dependencyMBClass) {
				Dependency d = new Dependency();
				d.setBiPhuThuoc(n);
				d.setGayPhuThuoc(mbTag_.getJSFConfig());
				output.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public List<MbNodeContainer> getManagedBeanContainers() {
		return mbNodeContainers_;
	}

	private static final String MANAGE_BEAN_NAME = "managed-bean-name";
	private static final String MANAGE_BEAN_CLASS = "managed-bean-class";
}
