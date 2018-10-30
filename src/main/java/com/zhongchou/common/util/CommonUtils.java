package com.zhongchou.common.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Formatter;

import com.yanshang.util.DateUtils;
import com.yanshang.util.MathUtils;
import com.yanshang.util.StringUtils;

public class CommonUtils {
	public static String generateId(Date date) {
		BigDecimal scale = new BigDecimal(0x100000000L);
		BigDecimal bn_time = new BigDecimal(Math.floor((date.getTime()) / 1000));
		BigDecimal incr = new BigDecimal(1);
		BigDecimal id = bn_time.multiply(scale).add(incr);

		return id.toPlainString();
	}
}
