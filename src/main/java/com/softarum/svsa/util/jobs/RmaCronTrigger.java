package com.softarum.svsa.util.jobs;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import lombok.extern.log4j.Log4j;

@Log4j
public class RmaCronTrigger {

	public static void start() throws Exception {

		final JobKey jobKey = new JobKey("RmaJobKeyName", "group1");
		final JobDetail job = JobBuilder.newJob(RmaJobTask.class).withIdentity(jobKey).build();

		try {
			log.info("RmaTriggerName - configurando o scheduler...");
			
			/* For linux: configura o cron para rodar dia 10 de cada mes as 12h. 
			 * Se 10 for sabado roda na sexta e se for domingo roda na segunda.
			 */
			
			/* 
			final Trigger trigger = TriggerBuilder.newTrigger().withIdentity("RmaTriggerName", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 8 10W * * ?")).build();  */
			
			/* for windows */
			final Trigger trigger = TriggerBuilder.newTrigger().withIdentity("RmaTriggerName", "group1")
					.withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(18, 16, 15)).build();
			
			/* Valores Cron :
			    1 2 3 4 5  
			 
			    1: Minute (0-59)
				2: Hours (0-23)
				3: Day (0-31)
				4: Month (0-12 [12 == December])
				5: Day of the week(0-7 [7 or 0 == sunday])
				
				01 * * * * root run-parts /etc/cron.hourly
				02 4 * * * root run-parts /etc/cron.daily
				22 4 * * 0 root run-parts /etc/cron.weekly
				42 4 1 * * root run-parts /etc/cron.monthly
			 */
	
			final Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
		} catch(Exception e) {
			e.printStackTrace();
			log.error("Erro iniciando o cron trigger!");
		}
	}
}