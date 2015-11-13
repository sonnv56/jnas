package com.fit.process.jsf.connection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fit.ducanh.test.ConfigurationOfAnh;
import com.fit.loader.ProjectLoader;
import com.fit.loader.tree.Search;
import com.fit.object.Node;
import com.fit.object.ProjectNode;
import com.fit.process.jsf.condition.ConfigurationCondition;

/**
 * Phan tich web.xml trong mot project con lay danh sach file cau hinh <br/>
 * Input: Mot project con <br/>
 * Output: Danh sach cac Node tuong ung voi cac file JSF config trong project
 * dau vao
 * 
 * @author DucAnh
 *
 */
public class JSFConfigToWebConfig extends ConnectionGeneration {
	private List<Node> listConfigJSFNode = new ArrayList<>();

	public static void main(String[] args) {
		// Project tree generation
		ProjectNode projectRootNode = ProjectLoader.load(ConfigurationOfAnh.MULTIPLE_CONFIG_JSF1);

		List<Node> listConfigJSFNode = new JSFConfigToWebConfig(projectRootNode).getListConfigJSFNode();
		for (Node n : listConfigJSFNode) {
			System.out.println(n.getPath());
		}
	}

	@Override
	void findDependencies() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param projectNode
	 *            project con
	 */
	public JSFConfigToWebConfig(Node projectNode) {
		// Tim kiem Node web.xml
		List<Node> webConfig = Search.searchNode(projectNode, new ConfigurationCondition(),
				projectNode.getNodeName() + "\\web\\WEB-INF\\web.xml");
		if (webConfig == null || webConfig.size() == 0)
			return;
		Node selectedWebConfig = webConfig.get(0);

		// Lay duong dan tuyet doi cac file config trong web.xml
		String[] configsFile = parseConfigFile(selectedWebConfig);
		if (configsFile != null)
			for (String config : configsFile) {

				// Lay node tuong ung voi duong dan tuong doi file cau hinh
				List<Node> configList = Search.searchNode(projectNode, new ConfigurationCondition(),
						projectNode.getNodeName() + "\\web" + config);

				if (configList == null || configList.size() == 0) {
					// de phong truong hop xay ra
				} else if (configList.size() == 1) {
					// hien nhien dung
					Node configPath = configList.get(0);
					listConfigJSFNode.add(configPath);
				}
			}
	}

	/**
	 * Lay duong dan tuong doi cac file cau hinh JSF
	 * 
	 * @param selectedWebConfig
	 * @return
	 */
	private String[] parseConfigFile(Node selectedWebConfig) {
		try {
			// default code
			File inputFile = new File(selectedWebConfig.getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// our code
			NodeList nList = doc.getElementsByTagName(CONTEXT_TAG);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				org.w3c.dom.Node nNode = nList.item(temp);

				if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String paramName = eElement.getElementsByTagName(PARAM_NAME_TAG).item(0).getTextContent();
					if (paramName.equals(CONFIG_FILES_TAG)) {
						String paramNameList = eElement.getElementsByTagName(PARAM_VALUE_TAG).item(0).getTextContent();

						// Loai bo cac ki tu thua
						paramNameList = paramNameList.replace("\n", "").replace(" ", "").replace("\r", "");
						return paramNameList.split(",");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Node> getListConfigJSFNode() {
		return listConfigJSFNode;
	}

	private static final String CONFIG_FILES_TAG = "javax.faces.CONFIG_FILES";
	private static final String CONTEXT_TAG = "context-param";
	private static final String PARAM_NAME_TAG = "param-name";
	private static final String PARAM_VALUE_TAG = "param-value";
}
