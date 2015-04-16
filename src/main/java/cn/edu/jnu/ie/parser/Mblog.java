package cn.edu.jnu.ie.parser;

import java.util.ArrayList;
import java.util.List;

public class Mblog {
	private String created_timestamp;
	private Long id;
	private Long  mid;
	private String  text;
	private String deleted;
	private List<WeiboPic> pics;
	private int reposts_count;
	private int comments_count;
	private int attitudes_count;
	private String source;
	private List<Url_struct> url_struct = new ArrayList<Url_struct>() ;
	private String bid;
	private User user;
	private Mblog retweeted_status;
	private Topic topic_struct ;
	public Mblog(){
		
	}
	public class WeiboPic{
		private String pid;
		public WeiboPic(){
			
		}
		public String getPid() {
			return pid;
		}
		public void setPid(String pid) {
			this.pid = pid;
		}
	}
	public class Url_struct{
		private String ori_url;
		private String url_title;
		private String url_type;
		public String getOri_url() {
			return ori_url;
		}
		public void setOri_url(String ori_url) {
			this.ori_url = ori_url;
		}
		public String getUrl_title() {
			return url_title;
		}
		public void setUrl_title(String url_title) {
			this.url_title = url_title;
		}
		public String getUrl_type() {
			return url_type;
		}
		public void setUrl_type(String url_type) {
			this.url_type = url_type;
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public int getReposts_count() {
		return reposts_count;
	}
	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}
	public int getComments_count() {
		return comments_count;
	}
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}
	public int getAttitudes_count() {
		return attitudes_count;
	}
	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public List<Url_struct> getUrl_struct() {
		return url_struct;
	}
	public void setUrl_struct(List<Url_struct> url_struct) {
		this.url_struct = url_struct;
	}
	public String getCreated_timestamp() {
		return created_timestamp;
	}
	public void setCreated_timestamp(String created_timestamp) {
		this.created_timestamp = created_timestamp;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<WeiboPic> getPics() {
		return pics;
	}
	public void setPics(List<WeiboPic> pics) {
		this.pics = pics;
	}
	public Mblog getRetweeted_status() {
		return retweeted_status;
	}
	public void setRetweeted_status(Mblog retweet_status) {
		this.retweeted_status = retweet_status;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public Topic getTopic_struct() {
		return topic_struct;
	}
	public void setTopic_struct(Topic topic_struct) {
		this.topic_struct = topic_struct;
	}
	
}
