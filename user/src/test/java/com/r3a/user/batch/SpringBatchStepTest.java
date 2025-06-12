package com.r3a.user.batch;

import com.r3a.user.entity.UserEntity;
import com.r3a.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;


//@SpringBatchTest
@Import({FirstBatch.class, PartitionBatch.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpringBatchStepTest {

//    @Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    FirstBatch firstBatch; // reader, writer, processor 등 접근용

    @Autowired
    PartitionBatch partitionBatch;

    @Autowired
    JobLauncher jobLauncher;

    ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(10);
        exec.setMaxPoolSize(10);
        exec.setThreadNamePrefix("multi_thread_pool_job");
        exec.initialize();
        return exec;
    }
    @Test
    void testMultiThreadBatch() throws Exception {
        runBatchJob("multi");
    }
    @Test
    void testSingleThreadBatch() throws Exception {
        runBatchJob("single");
    }
    @Test
    void partitionBatch() throws Exception {
        Step step = partitionBatch.partitionerStep(); // 마스터 스텝

        Job job = new JobBuilder("partitionTestJob", jobRepository)
                .start(step)
                .build();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);

        System.out.println(">>> PARTITION JOB STATUS: " + execution.getStatus());
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }


    private void runBatchJob(String mode) throws Exception {
        StepBuilder stepBuilder = new StepBuilder("testStep_" + mode, jobRepository);

        Step step = stepBuilder
                .<UserEntity, UserEntity>chunk(10, platformTransactionManager)
                .reader(firstBatch.userReader())
                .processor(firstBatch.middleProcessor())
                .writer(firstBatch.afterWriter())
                .taskExecutor("multi".equals(mode) ? taskExecutor() : null)
                .build();

        Job job = new JobBuilder("testJob_" + mode, jobRepository)
                .start(step)
                .build();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters();
        JobExecution execution = jobLauncher.run(job, jobParameters);

        System.out.println(">>> " + mode.toUpperCase() + " THREAD JOB STATUS: " + execution.getStatus());
    }
}