package cn.edu.jnu.ie.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import cn.edu.jnu.ie.nlp.Classifier;
import cn.edu.jnu.ie.nlp.Fenci;
import cn.edu.jnu.ie.util.Trimer;

public class NBFormater {
public static Vector<Integer> format(String f,String des,String polar) throws Exception{
	FileReader fr= new FileReader(f);
	Vector<Integer> result = new Vector<Integer>();
	BufferedReader reader = new BufferedReader(fr);
	String tempString;
	Classifier cl = new Classifier();
	while(!(tempString=reader.readLine()).equals(null)) {
		tempString = Trimer.trimPunc(tempString);
	   String[] substrs = Trimer.splitString2sentence(tempString);
	   String temp;
	   for(String s : substrs) {
	  	  temp=s.trim();
     	  if(temp.length()>4){
     		 String fenciString = Fenci.nlpirFenci(temp);
     		 int r= cl.classify(fenciString);
     		 if(r!=1)
     		 System.out.println(r+"\t"+fenciString);
     		 result.add(r);
			}
	  	 }
	}
	reader.close();
	return result;
}
public static void main(String[] args) throws Exception{
	/*Vector<Integer> result = NBFormater.format("data/ZOL/parseHTML/positive_final", "null", "null");
	int calP=0;
	int calN=0;
	int calO=0;
    for (int r : result){
    	if (r==1){
    		calP++;
    	}
    	if(r==-1){
    		calN++;
    	}
    	else
    		calO++;
    }
    System.out.println("size = "+result.size());
    System.out.println("calP="+calP+"\t"+"calN="+calN+"\t"+"calO="+calO);
	*/
	String f="data/ZOL/parseHTML/negative_final";
	String all = "/home/hadoop/NLPTestSet/raw/unfilted/neg/ZOLneg001_all";
	String on = "/home/hadoop/NLPTestSet/raw/unfilted/neg/ZOLpos001_op";
	FileReader fr= new FileReader(f);
	BufferedReader reader = new BufferedReader(fr);
	FileWriter fw = new FileWriter(all);
	BufferedWriter writer = new BufferedWriter(fw);
   FileWriter fwon = new FileWriter(on);
	BufferedWriter writeron = new BufferedWriter(fwon);
	Classifier cl = new Classifier();
	String tempString;
	while((tempString = reader.readLine())!= null) {
		tempString = Trimer.trimPunc(tempString);
		String[] substr = Trimer.splitString2sentence(tempString);
		int result;
		for (String asentence : substr) {
			writer.write(asentence+"\n");
			result = cl.classify(Fenci.nlpirFenci(asentence));
			if(result != -1){
			    writeron.write(asentence+"\n");	
			}
		}
	}
	reader.close();
	writer.close();
	writeron.close();
}
}
