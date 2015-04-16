package cn.edu.jnu.ie.parser;

public class Followee {
		private long id;
		private String screen_name;
		private long statuses_count; 
		private String fansNum;
		public Followee(){
		}
		public Followee(long uid) {
			this.id=uid;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getScreen_name() {
			return screen_name;
		}
		public void setScreen_name(String screen_name) {
			this.screen_name = screen_name;
		}
		public long getStatuses_count() {
			return statuses_count;
		}
		public void setStatuses_count(long statuses_count) {
			this.statuses_count = statuses_count;
		}
		public String getFansNum() {
			return fansNum;
		}
		public void setFansNum(String fansNum) {
			this.fansNum = fansNum;
		}
	}