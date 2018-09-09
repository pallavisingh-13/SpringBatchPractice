package com.practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by psingh15 on 5/16/17.
 */
@Configuration
@ComponentScan("com.practice")
@SpringBootApplication
@EnableAutoConfiguration
@Slf4j
public class FileProcessorBatchJobApplication {
    /**
     * main.
     * @param args args
     */
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(FileProcessorBatchJobApplication.class);
        app.setWebEnvironment(false);
        ApplicationContext ctx = app.run(args);
        JobLauncher jobLauncher = ctx.getBean("jobLauncher",JobLauncher.class);
        Job clearancePriceJob = ctx.getBean("userJob", Job.class);
        // Job parameters
        Map<String, JobParameter> parameters = new HashMap<>();
        JobParameters jobParameters = new JobParameters(parameters);

        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(clearancePriceJob, jobParameters);
            BatchStatus batchStatus = jobExecution.getStatus();
            while (batchStatus.isRunning()) {
                log.info("*********** Still running.... **************");
                Thread.sleep(100);
            }
            log.info(String.format("***********  JOB Exit status: %s",
                    jobExecution.getExitStatus().getExitCode()));
            JobInstance jobInstance = jobExecution.getJobInstance();
            log.info(String.format("********* Name of the job %s", jobInstance.getJobName()));

            log.info(String.format("*********** job instance Id: %d", jobInstance.getId()));
        } catch (JobExecutionAlreadyRunningException jobExeAlreadyRunningExp) {
            log.error("******** JobExecutionAlreadyRunningException : "
                    + jobExeAlreadyRunningExp.getMessage());
        } catch (JobRestartException jobRestartExp) {
            log.error("******** JobRestartException : " + jobRestartExp.getMessage());
        } catch (JobInstanceAlreadyCompleteException jonInsAlreadyComExp) {
            log.error("******** JobInstanceAlreadyCompleteException : "
                    + jonInsAlreadyComExp.getMessage());
        } catch (JobParametersInvalidException jobParamsInvalidExp) {
            log.error("******** JobParametersInvalidException : "
                    + jobParamsInvalidExp.getMessage());
        } catch (InterruptedException inturruptedExp) {
            log.error("******** InterruptedException : " + inturruptedExp.getMessage());
        } finally {
            System.exit(0);
        }
        log.info(String.format("*********** JOB Exit status: %s",
                jobExecution.getExitStatus().getExitCode()));
        JobInstance jobInstance = jobExecution.getJobInstance();
        log.info(String.format("********* Name of the job %s", jobInstance.getJobName()));

        log.info(String.format("*********** job instance Id: %d", jobInstance.getId()));
        System.exit(0);
    }
}
