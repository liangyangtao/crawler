package com.kf.data.approved;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.kf.data.approved.quartz.ParserJob;
import com.kf.data.fetcher.tools.KfConstant;

/****
 * 
 * @Title: ApprovedApp.java
 * @Package com.kf.data.approved
 * @Description: 准挂牌公司
 * @author liangyt
 * @date 2017年12月7日 下午4:51:18
 * @version V1.0
 */
public class ApprovedApp {

	public static void main(String[] args) {
		try {
			KfConstant.init();
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("1/5 * * * * ?");
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
					.withSchedule(scheduleBuilder).build();
			JobDetail job = JobBuilder.newJob(ParserJob.class).withIdentity("job1", "group1").build();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
