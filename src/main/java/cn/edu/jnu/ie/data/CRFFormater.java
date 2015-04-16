package cn.edu.jnu.ie.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.jnu.ie.nlp.Fenci;

public class CRFFormater {
	public CRFFormater(){
		
	}
public static ArrayList<String>  format(String rawString){
	 Pattern p =  Pattern.compile("#[^#,.。]+#");
  	 Matcher m = p.matcher(rawString);
  	 ArrayList<String> sentence = new ArrayList<String>();
  	 if(m.find()){
  	 	rawString=rawString.replace(m.group(), "");
  	   }
	 String[] words = Fenci.nlpirFenci(rawString,1).split(" ");
	 for (String word :words){
		if(word!=""){
      	String wordInfo;
			String[] info = word.split("/");
	  		if(info.length==2){
	  			//过滤名词
	  			if(!info[1].startsWith("rr") && !info[1].startsWith("n")){
	  		    wordInfo = info[0]+"\t"+info[1];
	  		    sentence.add(wordInfo);
	  			}
	  		}
	   	}
      }
	 return sentence;
}
public static void format(File src,String des,String polar) throws IOException{
	 FileReader fr = new FileReader(src);
	 FileWriter fw = new FileWriter(des,true);
	 BufferedReader reader = new BufferedReader(fr );
	 String tempString;
	 while((tempString=reader.readLine())!=null){
	    Pattern p =  Pattern.compile("#[^#,.。]+#");
  	    Matcher m = p.matcher(tempString);
  	    if(m.find()){
  	    	tempString=tempString.replace(m.group(), "");
  	    }
	    String[] words = Fenci.nlpirFenci(tempString,1).split(" ");
	    for (String word :words){
	    StringBuffer sb = new StringBuffer();
	    	if(word!=""){
	    		String[] info = word.split("/");
	    		if(info.length==2){
	    			//过滤名词
	    			if(!info[1].startsWith("rr") && !info[1].startsWith("n")){
	    		    sb.append(info[0]+"\t"+info[1]+"\t"+polar+"\n");
	    		    fw.write(sb.toString());
	    			}
	    		}
	    	}
	    }
	    fw.write("\n");
	  }
	 fw.close();
	 reader.close();
}
public static void main(String[] args) throws IOException{
	CRFFormater cf = new CRFFormater();
	File dir= new File("/home/hadoop/NLPCC-DATA/nlpcc2014_dataset/parse_point/yes_label");
	File[] files = dir.listFiles();
	//Pattern p =  Pattern.compile("pos");
	for (File file:files){
  	  //  Matcher m = p.matcher(file.getName());
  	    System.out.println("parsing "+file.getName());
  	   // if(m.find()){
	        cf.format(file, "/home/hadoop/crf++/weibo_input/temp_noop_4","opin");
  	    //}
  	    //else
	    //    cf.format(file, "./temp_neg","neg");
	}
}
}
