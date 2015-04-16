package cn.edu.jnu.ie.databaseutil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.jnu.ie.util.Constant;

public class ConnectDB {

	public static  Connection con = null; //定义一个MYSQL链接对象
	public static final Logger LOG=LoggerFactory.getLogger(ConnectDB.class);
	public ConnectDB(Connection con){
		this.con=con;
	}
	public static ConnectDB getConnection(){
         try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			LOG.error("Missing jdbc driver");
			e.printStackTrace();
		} //MYSQL驱动
          try {
			con = DriverManager.getConnection(Constant.DATABASE, Constant.USER, Constant.PASSWORD);
		} catch (SQLException e) {
			LOG.error("can not connect to database");
			e.printStackTrace();
		} //链接本地MYSQL	
          return new ConnectDB(con);
	}
	public  void write2FansTable(long follower,long followee){
		  Statement stmt; 
		  try {
			stmt = con.createStatement();
             stmt.executeUpdate("INSERT INTO weibo.fans (followee,follower) VALUES ("+follower+","+followee+")");
             LOG.info("success write to db");
		} catch (SQLException e) {
			LOG.error("SQLERROR:" + e.getStackTrace());
		}
      }
}