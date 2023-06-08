package com.challenge.devchall.base.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class S3Config {

    @Getter
    private static String accessKey;

    @Value("${cloud.aws.credentials.access-key}")
    public void setAccessKey(String accessKey) {
        S3Config.accessKey = accessKey;
    }

    @Getter
    private static String secretKey;

    @Value("${cloud.aws.credentials.secret-key}")
    public void setSecretKey(String secretKey){
        S3Config.secretKey = secretKey;
    }

    @Getter
    private static String region;

    @Value("${cloud.aws.region.static}")
    public void setRegion(String region){
        S3Config.region = region;
    }

    @Getter
    private static String endPoint;

    @Value("${cloud.aws.s3.endpoint}")
    public void setEndPoint(String endPoint){
        S3Config.endPoint = endPoint;
    }

    @Getter
    private static String bucket;

    @Value("${cloud.aws.s3.bucket}")
    public void setBucket(String bucket){
        S3Config.bucket = bucket;
    }

    public static AmazonS3 amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey,secretKey);
        return (AmazonS3) AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .build();
    }

}