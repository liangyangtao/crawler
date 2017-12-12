package com.kf.data.neeq.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.fetcher.tools.AliOssSender;
import com.kf.data.mybatis.entity.NeeqInformationLawsCrawlerWithBLOBs;
import com.kf.data.neeq.store.NeeqInformationLawsCrawlerStore;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NeeqLawsCrawler {

	static AliOssSender aliOssSender = new AliOssSender();

	public static void main(String[] args) {

		String url = null;
		String[] strs = { "105", "106", "107", "108" };
		for (String type : strs) {
			if (type.equals("105")) {
				url = "http://www.neeq.com.cn/info/list.do?page=0&pageSize=10&keywords=&nodeId=105";
			} else if (type.equals("106")) {
				url = "http://www.neeq.com.cn/info/list.do?page=0&pageSize=30&keywords=&nodeId=106";
			} else if (type.equals("107") || type.equals("108")) {
				url = "http://www.neeq.com.cn/info/node_list.do?nodeId=" + type;
			}
			String html = Fetcher.getInstance().get(url);
			parser(type, html);
		}

	}

	private static void parser(String type, String html) {
		if (type.equals("105") | type.equals("106")) {
			String page = StringUtils.substringBetween(html, "null([{\"result\":true,\"data\":{\"content\":",
					",\"firstPage");
			JSONArray array = JSONArray.fromObject(page);
			try {
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = array.getJSONObject(i);

					NeeqInformationLawsCrawlerWithBLOBs neeqInformationLawsCrawler = new NeeqInformationLawsCrawlerWithBLOBs();
					int nodeId = object.getInt("nodeId");
					if (nodeId == 105) {
						neeqInformationLawsCrawler.setCategoryName("法律法规");
					}
					if (nodeId == 106) {
						neeqInformationLawsCrawler.setCategoryName("部门规章");
					}

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = sdf.parse(object.getString("publishDate"));

					neeqInformationLawsCrawler.setReleaseDate(date);
					neeqInformationLawsCrawler.setLawsTitle(object.getString("title"));
					neeqInformationLawsCrawler.setSummary(object.getString("metaDescription"));
					// neeqInformationLawsCrawler.setText(html);
					Map<String, String> map = new HashMap<>();
					if (StringUtils.isNotEmpty(object.getString("linkUrl"))) {

						String file = "http://www.neeq.com.cn" + object.getString("linkUrl");
						if (file.endsWith(".html") || file.endsWith(".cn") || file.endsWith(".shtml")) {

						} else {

							neeqInformationLawsCrawler.setLawsUrl(file);
							String body = null;
							try {
								body = aliOssSender.uploadObject(file);
							} catch (Exception e) {
								e.printStackTrace();
							}
							neeqInformationLawsCrawler.setLawsUrl(body);
							map.put(neeqInformationLawsCrawler.getLawsTitle(), body);
							String json = JsonUtils.parseToJsonData(map);
							neeqInformationLawsCrawler.setJsonResult(json);
						}
					}

					String link = "http://www.neeq.com.cn" + object.getString("htmlUrl");
					if (link.contains(".html")) {
						String detailHtml = Fetcher.getInstance().get(link);
						Document doc = Jsoup.parse(detailHtml);
						Elements p = doc.select("div.txt>p");
						String pContent = p.toString();
						// Elements ahref = p.select("a[href]");
						// if (ahref != null) {
						// for (Element e : ahref) {
						// String href = "http://www.neeq.com.cn" +
						// e.attr("href");
						// if (href.endsWith("pdf") || href.endsWith("docx") ||
						// href.endsWith("doc")) {
						// String a = e.text();
						// String img = null;
						// try {
						// img = aliOssSender.uploadObject(href);
						// } catch (Exception e1) {
						// e1.printStackTrace();
						// }
						// String regex = "<a[^>]*>" + a + "</a>";
						// pContent = pContent.replaceAll(regex, "<a href=\"" +
						// img + "\">" + a + "</a>");
						// map.put(a, img);
						// String json = JsonUtils.parseToJsonData(map);
						// neeqInformationLawsCrawler.setJsonResult(json);
						// } 
						// }
						// }

						String regex = "<p[^>]*><s[^>]*>";
						String content = pContent.replaceAll(regex, "<p>").replaceAll("<span[^>]*>", "")
								.replaceAll("</strong>", "").replace("</span>", "").replaceAll("<p[^>]*>", "<p>")
								.replaceAll("<h[^>]*>", "").replaceAll("</h>", "").replaceAll("<strong>", "")
								.replaceAll("<img[^>]*>", "").replaceAll("<p[^>]*><span[^>]*>", "<p>");
						neeqInformationLawsCrawler.setContent(content);
					} else {
						neeqInformationLawsCrawler.setContent("");
					}
					new NeeqInformationLawsCrawlerStore().saveNeeqInformationLawsCrawler(neeqInformationLawsCrawler);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String page = StringUtils.substringBetween(html, "null([{\"result\":true,\"data\":", "}])");
			JSONArray array = JSONArray.fromObject(page);
			for (int i = 0; i < array.size(); i++) {
				try {
					JSONObject object = array.getJSONObject(i);
					String info = object.toString();
					NeeqBean nb = new Gson().fromJson(info, NeeqBean.class);
					List<InfoBean> list = nb.getInfo();
					for (int y = 0; y < list.size(); y++) {
						NeeqInformationLawsCrawlerWithBLOBs nl = new NeeqInformationLawsCrawlerWithBLOBs();
						// if (list.get(y).getFileUrl().endsWith(".html") ||
						// list.get(y).getFileUrl().endsWith(".cn")
						// || list.get(y).getFileUrl().endsWith(".shtml")) {
						// continue;
						// }
						if (list.get(y).getText().contains(
								"<style type=\"text/css\">table {border-collapse: collapse;} table td {border-width: 1px; border-style: solid;}</style>")) {
							list.get(y).setText("");
						}
						// List<String> pdfList = new ArrayList<String>();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = sdf.parse(list.get(y).getPublishDate());
						nl.setCategoryName(nb.getNodeName());
						Map<String, String> map = new HashMap<>();
						if (StringUtils.isNotEmpty(list.get(y).getText())) {
							String regexP = "<p[^>]*><s[^>]*>";
							String text = list.get(y).getText();
							// Document doc = Jsoup.parse(text);
							// Elements es = doc.select("a[href]");
							// for (Element e : es) {
							// String href = "http://www.neeq.com.cn" +
							// e.attr("href");
							// if (href.endsWith("pdf") || href.endsWith("docx")
							// || href.endsWith("doc")
							// || href.endsWith("rar")) {
							// String a = e.text();
							// String img = null;
							// try {
							// img = aliOssSender.uploadObject(href);
							// } catch (Exception e1) {
							// // TODO Auto-generated catch block
							// e1.printStackTrace();
							// }
							// map.put(a, img);
							// pdfList.add(img);
							// String regex = "<a[^>]*>" + a + "</a>";
							// text = text.replaceAll(regex, "<a href=\"" + img
							// + "\">" + a + "</a>");
							// }
							// }
							// String pdf = listToString(pdfList);
							String content = text.replaceAll(regexP, "<p>").replaceAll("</span>", "")
									.replaceAll("</strong>", "").replaceAll("<p[^>]*>", "<p>")
									.replaceAll("<span[^>]*>", "").replaceAll("<h[^>]*>", "").replaceAll("</h>", "")
									.replaceAll("<strong>", "").replaceAll("<img[^>]*>", "")
									.replaceAll("<p[^>]*><span[^>]*>", "<p>");
							nl.setContent(content);
							nl.setLawsUrl("");
							String json = JsonUtils.parseToJsonData(map);
							nl.setJsonResult(json);

						} else {
							String file = "";
							if (StringUtils.isNotEmpty(list.get(y).getFileUrl())) {
								file = "http://www.neeq.com.cn" + list.get(y).getFileUrl();
							} else {
								file = "http://www.neeq.com.cn" + list.get(y).getLinkUrl();
							}
							if (file.endsWith(".html")) {
								// nl.setLawsUrl(null);
								// 修改行
								try {
									String detailHtml = Fetcher.getInstance().get(file);
									Document doc = Jsoup.parse(detailHtml);
									Elements p = doc.select("div.txt>p");
									String pContent = p.toString();
									// Elements ahref = p.select("a[href]");
									// if (ahref != null) {
									// for (Element e : ahref) {
									// String href = "http://www.neeq.com.cn" +
									// e.attr("href");
									// if (href.contains("pdf") |
									// href.contains("docx") |
									// href.contains("doc")) {
									// String a = e.text();
									// String img = null;
									// try {
									// img = aliOssSender.uploadObject(href);
									// } catch (Exception e1) {
									// e1.printStackTrace();
									// }
									// String regex = "<a[^>]*>" + a + "</a>";
									// pContent = pContent.replaceAll(regex,
									// "<a href=\"" + img + "\">" + a + "</a>");
									// map.put(a, img);
									// String json =
									// JsonUtils.parseToJsonData(map);
									// nl.setJsonResult(json);
									// }
									// }
									// }

									String regex = "<p[^>]*><s[^>]*>";
									String content = pContent.replaceAll(regex, "<p>").replaceAll("<span[^>]*>", "")
											.replaceAll("</strong>", "").replace("</span>", "")
											.replaceAll("<p[^>]*>", "<p>").replaceAll("<h[^>]*>", "")
											.replaceAll("</h>", "").replaceAll("<strong>", "")
											.replaceAll("<img[^>]*>", "").replaceAll("<p[^>]*><span[^>]*>", "<p>");
									nl.setContent(content);

								} catch (Exception e) {
									e.printStackTrace();
								}
								// 修改行
							} else {
								if (file.endsWith(".cn") || file.endsWith(".shtml")) {
//									nl.setJsonResult(json);
//									nl.setLawsUrl(img);
								} else {

									String img = null;
									try {
										img = aliOssSender.uploadObject(file);
									} catch (Exception e) {
										e.printStackTrace();
									}
									map.put(list.get(y).getTitle(), img);
									String json = JsonUtils.parseToJsonData(map);
									nl.setJsonResult(json);
									nl.setLawsUrl(img);
								}
							}
						}
						nl.setLawsTitle(list.get(y).getTitle());
						nl.setSummary(list.get(y).getMetaDescription());
						nl.setText(html);
						nl.setReleaseDate(date);
						new NeeqInformationLawsCrawlerStore().saveNeeqInformationLawsCrawler(nl);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String listToString(List<String> stringList) {
		if (stringList == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (String string : stringList) {
			if (flag) {
				result.append(",");
			} else {
				flag = true;
			}
			result.append(string);
		}
		return result.toString();
	}

}
