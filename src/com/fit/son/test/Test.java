package com.fit.son.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		String parrtern = "(@)(Produces)( ).*?(\\().*?(\\)).*";
		Pattern pa = Pattern.compile(parrtern);
		String text = "@Inject ATMTransport createTransport(@Soap ATMTransport soapTransport, @Json ATMTransport jsonTransport)";
		Matcher m = pa.matcher(text);
		System.out.println(m.matches());
	}
}
