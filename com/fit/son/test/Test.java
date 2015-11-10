package com.fit.son.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		String txt = "@Inject private ATMTransport transport;";


//		String pattern = "(@)(Inject)( )(?!@).*?(\\().*?(\\))";
		String pattern = "(@Inject )(?!@).*?(;)";
		System.out.println(pattern);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(txt);
		System.out.println(m.matches());
	}
}
