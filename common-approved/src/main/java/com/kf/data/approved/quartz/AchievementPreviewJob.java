package com.kf.data.approved.quartz;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.kf.data.approved.parser.AchievementPreviewParser;
import com.kf.data.approved.store.PdfReportLinksReader;
import com.kf.data.fetcher.Fetcher;
import com.kf.data.mybatis.entity.PdfReportLinks;

/***
 * 8
 * 
 * @Title: AchievementPreviewJob.java
 * @Package com.kf.data.approved.quartz
 * @Description: 业绩预告
 * @author liangyt
 * @date 2017年12月15日 下午2:45:04
 * @version V1.0
 */
@DisallowConcurrentExecution
public class AchievementPreviewJob extends BaseJob implements Job {

	PdfReportLinksReader pdfReportLinksReader = new PdfReportLinksReader();

	AchievementPreviewParser achievementPreviewParser = new AchievementPreviewParser();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			doparser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doparser() {
		List<PdfReportLinks> pdfReportLinks = pdfReportLinksReader.readPdfLinkByRank(0, "业绩预告");
		for (PdfReportLinks pdfReportLink : pdfReportLinks) {
			try {
				if (pdfReportLink.getTitle().contains("半年")) {
					continue;
				}
				if (pdfReportLink.getTitle().contains("月")) {
					continue;
				}
				String html = null;
				String chagelink = changeHanzi(pdfReportLink.getLink());
				if (chagelink.endsWith("/")) {
				} else {
					html = Fetcher.getInstance().get(chagelink, "gbk");
					if (html == null || html.contains("MirrorFailed") || html.contains("NoSuchKey")) {
					} else {
						achievementPreviewParser.parserContent(html, pdfReportLink);
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
