package com.fit.process.jsf.dependenciesgeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fit.object.Node;
import com.fit.process.jsf.JsfUtils;
import com.fit.process.jsf.condition.CssCondition;
import com.fit.process.jsf.object.Dependency;
import com.fit.process.jsf.object.MbNodeContainer;
import com.fit.util.Utils;

/**
 * Phan tich web .xhtml de lay su phu thuoc (.xhtml page, CSS file), (.xhtml
 * page, MB file)
 * 
 * @author DucAnh
 *
 */
public class XhtmlPageParser extends DependenciesGeneration {
	private List<MbNodeContainer> mbNodeContainers_;
	private Node webPageNode_;
	private Node projectNode_;

	public XhtmlPageParser(Node webPage, Node projectNode, List<MbNodeContainer> mbNodeContainers) {
		this.mbNodeContainers_ = mbNodeContainers;
		this.webPageNode_ = webPage;
		this.projectNode_ = projectNode;

		dependencies = findDependencies();
	}

	@Override
	public List<Dependency> findDependencies() {
		List<Dependency> output = new ArrayList<>();
		try {
			final String fileContent = Utils.readFileContent(webPageNode_.getPath());

			/** Tim su phu thuoc giua web page va CSS Node */
			final List<String> relativeCssPaths = findCssDeclaration(fileContent);
			for (String relativeCssPath : relativeCssPaths) {
				final List<Node> cssNodes = JsfUtils.searchNode(projectNode_, new CssCondition(), relativeCssPath);
				for (Node cssNode : cssNodes) {
					final Dependency d = new Dependency();
					d.setBiPhuThuoc(cssNode);
					d.setGayPhuThuoc(webPageNode_);

					output.add(d);
				}
			}
			/** Tim su phu thuoc web page va managed bean file */
			for (MbNodeContainer mbNodeContainer : mbNodeContainers_) {
				if (fileContent.contains("#{" + mbNodeContainer.getName() + ".")) {
					final Dependency d = new Dependency();
					d.setBiPhuThuoc(mbNodeContainer.getClassNode());
					d.setGayPhuThuoc(webPageNode_);

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

	private static final String CSS_REGEX = "href=\"(.*\\.css)\"";
}
