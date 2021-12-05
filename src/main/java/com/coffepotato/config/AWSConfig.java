package com.coffepotato.config;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.coffepotato.common.component.S3Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
public class AWSConfig {

    @Autowired
    private Environment env;

    @Resource
    private S3Component s3Component;

    private final String prefix = "cloud.aws.";

    // 키,Secret 문자열을 받아서 설정하는 BasicAWSCredentials
    //보안상 취약.
    /*
    @Bean(name = "S3Client")
    public AmazonS3 s3Services(){

        String access = env.getProperty(prefix+"credentials.access-key");
        String secret = env.getProperty(prefix+"credentials.secret-key");
        String region  = env.getProperty(prefix+"region.static");

        AWSCredentials credentials = new BasicAWSCredentials(access, secret);

        AmazonS3 s3Client ;
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        return  s3Client;
    }
    */

    //Instance의 Credentia configure 파일을 미리 설정 후 이용.
    // 보안상 우위
     /*
    @Bean(name ="S3Client" )
    public AmazonS3 setS3Services() {

        String region  = env.getProperty(prefix+"region.static");

        ProfileCredentialsProvider provider = new ProfileCredentialsProvider();

        AmazonS3 s3Client ;
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(provider)
                .withRegion(region)
                .build();

        return  s3Client;
    }
    */


    @Bean(name = "S3Client")
    public S3Client s3Services(){
        AmazonS3 client ;
        String region  = env.getProperty(prefix+"region.static");

        client = AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(region)
                .build();

        return  new S3Client(client);
    }



    @Bean(name = "S3Bucket")
    public Map<String,String> setS3Bucket(){
        Map<String,String> s3Buckets = new HashMap<>();
        s3Component.getBuckets().forEach(b -> s3Buckets.put(b.get("name").toString(),b.get("path").toString()));
        return s3Buckets;
    }

}
