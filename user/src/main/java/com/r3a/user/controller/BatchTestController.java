package com.r3a.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/api/v1/batch")
public class BatchTestController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;


    public BatchTestController(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @PostMapping("/firstjob")
    public ResponseEntity<?> firstJobTest(){
        try {
            System.out.println("first job start");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
            String date = dateFormat.format(new Date());

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("date", date)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);
            return ResponseEntity.ok("job 성공 : "+jobExecution.getStatus());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("job 실패 : "+ e.getMessage());
        }
    }
}
