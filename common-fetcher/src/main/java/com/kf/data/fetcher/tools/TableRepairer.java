package com.kf.data.fetcher.tools;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/***
 * 
 * @Title: TableRepairer.java
 * @Package com.kf.data.crawler.tools
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年7月3日 下午2:54:28
 * @version V1.0
 */
public class TableRepairer {

	public Element repairTable(Element element) {
		formatElements(element);
		// 去除不需要的表头tr
		repairTableByRemoveTr(element);
		// 修复 通过边界
		Element newTable = repairTableBySolid(element);
		// 修复 通过宽度
		repairTableByWidth(newTable);
		// 去除统计信息
		repairTableByRemoveStatisTr(newTable);
		return newTable;

	}

	private void repairTableByRemoveStatisTr(Element newTable) {
		Elements trElements = newTable.select("tr");

		for (int i = 0; i < trElements.size(); i++) {
			Element trElement = trElements.get(i);
			Elements tdElements = trElement.select("td");
			if (tdElements.size() == 0) {
				trElement.remove();
				continue;
			}
			Element tdElement = tdElements.get(0);
			String tdText = tdElement.text();
			String[] statisStr = new String[] { "合计", "总计", "总股本", "普通股股东人数", "普通股总股本" };
			boolean isRemove = false;
			for (String string : statisStr) {
				if (tdText.equals(string)) {
					isRemove = true;
					break;
				}
			}

			String[] statisStr2 = new String[] { "董事会人数：", "监事会人数：", "高级管理人员人数：" };
			for (String string : statisStr2) {
				if (tdText.contains(string)) {
					isRemove = true;
					break;
				}
			}

			if (isRemove) {
				for (int j = i; j < trElements.size(); j++) {
					Element trElement1 = trElements.get(j);
					if (trElement1 != null) {
						trElement1.remove();
					}

				}
				break;
			}

		}

	}

	private Element repairTableBySolid(Element resultTable) {
		Elements trElements = resultTable.select("tr");
		Element newTable = new Element(Tag.valueOf("table"), "");
		newTable.attr("border", "1");
		for (int i = 0; i < trElements.size(); i++) {
			Element newTrElement = new Element(Tag.valueOf("tr"), "");
			Element trElement = trElements.get(i);
			// 如果height为0
			Elements tdElements = trElement.select("td");
			for (int j = 0; j < tdElements.size(); j++) {
				Element newTdElement = new Element(Tag.valueOf("td"), "");
				Element tdElement = tdElements.get(j);
				String tdElementText = tdElement.text();
				int width = 0;
				try {
					if (tdElement.hasAttr("width")) {
						width = Integer.parseInt(tdElement.attr("width"));
					} else {
						if (tdElement.hasAttr("style")) {
							String style = tdElement.attr("style");
							if (style.contains("width")) {
								String widthStr = StringUtils.substringBetween(style, "width:", ";");
								if (widthStr.contains("pt")) {
									width = Integer.parseInt(StringUtils.substringBetween(style, "width:", "."));
								} else if (widthStr.contains("cm")) {
									width = Integer.parseInt(StringUtils.substringBetween(style, "width:", "."));
									width = width * 28;
								}
								tdElement.attr("width", width + "");
							} else {
								tdElement.attr("width", "0");
							}
						} else {
							tdElement.attr("width", "0");
						}

					}
				} catch (Exception e) {
					continue;
				}
				if (tdElement.hasAttr("rowspan")) {
					newTdElement.attr("rowspan", tdElement.attr("rowspan"));
				}
				// 如果是最后一列
				if (j == tdElements.size() - 1) {
					newTdElement.attr("width", width + "");
					newTdElement.text(tdElementText);
					newTrElement.appendChild(newTdElement);
					break;
				}

				String iStyle = tdElement.attr("style");
				iStyle = iStyle.replace("\r\n", "");
				iStyle = iStyle.replace(" ", "");
				iStyle = iStyle.replace(" ", "");
				// System.out.println(iStyle);
				for (int k = j + 1; k < tdElements.size(); k++) {
					Element nextTdElement = tdElements.get(k);
					String nextTdElementText = nextTdElement.text();
					String style = nextTdElement.attr("style");
					style = style.replace("\r\n", "");
					style = style.replace(" ", "");
					style = style.replace(" ", "");
					if (nextTdElement.hasAttr("width")) {
					} else {
						if (nextTdElement.hasAttr("style")) {
							if (style.contains("width")) {
								String widthStr = StringUtils.substringBetween(style, "width:", ";");
								int tempWidth = 0;
								if (widthStr.contains("pt")) {
									tempWidth = Integer.parseInt(StringUtils.substringBetween(style, "width:", "."));
								} else if (widthStr.contains("cm")) {
									tempWidth = Integer.parseInt(StringUtils.substringBetween(style, "width:", "."));
									tempWidth = tempWidth * 28;
								}
								nextTdElement.attr("width", tempWidth + "");
							} else {
								nextTdElement.attr("width", "0");
							}
						} else {
							nextTdElement.attr("width", "0");
						}
					}
					// 左边没有右侧， 右边没有左侧
					if (iStyle.contains("border-right:none") && style.contains("border-left:none")) {
						// 右边没有左侧 ，右边也没有右侧
						if (style.contains("border-right:none")) {
							// 跨了好几个列
							String tempStyle = nextTdElement.attr("style");
							StringBuffer textBuffer = new StringBuffer();
							textBuffer.append(tdElementText);
							// 如果左侧一直没有
							// int colnum = 0;
							while (tempStyle.contains("border-left:none")) {
								if (tempStyle.contains("border:solid#5B9BD51.0pt")
										|| tempStyle.contains("border:solidblack1.0pt")) {
									break;
								} else {
									Element tempElement = tdElements.get(k++);
									tempStyle = tempElement.attr("style");
									tempStyle = tempStyle.replace("\r\n", "");
									tempStyle = tempStyle.replace(" ", "");
									tempStyle = tempStyle.replace(" ", "");
									textBuffer.append(tempElement.text().trim());
									if (tempElement.hasAttr("width")) {
									} else {
										if (tempElement.hasAttr("style")) {
											if (tempStyle.contains("width")) {
												String widthStr = StringUtils.substringBetween(style, "width:", ";");
												int tempWidth = 0;
												if (widthStr.contains("pt")) {
													tempWidth = Integer.parseInt(
															StringUtils.substringBetween(style, "width:", "."));
												} else if (widthStr.contains("cm")) {
													tempWidth = Integer.parseInt(
															StringUtils.substringBetween(style, "width:", "."));
													tempWidth = tempWidth * 28;
												}
												tempElement.attr("width", tempWidth + "");
											} else {
												tempElement.attr("width", "0");
											}
										} else {
											tempElement.attr("width", "0");
										}
									}
									width = width + Integer.parseInt(tempElement.attr("width"));
									j++;
									if (k >= tdElements.size()) {
										break;
									}
								}
							}
							newTdElement.attr("width", width + "");
							newTdElement.text(textBuffer.toString());
							newTrElement.appendChild(newTdElement);

						} else {
							j++;
							width = width + Integer.parseInt(nextTdElement.attr("width"));
							newTdElement.attr("width", width + "");
							newTdElement.text(tdElementText + nextTdElementText);
							newTrElement.appendChild(newTdElement);
						}
						break;

					} else if (iStyle.contains("border-right:none") && style.contains("border:none;")
							&& !style.contains("border-left:solid")) {

						// 跨了好几个列
						String tempStyle = nextTdElement.attr("style");
						StringBuffer textBuffer = new StringBuffer();
						textBuffer.append(tdElementText);
						// 如果左侧一直没有
						while (tempStyle.contains("border:none")) {
							Element tempElement = tdElements.get(k++);
							tempStyle = tempElement.attr("style");
							tempStyle = tempStyle.replace("\r\n", "");
							tempStyle = tempStyle.replace(" ", "");
							tempStyle = tempStyle.replace(" ", "");
							textBuffer.append(tempElement.text().trim());
							if (tempElement.hasAttr("width")) {
							} else {
								if (tempElement.hasAttr("style")) {
									if (tempStyle.contains("width")) {
										String widthStr = StringUtils.substringBetween(style, "width:", ";");
										int tempWidth = 0;
										if (widthStr.contains("pt")) {
											tempWidth = Integer
													.parseInt(StringUtils.substringBetween(style, "width:", "."));
										} else if (widthStr.contains("cm")) {
											tempWidth = Integer
													.parseInt(StringUtils.substringBetween(style, "width:", "."));
											tempWidth = tempWidth * 28;
										}
										tempElement.attr("width", tempWidth + "");
									} else {
										tempElement.attr("width", "0");
									}
								} else {
									tempElement.attr("width", "0");
								}
							}
							width = width + Integer.parseInt(tempElement.attr("width"));
							j++;
							if (k >= tdElements.size()) {
								break;
							}
						}
						newTdElement.attr("width", width + "");
						newTdElement.text(textBuffer.toString());
						newTrElement.appendChild(newTdElement);
						break;

					} else if (iStyle.contains("border:none") && !iStyle.contains("border-right:solid")
							&& style.contains("border:none") && !style.contains("border-left:solid")) {

						// 跨了好几个列
						String tempStyle = nextTdElement.attr("style");
						StringBuffer textBuffer = new StringBuffer();
						textBuffer.append(tdElementText);
						// 如果左侧一直没有
						while (tempStyle.contains("border:none")) {
							Element tempElement = tdElements.get(k++);
							tempStyle = tempElement.attr("style");
							tempStyle = tempStyle.replace("\r\n", "");
							tempStyle = tempStyle.replace(" ", "");
							tempStyle = tempStyle.replace(" ", "");
							textBuffer.append(tempElement.text().trim());
							try {
								if (tempElement.hasAttr("width")) {
								} else {
									if (tempElement.hasAttr("style")) {
										if (tempStyle.contains("width")) {
											String widthStr = StringUtils.substringBetween(style, "width:", ";");
											int tempWidth = 0;
											if (widthStr.contains("pt")) {
												tempWidth = Integer
														.parseInt(StringUtils.substringBetween(style, "width:", "."));
											} else if (widthStr.contains("cm")) {
												tempWidth = Integer
														.parseInt(StringUtils.substringBetween(style, "width:", "."));
												tempWidth = tempWidth * 28;
											}
											tempElement.attr("width", tempWidth + "");
										} else {
											tempElement.attr("width", "0");
										}
									} else {
										tempElement.attr("width", "0");
									}
								}
								width = width + Integer.parseInt(tempElement.attr("width"));
							} catch (Exception e) {
								System.out.println(tempElement);
								e.printStackTrace();
							}
							j++;
							if (k >= tdElements.size()) {
								break;
							}
						}
						newTdElement.attr("width", width + "");
						newTdElement.text(textBuffer.toString());
						newTrElement.appendChild(newTdElement);
						break;
					} else if (iStyle.contains("border:none") && !iStyle.contains("border-right:solid")
							&& (!style.contains("border-left:solid") || !style.contains("border:none"))) {
						width = width + Integer.parseInt(nextTdElement.attr("width"));
						j++;
						newTdElement.attr("width", width + "");
						newTdElement.text(tdElementText + nextTdElementText);
						newTrElement.appendChild(newTdElement);
						break;
					} else {
						newTdElement.attr("width", width + "");
						newTdElement.text(tdElementText);
						newTrElement.appendChild(newTdElement);
						break;
					}

				}

			}
			newTable.appendChild(newTrElement);
		}
		return newTable;
	}

	private Element repairTableByRemoveStyle(Element resultTable) {
		Elements trElements = resultTable.select("tr");
		Element newTable = new Element(Tag.valueOf("table"), "");
		newTable.attr("border", "1");
		for (int i = 0; i < trElements.size(); i++) {
			Element newTrElement = new Element(Tag.valueOf("tr"), "");
			Element trElement = trElements.get(i);
			// 如果height为0
			if (trElement.hasAttr("height")) {
				if (trElement.attr("height").equals("0")) {
					continue;
				}
			}
			Elements tdElements = trElement.select("td");
			for (int j = 0; j < tdElements.size(); j++) {
				Element newTdElement = new Element(Tag.valueOf("td"), "");
				Element tdElement = tdElements.get(j);
				String tdElementText = tdElement.text();
				if (tdElement.hasAttr("width")) {
					newTdElement.attr("width", tdElement.attr("width"));
				}
				if (tdElement.hasAttr("rowspan")) {
					newTdElement.attr("rowspan", tdElement.attr("rowspan"));
				}
				newTdElement.text(tdElementText);
				newTrElement.appendChild(newTdElement);

			}
			newTable.appendChild(newTrElement);
		}
		return newTable;
	}

	public void repairTableByWidth(Element souceTable) {
		Elements trElements = souceTable.select("tr");
		if (trElements.size() == 0) {
			return;
		}
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
		for (int i = 0; i < titleNum; i++) {
			if (i + 1 >= trElements.size() - 1) {
				// do noting
				break;
			}
			Element trElement = trElements.get(i);
			Elements tdElements = trElement.select("td");
			for (int j = 0; j < tdElements.size(); j++) {
				Element tdElement = tdElements.get(j);
				int nextRowIndex = i;
				if (tdElement.hasAttr("rowspan")) {
					nextRowIndex = nextRowIndex + Integer.parseInt(tdElement.attr("rowspan")) - 1;
				}
				int width = Integer.parseInt(tdElement.attr("width"));
				int nextColIndex = j;
				for (int k = 0; k < j; k++) {
					Element preTdElement = tdElements.get(k);
					if (preTdElement.hasAttr("rowspan")) {

						if (i <= Integer.parseInt(preTdElement.attr("rowspan"))) {
							if (tdElement.hasAttr("rowspan")) {
								if (i + Integer.parseInt(tdElement.attr("rowspan")) >= Integer
										.parseInt(preTdElement.attr("rowspan"))) {
									if (preTdElement.hasAttr("colspan")) {
										nextColIndex = nextColIndex + Integer.parseInt(preTdElement.attr("colspan"))
												- 1;
									}
								} else {
									nextColIndex = nextColIndex - 1;
								}
							} else {
								nextColIndex = nextColIndex - 1;
							}

						} else {
							nextColIndex = nextColIndex - 1;
							if (preTdElement.hasAttr("colspan")) {
								nextColIndex = nextColIndex + Integer.parseInt(preTdElement.attr("colspan")) - 1;
							}
						}
					} else {
						if (preTdElement.hasAttr("colspan")) {
							nextColIndex = nextColIndex + Integer.parseInt(preTdElement.attr("colspan")) - 1;
						}
					}
				}
				if (nextRowIndex + 1 >= trElements.size()) {
					break;
				}
				// 如果下一列的索引位置大于 下一列的值，就放弃
				if (nextColIndex >= trElements.get(nextRowIndex + 1).select("td").size()) {
					break;
				}

				Elements nextRowTdElements = trElements.get(nextRowIndex + 1).select("td");
				if (nextColIndex >= nextRowTdElements.size()) {
					break;
				}
				Element nextRowTd = nextRowTdElements.get(nextColIndex);
				int nextRowWidth = Integer.parseInt(nextRowTd.attr("width"));
				if (width == nextRowWidth) {
					String nextRowTdText = nextRowTd.text();
					nextRowTdText = replacekong(nextRowTdText);
					if (nextRowTdText.isEmpty() && nextRowTdElements.size() > tdElements.size()) {
						if (tdElement.hasAttr("rowspan")) {
							tdElement.attr("rowspan", (Integer.parseInt(tdElement.attr("rowspan")) + 1) + "");
						} else {
							tdElement.attr("rowspan", "2");
						}
						if (nextRowTd.hasAttr("colspan")) {
							tdElement.attr("colspan", nextRowTd.attr("colspan"));
						}
						nextRowTd.remove();
						j--;
					}

				} else if (width > nextRowWidth) {
					int colspan = 0;
					int sumWidth = 0;
					// 说明需要合并单元格
					for (int k = nextColIndex; k < nextRowTdElements.size(); k++) {
						Element nextTempRowTd = nextRowTdElements.get(k);
						int nextRowTempWidth = Integer.parseInt(nextTempRowTd.attr("width"));
						sumWidth = sumWidth + nextRowTempWidth;
						if (sumWidth <= width) {
							colspan++;
						} else if (sumWidth - width < 3) {
							colspan++;
						} else {
							break;
						}
					}
					if (tdElement.hasAttr("colspan")) {
						colspan = colspan + Integer.parseInt(tdElement.attr("colspan"));
					}
					if (colspan > 1) {
						tdElement.attr("colspan", "" + colspan);
						for (int m = i - 1; m >= 0; m--) {
							Element preTrElement = trElements.get(m);
							Elements preTdElements = preTrElement.select("td");
							int preWidth = 0;
							int k = 0;
							if (m == 0) {
								k = 1;
							}
							for (; k < preTdElements.size(); k++) {
								Element preTdElement = preTdElements.get(k);
								preWidth = preWidth + Integer.parseInt(preTdElement.attr("width"));
								if (width < preWidth) {
									if (preTdElement.hasAttr("colspan")) {
										preTdElement.attr("colspan",
												(Integer.parseInt(preTdElement.attr("colspan")) + colspan - 1) + "");

									} else {
										preTdElement.attr("colspan", (colspan - 1) + "");
									}

									break;
								}

							}
						}

					}

				}

			}

		}

	}

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

	public void repairTableByRemoveTr(Element resultTable) {
		Elements trElements = resultTable.select("tr");
		if (trElements.size() == 0) {
			return;
		}
		for (int i = 0; i < trElements.size(); i++) {
			Element trElement = trElements.get(i);
			if (trElement.hasAttr("height")) {
				if (trElement.attr("height").equals("0")) {
					trElements.get(i).remove();
					continue;
				}
			}
			String trText = trElement.text().trim();
			trText = replacekong(trText);
			if (trText.isEmpty()) {
				if (i - 1 >= 0) {
					Element preTrElement = trElements.get(i - 1);
					Elements tdElements = preTrElement.select("td");
					for (Element tdElement : tdElements) {
						if (tdElement.hasAttr("rowspan")) {
							int rowspan = Integer.parseInt(tdElement.attr("rowspan")) - 1;
							if (rowspan == 1 || rowspan == 0) {
								tdElement.removeAttr("rowspan");
							} else {
								tdElement.attr("rowspan", rowspan + "");
							}

						}
					}
				}
				trElements.get(i).remove();
				continue;
			}

		}
		Element trElement = trElements.get(0);
		Elements tdElements = trElement.select("td");
		Element tdElement = tdElements.get(0);
		if (tdElement.text().contains("信息统计")) {
			if (tdElement.hasAttr("rowspan")) {
				int rowspan = Integer.parseInt(tdElement.attr("rowspan"));
				for (int i = 0; i < rowspan; i++) {
					if (i < trElements.size()) {
						trElements.get(i).remove();
					}
				}
			}
		}
	}

	public void formatElements(Element contentElement) {
		// 去重属性
		removeElementAttr(contentElement);
		Elements allElements = contentElement.children();
		for (Element element : allElements) {
			try {
				removeElementAttr(element);
				if (element != null) {
					formatElements(element);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;

			}

		}

	}

	// 移除所有的属性

	public void removeElementAttr(Element element) {
		if (element == null) {
			return;
		}
		Attributes attributes = element.attributes();
		Iterator<Attribute> iterator = attributes.iterator();
		while (iterator.hasNext()) {
			Attribute attribute = iterator.next();
			if (attribute.getKey().equals("rowspan") || attribute.getKey().equals("colspan")
					|| attribute.getKey().equals("src") || attribute.getKey().equals("height")
					|| attribute.getKey().equals("width") || attribute.getKey().equals("style")) {
				continue;
			}
			element.removeAttr(attribute.getKey());
			iterator = attributes.iterator();
		}
	}

	/***
	 * 
	 * @param preEnd
	 * @return
	 */
	public String replacekong(String text) {
		text = text.replace(" ", "");
		text = text.replace(" ", "");
		text = text.replace(" ", "");
		text = text.replace(" ", "");
		return text;
	}
}
