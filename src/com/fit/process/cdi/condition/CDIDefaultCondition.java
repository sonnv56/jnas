package com.fit.process.cdi.condition;

import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.tree.Condition;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.util.Utils;

/**
 * @author son Dieu kien kiem tra CDI anotation Name
 * */
public class CDIDefaultCondition extends Condition {
	/** Anotation Named */
	private static final String DEFAULT_ANOTATION = "@Default";
	private String type;

	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof ClassNode) {
			try {
				// Doc file java
				String fileContent = Utils.readFileContent(n.getPath());
				ClassFileParser classFileParser = new ClassFileParser(
						n.getPath());
				// Lay vi tri cua anotation Named
				List<ASTNode> anotations = classFileParser.getListAnnotation();
				boolean check = false;
				for (ASTNode astNode : anotations) {
					if (astNode.toString().equals(DEFAULT_ANOTATION)) {
						check = true;
						break;
					}
				}

				if (check == false)
					return false;
				Type extendType = classFileParser.getExtendClass();
				if (extendType != null && extendType.toString().equals(type))
					return true;
				List<TypeDeclaration> typeDeclarations = classFileParser.getInterfaces();
				if (typeDeclarations != null && typeDeclarations.size() > 0) {
					return (typeDeclarations.toString().indexOf(type) > 0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public CDIDefaultCondition(String type) {
		this.type = type;
	}
}
