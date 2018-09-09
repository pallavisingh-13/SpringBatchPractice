package com.practice.listner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * Created by psingh15 on 5/20/17.
 */
@Slf4j
public class JobListner implements JobExecutionListener {
    @Autowired
    EmailNotification emailNotification;

    private StopWatch watch;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        watch = new StopWatch();
        watch.start();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        watch.stop();
        log.info("update Job stoped :");
        log.info("job Total time take in millis :" + watch.getTotalTimeMillis());

        emailNotification.sendMail();
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("job completed successfully");
            jobExecution.setExitStatus(ExitStatus.COMPLETED);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("job failed with following exceptions ");
            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            for (Throwable th : exceptionList) {
                log.info("job exception :" + th.getLocalizedMessage());
            }
        }
    }
}
