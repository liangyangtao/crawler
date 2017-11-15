package com.kf.data.fetcher.tools;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/***
 * 
 * @Title: TableSpliter.java
 * @Package com.kf.data.crawler.tools
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年7月3日 下午3:17:34
 * @version V1.0
 */
public class TableSpliter {

	Levenshtein levenshtein = new Levenshtein();

	public static String[] noNeedTitles = new String[] { "项目", "资产", "类别/项目", "负债和所有者权益", "负债和所有者权益(或股东权益)",
			"负债和所有者权益（或股东权益）" };

	public Element splitTable(Element tableElement, boolean isMergeCol, List<String> rules) {
		Elements trElements = tableElement.select("tr");
		int row = trElements.size();
		if (trElements.size() == 0) {
			return tableElement;
		}
		int col = 0;
		for (int i = 0; i < trElements.size(); i++) {
			Element trElement = trElements.get(i);
			Elements tdElements = trElement.select("td");
			if (col < tdElements.size()) {
				col = tdElements.size();
			}
		}
		// 获取表头的长度
		Element firstElement = trElements.get(0);
		Elements firtsTdElements = firstElement.select("td");
		int titleNum = 1;
		for (Element tdElement : firtsTdElements) {
			String rowsStr = tdElement.attr("rowspan");
			if (!rowsStr.isEmpty()) {
				int rowInt = Integer.parseInt(rowsStr);
				if (titleNum < rowInt) {
					titleNum = rowInt;
				}
			}
		}
		if (titleNum == 1) {
			for (Element tdElement : firtsTdElements) {
				String colStr = tdElement.attr("colspan");
				if (!colStr.isEmpty()) {
					titleNum++;
					break;
				}
			}
		}

		String[][] aa = table2array(trElements, row, col, titleNum);
		mergerTitle(titleNum, aa);
		// 合并同一个字段
		if (titleNum - 1 < aa.length) {
			for (int i = 0; i < aa[titleNum - 1].length; i++) {
				if (i + 1 < aa[titleNum - 1].length) {
					// 如果他们两个值相同
					if (aa[titleNum - 1][i] != null && !aa[titleNum - 1][i].isEmpty()
							&& !aa[titleNum - 1][i].contains("�") && !aa[titleNum - 1][i].contains("-")
							&& !aa[titleNum - 1][i].contains("_") && !aa[titleNum - 1][i].contains(",")
							&& !aa[titleNum - 1][i].matches("\\d{1,100}")
							&& aa[titleNum - 1][i].equals(aa[titleNum - 1][i + 1])) {
						for (int j = titleNum; j < aa.length; j++) {
							aa[j][i] = aa[j][i] + "@@" + aa[j][i + 1];
						}
						for (int j = 0; j < aa.length; j++) {
							aa[j][i + 1] = null;
						}
					}

				}

			}
		}
		// 区分相同的两个字段
		if (titleNum - 1 < aa.length) {
			for (int i = 0; i < aa[titleNum - 1].length; i++) {
				if (i + 2 < aa[titleNum - 1].length) {
					if (aa[titleNum - 1][i] != null && !aa[titleNum - 1][i].isEmpty()
							&& !aa[titleNum - 1][i].contains("�") && !aa[titleNum - 1][i].contains("-")
							&& !aa[titleNum - 1][i].contains("_") && !aa[titleNum - 1][i].contains(",")
							&& !aa[titleNum - 1][i].matches("\\d{1,100}")) {
						for (int j = i + 2; j < aa[titleNum - 1].length; j++) {
							if (aa[titleNum - 1][i].equals(aa[titleNum - 1][j])) {
								if (i - 1 > 0) {
									if (aa[titleNum - 1][i - 1] != null) {
										if (aa[titleNum - 1][i - 1].contains("本期")) {
											aa[titleNum - 1][i] = "本期" + aa[titleNum - 1][i];
										} else {
											aa[titleNum - 1][i] = aa[titleNum - 1][i - 1] + aa[titleNum - 1][i];
										}
										if (aa[titleNum - 1][j - 1] != null) {
											if (aa[titleNum - 1][j - 1].contains("上期")) {
												aa[titleNum - 1][j] = "上期" + aa[titleNum - 1][j];
											} else {
												aa[titleNum - 1][j] = aa[titleNum - 1][j - 1] + aa[titleNum - 1][j];
											}
										}
									}

								}
							}
						}
					}

				}

			}
		}
		// 去除多余表头
		for (int i = titleNum; i < aa.length; i++) {
			if (aa[i][0] == null || aa[i][0].isEmpty()) {
				continue;
			}
			if (Arrays.asList(noNeedTitles).contains(aa[i][0])) {
				for (int j = 0; j < aa[i].length; j++) {
					aa[i][j] = null;
				}
			}
		}

		if (isMergeCol) {
			// 去除表头

			for (int i = 1; i < aa.length; i++) {
				if (aa[i][0] == null || aa[i][0].isEmpty()) {
					continue;
				}
				// 如果包含
				if (rules != null && rules.contains(aa[i][0])) {
					// do nothing
				} else {

					if (rules != null) {
						boolean match = false;
						for (String rule : rules) {
							if (levenshtein.getSimilarityRatio(rule, aa[i][0]) > 0.7) {
								match = true;
								break;
							}
							if (!match) {
								// 如果包含第一行
								if (rule.contains(aa[i][0])) {
									if (i + 1 < aa.length) {
										// 也包含他的下一行
										if (rule.contains(aa[i + 1][0])) {
											match = true;
											// 就合并这两行,将第二行清空
											aa[i][0] = rule;
											aa[i + 1][0] = null;
											for (int j = 1; j < aa.length; j++) {
												if (j < aa[i + 1].length) {
													aa[i][j] = aa[i][j] + aa[i + 1][j];
													aa[i + 1][j] = null;
												}
											}
											break;
										}
									}
								}
							}
						}

					}
					//

				}
			}

		} else {
			// for (int i = 0; i < aa.length; i++) {
			// int spaceNum = 0;
			// int colNum = 0;
			// for (int j = 0; j < aa[i].length; j++) {
			// if (aa[i][j] == null || aa[i][j].isEmpty()) {
			// spaceNum++;
			// } else {
			// colNum = j;
			// }
			// }
			// if (i - 1 > 0 && spaceNum == aa[i].length - 1) {
			// aa[i - 1][colNum] = aa[i - 1][colNum] + aa[i][colNum];
			// aa[i][colNum] = "";
			// }
			// }
		}
		Element result = makeTable(aa, titleNum);
		return result;
	}

	/***
	 * 数组转换为table
	 * 
	 * @param bb
	 * @return
	 */
	public Element makeTable(String[][] bb, int titleNum) {
		Element result = new Element(Tag.valueOf("table"), "");
		result.attr("border", "1");
		StringBuffer sb = new StringBuffer();
		for (int i = titleNum - 1; i < bb.length; i++) {
			sb.append("<tr>");
			for (int j = 0; j < bb[i].length; j++) {
				sb.append("<td>");
				sb.append(bb[i][j] == null ? "" : bb[i][j]);
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		result.append(sb.toString());
		return result;
	}

	public String[][] table2array(Elements trElements, int row, int col, int titleNum) {
		String[][] aa = new String[row][col];
		for (int i = 0; i < trElements.size(); i++) {
			int n = 0;
			Element trElement = trElements.get(i);
			Elements tdElements = trElement.select("td,th");
			for (int j = 0; j < tdElements.size(); j++) {
				Element tdElement = tdElements.get(j);
				String tdText = tdElement.text().trim();
				// tdText = replacekong(tdText);
				if (n > col - 1) {
					break;
				}
				if (aa[i][n] == null || aa[i][n].endsWith("@@")) {
					String rowsStr = tdElement.attr("rowspan");
					String colStr = tdElement.attr("colspan");
					// 如果两个都有值
					if (!rowsStr.isEmpty() && !colStr.isEmpty()) {
						int rowInt = Integer.parseInt(rowsStr);
						int colInt = Integer.parseInt(colStr);
						for (int m = 0; m < rowInt; m++) {
							if (i + m > row - 1) {
								break;
							}
							for (int k = 0; k < colInt; k++) {
								if (n + k > col - 1) {
									break;
								}
								aa[i + m][n + k] = tdText;
							}
						}
						n = n + colInt - 1;
					} else if (!rowsStr.isEmpty() && colStr.isEmpty()) {
						int rowInt = Integer.parseInt(rowsStr);
						String coltext = null;
						if (i < titleNum) {
							if (aa[i][n] != null && aa[i][n].endsWith("@@")) {
								coltext = aa[i][n] + tdText;
							} else {
								coltext = tdText;
							}
						} else {
							coltext = tdText;
						}
						for (int k = 0; k < rowInt; k++) {
							if (i + k < row) {
								aa[i + k][n] = coltext;
							}

						}

					} else if (!colStr.isEmpty() && rowsStr.isEmpty()) {
						int colInt = Integer.parseInt(colStr);
						for (int k = 0; k < colInt; k++) {
							if (n > aa[i].length - 1) {
								break;
							}
							if (i < titleNum - 1) {
								if (aa[i][n] != null && aa[i][n].endsWith("@@")) {
									aa[i][n] = aa[i][n] + tdText;
								} else {
									aa[i][n] = tdText;
								}
								if (i + 1 < aa.length - 1) {
									aa[i + 1][n] = aa[i][n] + "@@";
								}
							} else {
								aa[i][n] = tdText;
							}
							n++;
						}
						n--;
					} else if (colStr.isEmpty() && rowsStr.isEmpty()) {
						if (i < titleNum) {
							if (aa[i][n] != null && aa[i][n].endsWith("@@")) {
								aa[i][n] = aa[i][n] + tdText;
							} else {
								aa[i][n] = tdText;
							}
						} else {
							aa[i][n] = tdText;
						}
					}
				} else {
					j--;
				}
				n++;
			}
		}
		return aa;
	}

	/***
	 * 
	 * @param titleNum
	 * @param cc
	 */
	public void mergerTitle(int titleNum, String[][] cc) {
		for (int i = titleNum - 1; i > 0; i--) {
			if (i > cc.length - 1) {
				break;
			}
			for (int j = 0; j < cc[i].length;) {
				if (cc[i][j] == null || cc[i][j].isEmpty()) {
					cc[i][j] = cc[i - 1][j];
				}
				// 如果他的上級是空的,说明需要合并
				if (cc[i - 1][j] == null || cc[i - 1][j].isEmpty()) {
					if (j - 1 >= 0) {
						// 如果左侧和他的上一行是一样的，说明得和右侧的合并
						if (cc[i][j - 1] != null && !cc[i][j - 1].isEmpty() && cc[i][j - 1].equals(cc[i - 1][j - 1])) {
							int colindex = j;
							boolean isWrite = false;
							while (true) {
								if (j + 1 < cc[i - 1].length && j + 1 < cc[i].length) {

									if (cc[i - 1][j + 1] != null && !cc[i - 1][j + 1].isEmpty()) {
										if (cc[i - 1][j + 1].equals("@@") || cc[i - 1][j + 1].equals("@@@@")) {
											cc[i - 1][j + 1] = "";
										} else {
											// 他的右侧和他的右上相同的？
											if (cc[i - 1][j + 1] != null && !cc[i - 1][j + 1].isEmpty()
													&& cc[i][j + 1] != null && !cc[i][j + 1].isEmpty()
													&& cc[i][j + 1].contains(cc[i - 1][j + 1])) {
												isWrite = true;
											}
											break;
										}
									}
								}
								j++;
								if (j >= cc[i].length) {
									break;
								}

							}
							if (isWrite) {

								// 行的 开始 ，，j 是结束
								for (int k = colindex; k <= j; k++) {
									cc[i - 1][k] = cc[i - 1][j + 1];
								}
								for (int k2 = i; k2 < titleNum; k2++) {
									for (int k = colindex; k <= j; k++) {
										// 列的开始 ... 到结束
										// 如果上一行是空的
										if (cc[k2 - 1][k] == null || cc[k2 - 1][k].isEmpty()) {
											if (cc[k2][k] != null && !cc[k2][k].isEmpty()) {
												cc[k2][k] = cc[k2][k].replace("@@@@", "");
											}
										} else {
											if (cc[k2][k] != null && !cc[k2][k].isEmpty()) {
												cc[k2][k] = cc[k2][k].replace("@@@@", "");
												cc[k2][k] = mergeString(cc[k2 - 1][k] + "@@", cc[k2][k]);
											}
										}

									}
								}

							} else {
								// 他的右侧和他的右上不同
								if (j + 1 < cc[i - 1].length) {
									cc[i - 1][j] = cc[i - 1][j + 1];

									for (int j2 = i; j2 < titleNum; j2++) {
										if (cc[j2 - 1][j] == null || cc[j2 - 1][j].isEmpty()) {
											if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
												cc[j2][j] = cc[j2][j].replace("@@@@", "");
											}
										} else {
											if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
												cc[j2][j] = cc[j2][j].replace("@@@@", "");
												cc[j2][j] = mergeString(cc[j2 - 1][j] + "@@", cc[j2][j]);
											}

										}
									}

									for (int j2 = i; j2 < titleNum; j2++) {
										if (j + 1 < cc[j2 - 1].length && j + 1 < cc[j2].length) {
											if (cc[j2 - 1][j + 1] == null || cc[j2 - 1][j + 1].isEmpty()) {
												if (cc[j2][j + 1] != null && !cc[j2][j + 1].isEmpty()) {
													cc[j2][j + 1] = cc[j2][j + 1].replace("@@@@", "");
												}
											} else {
												if (cc[j2][j + 1] != null && !cc[j2][j + 1].isEmpty()) {
													cc[j2][j + 1] = cc[j2][j + 1].replace("@@@@", "");
													cc[j2][j + 1] = mergeString(cc[j2 - 1][j + 1] + "@@",
															cc[j2][j + 1]);
												}
											}
										}
									}

								}
							}
						} else if (cc[i][j - 1] != null && !cc[i][j - 1].isEmpty() && cc[i - 1][j - 1] != null
								&& !cc[i - 1][j - 1].isEmpty() && cc[i][j - 1].contains(cc[i - 1][j - 1])) {

							if (j + 1 < cc[i - 1].length && cc[i - 1][j + 1] != null && !cc[i - 1][j + 1].isEmpty()
									&& cc[i][j + 1] != null && cc[i][j + 1].equals(cc[i - 1][j + 1])) {
								// 如果右侧相同 左侧也是包含关系，选左侧
								cc[i - 1][j] = cc[i - 1][j - 1];
								for (int j2 = i; j2 < titleNum; j2++) {
									if (cc[j2 - 1][j] == null || cc[j2 - 1][j].isEmpty()) {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
										}
									} else {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
											cc[j2][j] = mergeString(cc[j2 - 1][j] + "@@", cc[j2][j]);
										}
									}

								}

							} else if (j + 1 < cc[i - 1].length && cc[i - 1][j + 1] != null && cc[i][j + 1] != null
									&& !cc[i - 1][j + 1].isEmpty() && cc[i][j + 1].contains(cc[i - 1][j + 1])) {
								// 如果右侧包含 左侧也是包含关系， 两个选择一个
								String temp = cc[i][j];
								boolean isExit = false;
								for (int k = 0; k < j; k++) {
									if (cc[i][k].contains(temp)) {
										isExit = true;
										break;
									}
								}
								if (isExit) {
									cc[i - 1][j] = cc[i - 1][j + 1];
									for (int j2 = i; j2 < titleNum; j2++) {
										if (cc[j2 - 1][j] == null || cc[j2 - 1][j].isEmpty()) {
											if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
												cc[j2][j] = cc[j2][j].replace("@@@@", "");
											}
										} else {
											if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
												cc[j2][j] = cc[j2][j].replace("@@@@", "");
												cc[j2][j] = mergeString(cc[j2 - 1][j] + "@@", cc[j2][j]);
											}
										}
									}
								} else {
									cc[i - 1][j] = cc[i - 1][j - 1];
									for (int j2 = i; j2 < titleNum; j2++) {
										if (cc[j2 - 1][j] == null || cc[j2 - 1][j].isEmpty()) {

											if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
												cc[j2][j] = cc[j2][j].replace("@@@@", "");
											}
										} else {
											if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
												cc[j2][j] = cc[j2][j].replace("@@@@", "");
												cc[j2][j] = mergeString(cc[j2 - 1][j] + "@@", cc[j2][j]);
											}
										}

									}

								}

							} else {
								// 如果右侧不是包含关系 选左侧，
								///////////////// 如果左侧包含他的左上角的的
								cc[i - 1][j] = cc[i - 1][j - 1];
								for (int j2 = i; j2 < titleNum; j2++) {
									if (cc[j2 - 1][j] == null || cc[j2 - 1][j].isEmpty()) {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
										}
									} else {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
											cc[j2][j] = mergeString(cc[j2 - 1][j] + "@@", cc[j2][j]);
										}
									}

								}
							}

						} else if (cc[i][j - 1] != null && !cc[i][j - 1].isEmpty()) {

							///////////////// 如果左侧 不等也不包含，但是右侧是一样的
							if (j + 1 < cc[i - 1].length && cc[i - 1][j + 1] != null && !cc[i - 1][j + 1].isEmpty()
									&& cc[i][j + 1] != null && cc[i][j + 1].equals(cc[i - 1][j + 1])) {
								cc[i - 1][j] = cc[i - 1][j - 1];

								for (int j2 = i; j2 < titleNum; j2++) {
									if (cc[j2 - 1][j] == null || cc[j2 - 1][j].isEmpty()) {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
										}
									} else {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
											cc[j2][j] = mergeString(cc[j2 - 1][j] + "@@", cc[j2][j]);
										}
									}
								}

								for (int j2 = i; j2 < titleNum; j2++) {
									if (cc[j2 - 1][j - 1] == null || cc[j2 - 1][j - 1].isEmpty()) {
										if (cc[j2][j - 1] != null && !cc[j2][j - 1].isEmpty()) {
											cc[j2][j - 1] = cc[j2][j - 1].replace("@@@@", "");
										}
									} else {
										if (cc[j2][j - 1] != null && !cc[j2][j - 1].isEmpty()) {
											cc[j2][j - 1] = cc[j2][j - 1].replace("@@@@", "");
											cc[j2][j - 1] = mergeString(cc[j2 - 1][j - 1] + "@@", cc[j2][j - 1]);
										}
									}
								}

							} else if (j + 1 == cc[i - 1].length) {
								// 说明是最后一行是空的

								cc[i - 1][j] = cc[i - 1][j - 1];

								for (int j2 = i; j2 < titleNum; j2++) {
									if (cc[j2 - 1][j] == null || cc[j2 - 1][j].isEmpty()) {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
										}
									} else {
										if (cc[j2][j] != null && !cc[j2][j].isEmpty()) {
											cc[j2][j] = cc[j2][j].replace("@@@@", "");
											cc[j2][j] = mergeString(cc[j2 - 1][j] + "@@", cc[j2][j]);
										}
									}
								}

								for (int j2 = i; j2 < titleNum; j2++) {
									if (cc[j2 - 1][j - 1] == null || cc[j2 - 1][j - 1].isEmpty()) {
										if (cc[j2][j - 1] != null && !cc[j2][j - 1].isEmpty()) {
											cc[j2][j - 1] = cc[j2][j - 1].replace("@@@@", "");
										}
									} else {
										if (cc[j2][j - 1] != null && !cc[j2][j - 1].isEmpty()) {
											cc[j2][j - 1] = cc[j2][j - 1].replace("@@@@", "");
											cc[j2][j - 1] = mergeString(cc[j2 - 1][j - 1] + "@@", cc[j2][j - 1]);
										}
									}
								}

							} else {

							}

						} else {

						}
					}

				} else {

				}
				// // 如果是空的就把他的上面赋值给自己

				j++;

			}

		}
		// 如果处理完第一列还有空
	}

	/***
	 * 
	 * @param preEnd
	 * @return
	 */
	// public String replacekong(String text) {
	// text = text.replace(" ", "");
	// text = text.replace(" ", "");
	// text = text.replace(" ", "");
	// text = text.replace(" ", "");
	// text = text.replace(" ", "");
	// text = text.replace(" ", "");
	// text = text.replace(" ", "");
	// text = text.replace(" ", "");
	// return text;
	// }

	/****
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public String mergeString(String a, String b) {
		try {
			StringBuffer sb = new StringBuffer();
			char aa[] = a.toCharArray();
			char bb[] = b.toCharArray();
			int j = 0;
			for (int i = 0; i < aa.length; i++) {
				if (j < bb.length) {
					if (aa[i] == bb[j]) {
						sb.append(aa[i]);
						j++;
					} else {
						if (j == 0) {
						} else {
							break;
						}
					}
				}
			}
			String c = b.replace(sb.toString(), "");
			return a + c;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(a);
			System.out.println(b);
		}
		return b;
	}
}
