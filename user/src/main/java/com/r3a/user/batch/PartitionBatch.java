package com.r3a.user.batch;

        import com.r3a.user.entity.UserEntity;
        import com.r3a.user.repository.UserRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.batch.core.Job;
        import org.springframework.batch.core.Step;
        import org.springframework.batch.core.StepExecution;
        import org.springframework.batch.core.StepExecutionListener;
        import org.springframework.batch.core.configuration.annotation.StepScope;
        import org.springframework.batch.core.job.builder.JobBuilder;
        import org.springframework.batch.core.partition.support.Partitioner;
        import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
        import org.springframework.batch.core.repository.JobRepository;
        import org.springframework.batch.core.step.builder.PartitionStepBuilder;
        import org.springframework.batch.core.step.builder.StepBuilder;
        import org.springframework.batch.item.ExecutionContext;
        import org.springframework.batch.item.ItemProcessor;
        import org.springframework.batch.item.data.RepositoryItemReader;
        import org.springframework.batch.item.data.RepositoryItemWriter;
        import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
        import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.core.task.TaskExecutor;
        import org.springframework.data.domain.Sort;
        import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
        import org.springframework.transaction.PlatformTransactionManager;

        import java.util.HashMap;
        import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class PartitionBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final UserRepository userRepository;

    private int poolSize;

    @Value("${poolSize:10}")
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    // 기존 작업 Step (workerStep)
    @Bean
    @StepScope
    public Step workerStep() {
        return new StepBuilder("workerStep", jobRepository)
                .<UserEntity, UserEntity>chunk(10, platformTransactionManager)
                .reader(userReader(null, null))  // 파티션별 파라미터 받음
                .processor(middleProcessor())
                .writer(afterWriter())
                .build();
    }

    // Partitioning용 Partitioner - id 기준 범위 나누기 예시
    @Bean
    public Partitioner userRangePartitioner() {

        return gridSize -> {
            Map<String, ExecutionContext> partitions = new HashMap<>();

            // userRepository를 이용해 전체 아이디 범위를 조회하거나,
            // 단순 예시로 1~1000 범위를 5개 파티션으로 나눈다고 가정
            int totalSize = 1000;
            int targetSize = totalSize / gridSize;

            for (int i = 0; i < gridSize; i++) {
                ExecutionContext context = new ExecutionContext();
                int start = i * targetSize + 1;
                int end = (i == gridSize - 1) ? totalSize : (start + targetSize - 1);

                context.putInt("minId", start);
                context.putInt("maxId", end);

                partitions.put("partition" + i, context);
            }

            return partitions;
        };
    }

    // 파티셔닝 마스터 Step 정의
    @Bean
    public Step partitionerStep() {


        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setTaskExecutor(taskExecutor());
        partitionHandler.setStep(workerStep());
        partitionHandler.setGridSize(poolSize);

        return new StepBuilder("partitionerStep", jobRepository)
                .partitioner(workerStep().getName(), userRangePartitioner())
                .partitionHandler(partitionHandler)
                .build();
    }

    @Bean
    public Job partitionJob() {

        return new JobBuilder("partitionJob", jobRepository)
                // 아래서 단일 Step 대신 파티셔닝 Step으로 변경
                .start(partitionerStep())
                .build();
    }

    private TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(poolSize);
        exec.setMaxPoolSize(poolSize);
        exec.setThreadNamePrefix("partition_thread_pool_");
        exec.initialize();
        return exec;
    }

    // StepScope 필수! 파티션별 ExecutionContext에서 파라미터 받아 처리하도록 수정
    @Bean
    @StepScope
    public RepositoryItemReader<UserEntity> userReader(
            @Value("#{stepExecutionContext['minId']}") Integer minId,
            @Value("#{stepExecutionContext['maxId']}") Integer maxId) {

        return new RepositoryItemReaderBuilder<UserEntity>()
                .name("userReader")
                .repository(userRepository)
                .methodName("findByIdBetween")
                .pageSize(10)
                .arguments(minId, maxId)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<UserEntity, UserEntity> middleProcessor() {

        return item -> {
            item.calcScore();
            item.resetTransactionCount();
            return item;
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
