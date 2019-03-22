package com.poc.cron;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import com.poc.cron.jobs.Job1;
import com.poc.cron.jobs.Job2;
import com.poc.cron.jobs.Job3;

public class QuartzApp {
	
	public static void main(String[] args) {
		try {
			// ---------------------------------------------------------------
	        // create the job detail, based on the actual job class
			// ie. the class that will run at the scheduled time
			JobDetail job1 = JobBuilder.newJob(Job1.class)
	                .withIdentity("job1", "group1").build();
	
			// define the trigger for a group of jobs (here, group 1 will just include job1)
			// note this will include the timing info, supplied as a cron expression here
			// http://infocenter.sybase.com/help/index.jsp?topic=/com.sybase.infocenter.dc01963.0510/doc/html/psh1360346725201.html
			// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
			// https://stackoverflow.com/questions/41099397/use-day-of-month-and-day-of-week-both-in-cron-expression
			// https://crontab.guru/
	        Trigger trigger1 = TriggerBuilder.newTrigger()
	                .withIdentity("cronTrigger1", "group1")
	                .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * 9 * * ?"))   // "0/5 * * * * ?"
	                .build();
	        
	        // create a scheduler and start it running 
	        // then supply the job and trigger timing info to the scheduler to complete the configuration 
	        Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
	        scheduler1.start();
	        scheduler1.scheduleJob(job1, trigger1);
	        
	        // ---------------------------------------------------------------
	        JobDetail job2 = JobBuilder.newJob(Job2.class)
	                .withIdentity("job2", "group2").build();
	         
	        Trigger trigger2 = TriggerBuilder.newTrigger()
	                .withIdentity("cronTrigger2", "group2")
	                .withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("0/7 * * * * ?")))
	                .build();
	         
	        Scheduler scheduler2 = new StdSchedulerFactory().getScheduler();
	        scheduler2.start();
	        scheduler2.scheduleJob(job2, trigger2);
	
	        // ---------------------------------------------------------------
	        // consider how to stop the scheduler on the fly:
	        // --> keep a record of the scheduler: Map the group name V the Scheduler
	        // --> when told to change, get the scheduler and shut it down
	        // --> start a new scheduler with the existing JobDetail and an updated trigger for the adjusted time, then start and rescheduler the job 
	        JobDetail job3 = JobBuilder.newJob(Job3.class)
	                .withIdentity("job3", "group3").build();
	        
	        Trigger trigger3 = TriggerBuilder.newTrigger()
	                .withIdentity("cronTrigger3", "group3")
	                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(13, 46)) // harder to test!
	                .build();
	         
	        Scheduler scheduler3 = new StdSchedulerFactory().getScheduler();
	        scheduler3.start();
	        scheduler3.scheduleJob(job3, trigger3);
	        
	        // allow to run for 100 seconds, then shutdown all the schedulers
	        Thread.sleep(100000);
            scheduler1.shutdown();
            scheduler2.shutdown();
            scheduler3.shutdown();
	        
		} catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	
}
