package cn.edu.jnu.ie.nlp;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.StandardNaiveBayesClassifier;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.vectorizer.TFIDF;
import org.chasen.crfpp.Tagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.data.CRFFormater;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

public class Classifier {
 //上述几个文件路径
	public static final Logger LOG = LoggerFactory.getLogger(Classifier.class);
        String modelPath = "./source/model";
        String labelIndexPath = "./source/labelindex";
        String dictionaryPath = "./source/dictionary.file-0";
        String documentFrequencyPath = "./source/part-r-00000";
        Configuration configuration;
        NaiveBayesModel model;
        private StandardNaiveBayesClassifier classifier;
        private Map<Integer, String> labels;
        private Map<String, Integer> dictionary;
        private Map<Integer, Long> documentFrequency ;
        //实时分词后是以空格分割的
        //所以此处用 WhitespaceAnalyzer
        //lucene 版本是 4.6.0
      private  Analyzer analyzer;
        //读取训练集包含的文档个数
      static int documentCount;
public Classifier(){
        this.configuration = new Configuration();
        //hdfs 配置
        //configuration.set("fs.default.name", "hdfs://172.21.1.129:9000");
        //configuration.set("mapred.job.tracker", "172.21.1.129:9001");
        //读取模型文件
        try {
			this.model = NaiveBayesModel.materialize(new Path(modelPath), configuration);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //初始化训练器
       this.classifier = new StandardNaiveBayesClassifier(model);
        //读取 labelindex、dictionary、df-count
        this.labels = BayesUtils.readLabelIndex(configuration, new Path(labelIndexPath));
        dictionary = readDictionnary(configuration, new Path(dictionaryPath));
        documentFrequency = readDocumentFrequency(configuration, new Path(documentFrequencyPath));
        //读取训练集包含的文档个数
        documentCount = documentFrequency.get(-1).intValue();
}

    public static Map<String, Integer> readDictionnary(Configuration conf, Path dictionnaryPath) {
        Map<String, Integer> dictionnary = new HashMap<String, Integer>();
        for (Pair<Text, IntWritable> pair : new SequenceFileIterable<Text, IntWritable>(dictionnaryPath, true, conf)) {
            dictionnary.put(pair.getFirst().toString(), pair.getSecond().get());
        }
        return dictionnary;
    }

    public static Map<Integer, Long> readDocumentFrequency(Configuration conf, Path documentFrequencyPath) {
        Map<Integer, Long> documentFrequency = new HashMap<Integer, Long>();
        for (Pair<IntWritable, LongWritable> pair : new SequenceFileIterable<IntWritable, LongWritable>(documentFrequencyPath, true, conf)) {
            documentFrequency.put(pair.getFirst().get(), pair.getSecond().get());
        }
        return documentFrequency;
    }
    public int hasOpinion(String rawString){
    	  Tagger tagger = new Tagger("-m source/crfppModel/model_test -n2");
    	  tagger.clear();
    	  int hasOpinion = 0;
    	  ArrayList<String> words = CRFFormater.format(rawString);
    	  for (String wordInfo :words)
    	  tagger.add(wordInfo);
    	  if (!tagger.parse())
    		    System.out.println("error parsing sentence");;
    	  if (tagger.size()>0){
    		  hasOpinion = tagger.y2(0).equals("noop")?0:1;
    		  if(tagger.prob(hasOpinion)>0.8){
    		  LOG.info(rawString+"\t"+tagger.y2(0)+"\t");
    		  LOG.info("prob = "+String.valueOf(tagger.prob(hasOpinion)));
    		  }
    	  }
    	return hasOpinion;
    	  }

    public int classify(String sourceSentence) throws Exception {
        //待分类文本//
        String content = Fenci.nlpirFenci(sourceSentence,0);	
        Multiset<String> words = ConcurrentHashMultiset.create();
        this.analyzer = new WhitespaceAnalyzer(Version.LUCENE_46);
        TokenStream ts = analyzer.tokenStream("text", new StringReader(content));
        CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        int wordCount = 0;
        //统计在 dictionary 里出现的待分类的句子中的词
        while (ts.incrementToken()) {
            if (termAtt.length() > 0) {
                String word = ts.getAttribute(CharTermAttribute.class).toString();
                Integer wordId = dictionary.get(word);

                if (wordId != null) {
                    words.add(word);
                    wordCount++;
                }
            }
        }
        //计算 TF-IDF，并构造 Vector 
        Vector vector = new RandomAccessSparseVector(10000);
        TFIDF tfidf = new TFIDF();
        for (Multiset.Entry<String> entry : words.entrySet()) {
            String word = entry.getElement();
            int count = entry.getCount();
            Integer wordId = dictionary.get(word);
            Long freq = documentFrequency.get(wordId);
            double tfIdfValue = tfidf.calculate(count, freq.intValue(), wordCount, documentCount);
            vector.setQuick(wordId, tfIdfValue);
        }
        //分类
        Vector resultVector = classifier.classifyFull(vector);
        boolean isObject=true;
        double bestScore = -Double.MAX_VALUE;
        int bestCategoryId = -1;
        for(Element element : resultVector.all()) {
            int categoryId = element.index();
            double score = element.get();
            if(score != 0.0){isObject=false;}
            if (score > bestScore) {
                bestScore = score;
                bestCategoryId = categoryId;
            }
        }
        analyzer.close();
        if(!isObject){
        	if(labels.get(bestCategoryId).equals("negative")){
        		return -1;
        	}
        	else return 1;
        }
        else{

        	return 0;
        	}
    }
    public int getPolar(Collection<String> rawComment){
    	float sum=0;
    	for(String comment : rawComment){
    		try {
    			if(hasOpinion(comment)==1)
   				sum+=classify(comment);
    			else{
    				return 0;
    			}
			} catch (Exception e) {
				System.out.println("can't find model file");
			}
    	}
    	if(sum/rawComment.size()>=0.5){
    		return 1;
    	}
    	else if(sum/rawComment.size()<=-0.5) {
    		return -1;
    	}
    	else
    		return 0;
    }
    static {
    	  try {
    	    System.loadLibrary("CRFPP");
    	  } catch (UnsatisfiedLinkError e) {
    	    System.err.println("Cannot load the example native code.\nMake sure your LD_LIBRARY_PATH contains \'.\'\n" + e);
    	    System.exit(1);
    	  }
    	}
    public static void main(String[] args) throws Exception{
        Classifier cl = new Classifier();
        String str="三星旗舰Galaxy S6屏幕大缺陷：触摸失灵、偏移：当手指的触摸操作靠近屏幕边框时，其屏幕传感器突然出现偶尔失灵的情况，勉强可以识别触摸操作，屏幕边缘则根本无法触到，或者说完全没反应。http://t.cn/Rw3Zjl9";
        java.util.Vector<String> test = new java.util.Vector<String>();
        test.add(str);
        
        String str2="测f试一下,一点都不好";
    	 // int temp = cl.hasOpinion(str);
    	  //System.out.println(cl.getPolar(test));
    	  
    }
}

