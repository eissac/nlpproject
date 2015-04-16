package cn.edu.jnu.ie.parser;


public interface Parser {
	ParseResult parse();
//	User getUser();
//	Vector<User> getUsers();
//	Weibo getWeibo();
//	Vector<Weibo>  getWeibos();
	
	void setContent(String content);
}
