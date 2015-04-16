package cn.edu.jnu.ie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.chasen.crfpp.Tagger;

public class test {

public static void main(String[] argv) throws IOException {
  Tagger tagger = new Tagger("-m /home/hadoop/crf++/weibo_output/model_test -v 1 -n2");
  // clear internal context
  File src= new File("/home/hadoop/crf++/weibo_input/train_set");
  FileReader fr = new FileReader(src);
  BufferedReader reader = new BufferedReader(fr );
   String tempString;
  tagger.clear();
  while((tempString=reader.readLine())!=null){
 // tagger.add(tempString);
	  tagger.add(tempString);
  }
  System.out.println("column size: " + tagger.xsize());
  System.out.println("token size: " + tagger.size());
  System.out.println("tag size: " + tagger.ysize());

  System.out.println("tagset information:");
  for (int i = 0; i < tagger.ysize(); ++i) {
    System.out.println("tag " + i + " " + tagger.yname(i));
  }

  // parse and change internal stated as 'parsed'
  if (!tagger.parse())
    return;

  System.out.println("conditional prob=" + tagger.prob()
                     + " log(Z)=" + tagger.Z());
  for (int i = 0; i < tagger.size(); ++i) {
    for (int j = 0; j < tagger.xsize(); ++j) {
        System.out.print(tagger.x(i, j) + "\t");
    }
    System.out.print(tagger.y2(i) + "\t");
    System.out.print("\n");

    System.out.print("Details");
    for (int j = 0; j < tagger.ysize(); ++j) {
      System.out.print("\t" + tagger.yname(j) + "/prob=" + tagger.prob(i,j)
                       + "/alpha=" + tagger.alpha(i, j)
                       + "/beta=" + tagger.beta(i, j));
    }
    System.out.print("\n");
  }

  // when -n20 is specified, you can access nbest outputs
  System.out.println("nbest outputs:");
  for (int n = 0; n < 10; ++n) {
    if (! tagger.next()) break;
    System.out.println("nbest n=" + n + "\tconditional prob=" + tagger.prob());
    // you can access any information using tagger.y()...
  }
  System.out.println("Done");
}

static {
  try {
    System.loadLibrary("CRFPP");
  } catch (UnsatisfiedLinkError e) {
    System.err.println("Cannot load the example native code.\nMake sure your LD_LIBRARY_PATH contains \'.\'\n" + e);
    System.exit(1);
  }
}

}
