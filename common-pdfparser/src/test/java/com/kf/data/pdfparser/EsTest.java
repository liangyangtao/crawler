package com.kf.data.pdfparser;

import com.kf.data.fetcher.tools.KfConstant;
import com.kf.data.pdfparser.es.PdfReportTextReader;

public class EsTest {

	public static void main(String[] args) {
		KfConstant.init();
		new PdfReportTextReader().readPdfLinkInEsByNoticId(2624467);
	}
}
