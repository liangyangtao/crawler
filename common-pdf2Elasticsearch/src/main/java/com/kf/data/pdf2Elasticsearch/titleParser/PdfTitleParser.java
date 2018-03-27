package com.kf.data.pdf2Elasticsearch.titleParser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.pdf2Elasticsearch.entity.TitleTreeNode;


public class PdfTitleParser {

	
	public List<TitleTreeNode> simpleDocument(Document document) {
		List<TitleTreeNode> titleTreeNodes = new ArrayList<TitleTreeNode>();
		Elements divElements = document.body().children();
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
					// 一级
					if (text.matches("\\s{0,100}第[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+节.*")
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
					if (text.matches("\\s{0,100}[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+、.*")) {
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
					if (text.matches("\\s{0,100}（[一|二|三|四|五|六|七|八|九|十|十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|二十一|二十二|二十三|二十四|二十五]+）.*")) {
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
					if (text.matches("\\s{0,100}[1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25]+、.*")) {
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
		return titleTreeNodes;
	}

}
