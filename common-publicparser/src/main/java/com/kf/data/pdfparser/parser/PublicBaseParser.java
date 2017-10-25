package com.kf.data.pdfparser.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/***
 * 
 * @Title: PublicBaseParser.java
 * @Package com.kf.data.pdfparser.parser
 * @Description:公转书基本解析类
 * @author liangyt
 * @date 2017年10月23日 上午10:01:07
 * @version V1.0
 */
public class PublicBaseParser {

	static String[] titleTags = new String[] { "一)", "二)", "三)", "四)", "五)", "六)", "七)", "八)", "九)", "一）", "二）", "三）",
			"四）", "五）", "六）", "七）", "八）", "九）", "1)", "2)", "3)", "4)", "5)", "6)", "7)", "8)", "9)", "1）", "2）", "3）",
			"4）", "5）", "6）", "7）", "8）", "9）", "第一节", "第二节", "第三节", "第四节", "第五节", "第六节", "第七节", "第八节", "第九节", "第十节",
			"第十一节", "第十二节", "第十三节", "第十四节", "第十五节", "一、", "二、", "三、", "四、", "五、", "六、", "七、", "八、", "九、", "十、" };

	/****
	 * 将 时间转换为 yyyy-MM-dd HH:mm:ss 格式
	 * 
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/***
	 * 
	 * @param pre
	 * @param end
	 */
	public void sortPreAndEnd(List<String> pre, List<String> end) {
		for (int i = 0; i < pre.size(); i++) {
			for (int j = i + 1; j < pre.size(); j++) {
				if (pre.get(i).length() < pre.get(j).length()) {
					String temp = pre.get(j);
					pre.set(j, pre.get(i));
					pre.set(i, temp);
					String btemp = end.get(j);
					end.set(j, end.get(i));
					end.set(i, btemp);
				} else if (pre.get(i).length() == pre.get(j).length()) {
					if (!pre.get(i).contains("母公司") && end.get(j).contains("母公司")) {
						String temp = pre.get(j);
						pre.set(j, pre.get(i));
						pre.set(i, temp);
						String btemp = end.get(j);
						end.set(j, end.get(i));
						end.set(i, btemp);
					} else {
						if (end.get(i).length() < end.get(j).length()) {
							String temp = pre.get(j);
							pre.set(j, pre.get(i));
							pre.set(i, temp);
							String btemp = end.get(j);
							end.set(j, end.get(i));
							end.set(i, btemp);
						}
					}

				}
			}
		}

	}

}
