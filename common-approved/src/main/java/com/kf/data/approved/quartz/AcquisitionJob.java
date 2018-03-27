package com.kf.data.approved.quartz;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.kf.data.approved.parser.purchase.AcquisitionParser;
import com.kf.data.approved.store.PdfReportLinksReader;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.mybatis.entity.PdfReportLinks;

/****
 * 
 * @Title: AcquisitionJob.java
 * @Package com.kf.data.approved.quartz
 * @Description: 收购报告
 * @author liangyt
 * @date 2018年1月5日 下午6:34:52
 * @version V1.0
 */
@DisallowConcurrentExecution
public class AcquisitionJob extends BaseJob implements Job {

	PdfReportLinksReader pdfReportLinksReader = new PdfReportLinksReader();

	AcquisitionParser acquisitionParser = new AcquisitionParser();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			doparser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doparser() {
		List<PdfReportLinks> pdfReportLinks = pdfReportLinksReader.readPdfLinkByRank(0, "收购报告");
		for (PdfReportLinks pdfReportLink : pdfReportLinks) {
			try {
				String html = null;
				String chagelink = changeHanzi(pdfReportLink.getLink());
				if (chagelink.endsWith("/")) {
				} else {
					html = Fetcher.getInstance().get(chagelink, "gbk");
					if (html == null || html.contains("MirrorFailed") || html.contains("NoSuchKey")) {
					} else {
						acquisitionParser.parserAcquisition(html, pdfReportLink);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pdfReportLink.setRank(2);
				pdfReportLinksReader.updatePdfReportLinkByid(pdfReportLink);
			}

		}

	}

}
