package com.challenge.devchall.photo.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.challenge.devchall.base.config.S3Config;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.photo.entity.Photo;
import com.challenge.devchall.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

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



    public RsData<String> isImgFile(String fileName) {

        //확장자 추출
        String fileExtension = StringUtils.getFilenameExtension(fileName);

        if(fileExtension == null){
            return RsData.of("S-7", "없어도 생성이 가능합니다.");
        } else if (fileExtension.equals("jpg") || fileExtension.equals("jpeg")
                || fileExtension.equals("png") || fileExtension.equals("gif")){
            return RsData.of("S-6", "이미지 파일이 맞습니다.");
        } else{
            return RsData.of("F-6", "이미지만 업로드가 가능합니다.");
        }

    }

    public Photo createPhoto(String photoUrl){

        Photo photo = Photo.builder()
                .photoUrl(photoUrl)
                .build();

        photoRepository.save(photo);

        return photo;
    }

}


