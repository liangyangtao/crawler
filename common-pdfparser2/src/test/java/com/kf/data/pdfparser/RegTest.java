package com.kf.data.pdfparser;

public class RegTest {

	public static void main(String[] args) {

		
		String text ="（十）主要会计政策";
		System.out.println(text.matches("（[十|十五]+）主要会计政策"));
	}
}
