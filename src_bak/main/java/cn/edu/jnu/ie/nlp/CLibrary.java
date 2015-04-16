package cn.edu.jnu.ie.nlp;
import com.sun.jna.Library;
import com.sun.jna.Native;
// 定义接口CLibrary，继承自com.sun.jna.Library
//定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"lib/libNLPIR.so", CLibrary.class);
		
		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);
				
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
		public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
		public String  NLPIR_GetNewWords(String sLine,int nMaxKeyLimit,boolean
				bWeightOut);
		public int NLPIR_ImportUserDict(String sFilename);
		public String NLPIR_GetLastErrorMsg();
		public void NLPIR_Exit();
	}
