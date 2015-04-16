package cn.edu.jnu.ie;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import cn.edu.jnu.ie.util.DateFormater;

public class DateFormaterTest {

	public void str2Datetest() {
      Long a = DateFormater.timeStr2Date("2015-11-02-22").getTime();
		assertEquals((DateFormater.getFormatTime(a)),"2015-11-02-22");
	}

	@Test
	public void temp(){
		int a=2;
		adda(a);
		System.out.println(a);
	}
	public void adda(int a){
		a++;
	}

}
