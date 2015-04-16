package cn.edu.jnu.ie.nlp;

import java.util.List;
import com.sun.jna.Native;
public class Fenci {
  private static CLibrary cLibrary = (CLibrary) Native.loadLibrary("lib/libNLPIR.so", CLibrary.class);
   /*
   * nlpir分词工具
   */
  public static String nlpirFenci (String contents){
	  return nlpirFenci(contents,0);
  }
  public static String nlpirFenci (String contents ,int needPos) {
		String argu = "/home/hadoop/NLPproject/nlpproject";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		
		String nativeBytes = null;
	/*	int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
	

		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			return "error";
		}
		*/
     cLibrary.NLPIR_Init("", 1, "0");
    try {
/*
 * 添加自定义词典，注意，只有添加API，没有删除的API，如果只是测试，请先用添加词测试效果，确认后，再放进词典。
 */
  //int import_userdic = cLibrary.NLPIR_ImportUserDict("Data/new");
	//System.out.println(import_userdic);
  /*
	 * 添加自定义词
	 */
    	cLibrary.NLPIR_AddUserWord("美图 n");
    	cLibrary.NLPIR_AddUserWord("魅族 n");
    	cLibrary.NLPIR_AddUserWord("满意 a");
    	cLibrary.NLPIR_AddUserWord("不 deni");
    	cLibrary.NLPIR_AddUserWord("不是 deni");
		cLibrary.NLPIR_AddUserWord("没 deni");
		cLibrary.NLPIR_AddUserWord("没有 deni");
		cLibrary.NLPIR_AddUserWord("非 deni");
        nativeBytes = cLibrary.NLPIR_ParagraphProcess(contents, needPos);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    cLibrary.NLPIR_Exit();
    return nativeBytes;
}
  public static String checkADJ (String word) {
	  String[] a = nlpirFenci(word).split("/");
	  return ((a.length>=2)&&(a[1].trim().equals("a"))) ?a[0]:null;
  }
  public static String getWordName (String word) {
	  String[] a = nlpirFenci(word).split("/");
	  return (a.length>=2) ?a[0]:"error";
  }
}

