package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.process.jsf.condition.CssCondition;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.ManagedBeanNodeContainer;
import com.fit.util.Utils;

/**
 * Phan tich web .xhtml de lay su phu thuoc (.xhtml page, CSS file), (.xhtml
 * page, MB file)
 * 
 * @author DucAnh
 *
 */
public class XhtmlWebPageParser extends DependenciesGeneration {
	List<ManagedBeanNodeContainer> managedBeanList;
	Node webPage;
	Node projectNode;

	public XhtmlWebPageParser(Node webPage, Node projectNode, List<ManagedBeanNodeContainer> managedBeanList) {
		this.managedBeanList = managedBeanList;
		this.webPage = webPage;
		this.projectNode = projectNode;
		dependenciesList = findDependencies();
	}

	@Override
	public List<Dependency> findDependencies() {
		List<Dependency> output = new ArrayList<>();
		try {
			String fileContent = Utils.readFileContent(webPage.getPath());

			/** Tim su phu thuoc giua web page va CSS Node */
			List<String> relativeCssPaths = findCssDeclaration(fileContent);
			for (String relativeCssPath : relativeCssPaths) {
				List<Node> cssNodeList = Search.searchNode(projectNode, new CssCondition(), relativeCssPath);
				for (Node cssNode : cssNodeList) {
					Dependency d = new Dependency();
					d.setBiPhuThuoc(cssNode);
					d.setGayPhuThuoc(webPage);
					output.add(d);
				}
			}
			/** Tim su phu thuoc web page va managed bean file */
			for (ManagedBeanNodeContainer mbNode : managedBeanList) {
				if (fileContent.contains("#{" + mbNode.getName() + ".")) {
					Dependency d = new Dependency();
					d.setBiPhuThuoc(mbNode.getClassNode());
					d.setGayPhuThuoc(webPage);
					output.add(d);
				}
			}
		} catch (Exception e) {

		}
		return output;
	}

	/**
	 * Tim cac khai bao duong dan CSS file trong trang web
	 * 
	 * @param fileContent
	 * @return cac duong dan tuong doi cua CSS trong trang web
	 */
	private List<String> findCssDeclaration(String fileContent) {
		List<String> relativeCssPaths = new ArrayList<>();

		Pattern p = Pattern.compile(CSS_REGEX);
		Matcher m = p.matcher(fileContent);

		if (m.find()) {
			relativeCssPaths.add(m.group(1));
		}
		return relativeCssPaths;
	}

	private static final String LINK = "link";
	private static final String CSS_REGEX = "href=\"(.*\\.css)\"";
}
