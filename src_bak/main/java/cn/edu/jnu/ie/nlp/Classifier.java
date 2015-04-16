package cn.edu.jnu.ie.nlp;
import java.io.IOException;
import java.io.StringReader;
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

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

public class Classifier {
 //上述几个文件路径
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
//            System.out.print("  " + labels.get(categoryId) + ": " + score);
        }
        analyzer.close();
        if(!isObject){
        	if(labels.get(bestCategoryId).equals("negative")){
        		return -1;
        	}
        	else return 1;
  //      System.out.println(" => " + labels.get(bestCategoryId));
        }
        else{

        	return 0;
        	//System.out.println(" =>" + "object");
        	}
    }
    public static void main(String[] args) throws Exception{
        Classifier cl = new Classifier();
        String str="测试一下,一点都不好";
        String str2="测f试一下,一点都不好";
    	  cl.classify(str);
    	  cl.classify(str2);
    }
}

