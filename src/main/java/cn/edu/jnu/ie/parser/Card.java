package cn.edu.jnu.ie.parser;



public  class Card {
	private int card_type;
	private Mblog mblog;
private Followee user;
public Card(){
	
}
	public int getCard_type() {
		return card_type;
	}
	public void setCard_type(int card_type) {
		this.card_type = card_type;
	}
	public Mblog getMblog() {
		return mblog;
	}
	public void setMblog(Mblog mblog) {
		this.mblog = mblog;
	}
	
	public Followee getUser() {
		return user;
	}
	public void setUser(Followee user) {
		this.user = user;
	}
	

}
