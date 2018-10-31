package com.hnjk.edu.roll.controller;

public class Replace {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "aaaadsf(我要的字符串）kasdjf";
		String r = str.replaceAll("\\(.*\\）", "");
		System.out.println(r);
	}
}
