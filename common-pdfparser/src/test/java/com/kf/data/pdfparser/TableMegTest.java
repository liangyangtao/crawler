package com.kf.data.pdfparser;

import java.io.File;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.kf.data.fetcher.tools.TableRepairer;
import com.kf.data.fetcher.tools.TableSpliter;

public class TableMegTest {

	public static void main(String[] args) {
		try {
			File in = new File("C:/Users/meidi/Desktop/a.html");
			Document document = Jsoup.parse(in, "gbk");
            Element table =new TableRepairer().repairTable(document);
            System.out.println(table);
//			System.out.println(new TableSpliter().splitTable(table, false, null));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		Calendar calendar =Calendar.getInstance();
		calendar.set(2017, 5, 30);
		System.out.println(calendar.getTime().toLocaleString());
	}

}
