package cn.edu.jnu.ie;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import cn.edu.jnu.ie.util.DateFormater;

public class DateFormaterTest {

	@Test
	public void str2Datetest() {
      Long a = DateFormater.timeStr2Date("2015-11-02-22").getTime();
		assertEquals((DateFormater.getFormatTime(a)),"2015-11-02-22");
	}

}
