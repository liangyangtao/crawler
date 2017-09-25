package com.kf.data.pdfparser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.kf.data.fetcher.Fetcher;
import com.kf.data.pdfparser.entity.TitleTreeNode;

public class PdFTitleTest {
	static String[] titleTags = new String[] { "一)", "二)", "三)", "四)", "五)", "六)", "七)", "八)", "九)", "一）", "二）", "三）",
			"四）", "五）", "六）", "七）", "八）", "九）", "1)", "2)", "3)", "4)", "5)", "6)", "7)", "8)", "9)", "1）", "2）", "3）",
			"4）", "5）", "6）", "7）", "8）", "9）", "第一节", "第二节", "第三节", "第四节", "第五节", "第六节", "第七节", "第八节", "第九节", "第十节",
			"第十一节", "第十二节", "第十三节", "第十四节", "第十五节", "一、", "二、", "三、", "四、", "五、", "六、", "七、", "八、", "九、", "十、" };
	List<TitleTreeNode> titleTreeNodes = new ArrayList<TitleTreeNode>();

	public static void main(String[] args) {
		String url = "https://static.kaifengdata.com/neeq/05a100aacb2eb5920c4ddb6edf30c60e/[定期报告]三炬生物 2017年半年度报告.pdf.htm";
		url = changeHanzi(url);
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
		new PdFTitleTest().simpleDocument(document);

	}

	public static String changeHanzi(String url) {
		char[] tp = url.toCharArray();
		String now = "";
		for (char ch : tp) {
			if (ch >= 0x4E00 && ch <= 0x9FA5) {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == '[') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ']') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (ch == ' ') {
				try {
					now += URLEncoder.encode(ch + "", "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				now += ch;
			}

		}
		return now;
	}

	public Element simpleDocument(Document document) {
		Elements divElements = document.body().children();
		Element newBody = new Element(Tag.valueOf("div"), "");
		int nodeId = 0;
		int firstId = 0;
		int scondId = 0;
		int threeId = 0;
		int rank = 1;

		for (Element element : divElements) {
			Elements pElements = element.children();
			if (pElements.size() > 0) {
				for (Element element2 : pElements) {
					String elementText = element2.text();
					String text = elementText;
					if (elementText.contains("。")) {
						String texttemp[] = elementText.split("。");
						text = texttemp[texttemp.length - 1];
					}
					if (text.length() > 0) {

					}
					// 一级
					if (text.matches(
							"\\s{0,100}第[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+节.*")
							&& text.length() < 20) {
						nodeId++;
						// System.out.println(text);
						TitleTreeNode titleTreeNode = new TitleTreeNode();
						titleTreeNode.setCname(text);
						titleTreeNode.setCid(nodeId);
						titleTreeNode.setPid(0);
						titleTreeNodes.add(titleTreeNode);
						firstId = nodeId;
						rank = 1;
					}

					if (text.matches("\\s{0,100}.*财务报表附注") && text.length() < 20) {
						nodeId++;
						// System.out.println(text);
						TitleTreeNode titleTreeNode = new TitleTreeNode();
						titleTreeNode.setCname(text);
						titleTreeNode.setCid(nodeId);
						titleTreeNode.setPid(0);
						titleTreeNodes.add(titleTreeNode);
						firstId = nodeId;
						rank = 1;
					}

					// 二级
					if (text.matches(
							"\\s{0,100}[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+、.*")) {
						// System.out.println(text);
						nodeId++;
						TitleTreeNode titleTreeNode = new TitleTreeNode();
						titleTreeNode.setCname(text);
						titleTreeNode.setCid(nodeId);
						titleTreeNode.setPid(firstId);
						titleTreeNodes.add(titleTreeNode);
						scondId = nodeId;
						rank = 2;
					}

					// 二级
					if (text.matches("【.*】") && text.length() < 25) {
						// System.out.println(text);
						nodeId++;
						TitleTreeNode titleTreeNode = new TitleTreeNode();
						titleTreeNode.setCname(text);
						titleTreeNode.setCid(nodeId);
						titleTreeNode.setPid(firstId);
						titleTreeNodes.add(titleTreeNode);
						scondId = nodeId;
						rank = 2;
					}

					// 三级
					if (text.matches(
							"\\s{0,100}（[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+）.*")) {
						// System.out.println(text);
						nodeId++;
						TitleTreeNode titleTreeNode = new TitleTreeNode();
						titleTreeNode.setCname(text);
						titleTreeNode.setCid(nodeId);
						titleTreeNode.setPid(scondId);
						titleTreeNodes.add(titleTreeNode);
						threeId = nodeId;
						rank = 3;
					}

					// 四级
					// if
					// (text.matches("[1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25]\\..*"))
					// {
					// // System.out.println(text);
					// String temp = text.substring(0, 8);
					// if(temp.contains("%")){
					// continue;
					// }
					// nodeId++;
					// if (rank == 3) {
					// TitleTreeNode titleTreeNode = new TitleTreeNode();
					// titleTreeNode.setCname(text);
					// titleTreeNode.setCid(nodeId);
					// titleTreeNode.setPid(threeId);
					// titleTreeNodes.add(titleTreeNode);
					// } else if (rank == 2) {
					// TitleTreeNode titleTreeNode = new TitleTreeNode();
					// titleTreeNode.setCname(text);
					// titleTreeNode.setCid(nodeId);
					// titleTreeNode.setPid(scondId);
					// titleTreeNodes.add(titleTreeNode);
					// threeId = nodeId;
					// }
					// }
					// 四级 er
					if (text.matches(
							"\\s{0,100}[1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25]+、.*")) {
						// System.out.println(text);
						nodeId++;
						if (rank == 3) {
							TitleTreeNode titleTreeNode = new TitleTreeNode();
							titleTreeNode.setCname(text);
							titleTreeNode.setCid(nodeId);
							titleTreeNode.setPid(threeId);
							titleTreeNodes.add(titleTreeNode);
						} else if (rank == 2) {
							TitleTreeNode titleTreeNode = new TitleTreeNode();
							titleTreeNode.setCname(text);
							titleTreeNode.setCid(nodeId);
							titleTreeNode.setPid(scondId);
							titleTreeNodes.add(titleTreeNode);
							threeId = nodeId;
						}
					}
				}

			} else {
			}

		}

		for (TitleTreeNode firstTitleTreeNode : titleTreeNodes) {
			if (firstTitleTreeNode.getPid() == 0) {
				String firstTitle = firstTitleTreeNode.getCname();
				boolean isScond = false;
				for (TitleTreeNode secondTitleTreeNode : titleTreeNodes) {
					if (secondTitleTreeNode.getPid() == firstTitleTreeNode.getCid()) {
						isScond = true;
						String secondTitle = secondTitleTreeNode.getCname();
						boolean isThree = false;
						for (TitleTreeNode threeTitleTreeNode : titleTreeNodes) {
							if (threeTitleTreeNode.getPid() == secondTitleTreeNode.getCid()) {
								isThree = true;
								String threeTitle = threeTitleTreeNode.getCname();
								boolean isFour = false;
								for (TitleTreeNode fourTitleTreeNode : titleTreeNodes) {
									if (fourTitleTreeNode.getPid() == threeTitleTreeNode.getCid()) {
										String fourTitle = fourTitleTreeNode.getCname();
										isFour = true;
										System.out.println(firstTitle + "------------" + secondTitle + "------------"
												+ threeTitle + "---------" + fourTitle);
									}
								}
								if (!isFour) {
									System.out.println(
											firstTitle + "------------" + secondTitle + "------------" + threeTitle);
								}

							}

						}
						if (!isThree) {
							System.out.println(firstTitle + "------------" + secondTitle);
						}

					}
				}
				if (!isScond) {
					System.out.println(firstTitle);
				}
			}
		}

		return newBody;
	}

}
