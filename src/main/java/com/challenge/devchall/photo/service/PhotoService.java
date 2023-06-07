package com.challenge.devchall.photo.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.challenge.devchall.base.config.S3Config;
import com.challenge.devchall.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class PhotoService {

    final String endPoint = S3Config.getEndPoint();
    final String regionName = S3Config.getRegion();
    final String accessKey = S3Config.getAccessKey();
    final String secretKey = S3Config.getSecretKey();

    // S3 client
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();

    String bucketName = S3Config.getBucket();

    // upload local file
    @Transactional
    public String photoUpload (MultipartFile file) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "devchall_img/" + storeFileName;

        InputStream inputStream = file.getInputStream();

        PutObjectResult putObjectResult = s3.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return String.format("https://kr.object.ncloudstorage.com/%s/%s", bucketName, key);
    }

}


