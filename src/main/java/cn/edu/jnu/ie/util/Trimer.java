package cn.edu.jnu.ie.util;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trimer {
	public static String trimPunc(String str){
		Pattern p = Pattern.compile("([。！!.])\\1+");
		Matcher m = p.matcher(str);
		while(m.find()){
			System.out.println("find :"+m.group());
			str=str.replace(m.group(), "");
		}
		return str;
	}
	public static String[] splitString2sentence(String tempString) {
		String regex="[;；。？！?!]";
		   Pattern p =Pattern.compile(regex);
		   String[] substrs = p.split(tempString);
		   return substrs;
	}
	public static int parseNumber(String str) {
		String regex="[0-9]+";
		Pattern p =Pattern.compile(regex);
		Matcher m = p.matcher(str);
		String newStr="";
		while(m.find()){
			newStr+=m.group();
		}
		return Integer.parseInt(newStr);
		}
	public static String parseChinese(String str){
		//注意：大写字母可以通过
		String zh="";
		Pattern p = Pattern.compile("[^0-9a-zA-PR-Z ：，=]+");
		Matcher m = p.matcher(str);
		if(m.find()){
			zh=m.group(0);
		}
		return zh;
	}
}
