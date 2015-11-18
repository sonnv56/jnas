package com.fit.process.cdi.condition;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.fit.jdtParser.ClassFileParser;
import com.fit.loader.tree.Condition;
import com.fit.object.ClassNode;
import com.fit.object.Node;

public class CDIProducesCondition extends Condition {
	private static String ANOTATION_PREFIX = "@";
	private static final String PRODUCES_ANOTATION = "@Produces";
	/** Anotation Named */
	private String name;

	public CDIProducesCondition(String name) {
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
			if(anotation.indexOf(ANOTATION_PREFIX) == -1){
				anotation = ANOTATION_PREFIX + name;
			}
			for (MethodDeclaration method : classFileParser .getListMethodDeclaration()) {
				if(method.toString().trim().indexOf(PRODUCES_ANOTATION + " " + anotation)!=-1){
					return true;
				}
			}
		}
		return false;
	}
}
