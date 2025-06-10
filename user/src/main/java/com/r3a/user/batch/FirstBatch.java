package com.r3a.user.batch;

import com.r3a.user.entity.UserEntity;
import com.r3a.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@AllArgsConstructor
@Configuration
public class FirstBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final UserRepository userRepository;




    @Bean
    public Job firstJob() {

        System.out.println("first job");

        return new JobBuilder("firstJob", jobRepository)
                .start(firstStep())
                .build();
    }

    @Bean
    public Step firstStep() {

        System.out.println("first step");

        return new StepBuilder("firstStep", jobRepository)
                .<UserEntity, UserEntity> chunk(10, platformTransactionManager)
                .reader(userReader())
                .processor(middleProcessor())
                .writer(afterWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    private TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(10);
        exec.setMaxPoolSize(10);
        exec.setThreadNamePrefix("multi_thread_pool_job");
        exec.initialize();
        return exec;
    }

    @Bean
    public RepositoryItemReader<UserEntity> userReader() {

        return new RepositoryItemReaderBuilder<UserEntity>()
                .name("beforeReader")
                .pageSize(10)
                .methodName("findAll")
                .repository(userRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<UserEntity, UserEntity> middleProcessor() {

        return new ItemProcessor<UserEntity, UserEntity>() {

            @Override
            public UserEntity process(UserEntity item) throws Exception {
//                int cnt = item.getRatingScore().getTransactionCnt();
//                double ratingScore = item.getRatingScore().getScore();

                item.calcScore();
                item.resetTransactionCount();

                return item;
            }
        };
    }

    @Bean
    public RepositoryItemWriter<UserEntity> afterWriter() {

        return new RepositoryItemWriterBuilder<UserEntity>()
                .repository(userRepository)
                .methodName("save")
                .build();
    }
}
