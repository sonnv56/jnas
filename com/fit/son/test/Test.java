package com.fit.son.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;

import com.fit.jdtParser.ClassFileParser;

public class Test {
	public static void main(String[] args) {
		String parrten = "(@(Produces) ).*?(\\(.*)(?!@).*?(\\)).*";
	}
}
