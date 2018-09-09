package com.practice.config;

import com.practice.entity.User;
import com.practice.listner.JobListner;
import com.practice.processor.FileProcessor;
import com.practice.reader.FileReader;
import com.practice.writer.FileWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.List;

/**
 * Created by psingh15 on 5/16/17.
 */
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@Slf4j
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * transactionManager.
     * @return transactionManager
     */

    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }
    @Bean
    public JavaMailSenderImpl sender() {
        return new JavaMailSenderImpl();
    }

    /**
     * jobRepository.
     * @param transactionManager transactionManager
     * @return jobRepository
     * @throws Exception excpetion
     */
    @Bean
    public JobRepository jobRepository(ResourcelessTransactionManager transactionManager)
            throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean =
                new MapJobRepositoryFactoryBean(transactionManager);
        mapJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        return mapJobRepositoryFactoryBean.getObject();
    }

    /**
     * jobLauncher.
     * @param jobRepository jobRepository
     * @return jobLauncher.
     */
    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;
    }

    /**
     * clearancePriceJob.
     * @return clearancePriceJob
     */
    @Bean
    public Job userJob() {
        return jobBuilderFactory.get("userJob")
                .start(slaveStep())
                .listener(listener()).preventRestart()
                .build();
    }

    /**
     * @return Step .
     */
    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaveStep")
                .<String,
                        List<User>>chunk(10000)
                .reader(fileReader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public FileReader fileReader() {
        FileReader fileReader = new FileReader();
        return fileReader;
    }

    /**
     * Processor for TomDataload
     */
    @Bean
    @StepScope
    public ItemProcessor<String, List<User>> processor() {
        FileProcessor processor = new FileProcessor();
        return processor;
    }

    @Bean
    @StepScope
    public ItemWriter<List<User>> writer() {
        FileWriter fileWriter = new FileWriter();
        return fileWriter;
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobListner();
    }
}
