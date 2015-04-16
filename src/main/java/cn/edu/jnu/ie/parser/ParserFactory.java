package cn.edu.jnu.ie.parser;

import cn.edu.jnu.ie.pageUtil.Page;

public class ParserFactory {
	public ParserFactory(){
		
	}
	public Parser getParser(Page page) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String type = page.getType();
		return (Parser) Class.forName("cn.edu.jnu.ie.parser."+type+"PageParser").newInstance();
	}
}
