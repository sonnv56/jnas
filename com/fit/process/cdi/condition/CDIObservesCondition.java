package com.fit.process.cdi.condition;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.tree.Condition;
import com.fit.object.ClassNode;
import com.fit.object.Node;
import com.fit.process.cdi.CDIConst;

public class CDIObservesCondition extends Condition {
	/** Anotation Named */
	private String name;

	public CDIObservesCondition(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isStatisfiabe(Node n) {
		if (n instanceof ClassNode) {
			ClassFileParser classFileParser = new ClassFileParser(n.getPath());
			// Kiem tra cac method
			String anotation = name;
			if(anotation.indexOf(CDIConst.ANNOTATION_PREFIX) == -1){
				anotation = CDIConst.ANNOTATION_PREFIX + name;
			}
			for (MethodDeclaration method : classFileParser .getListMethodDeclaration()) {
				@SuppressWarnings("unchecked")
				List<SingleVariableDeclaration> parameters = method.parameters();
				for (SingleVariableDeclaration p : parameters) {
					if(p.toString().trim().indexOf(CDIConst.OBSERVES_ANNOTATION + " " + anotation)!=-1){
						return true;
					}
				}
			}
		}
		return false;
	}
}
