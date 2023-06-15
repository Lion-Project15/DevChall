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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private HttpServletRequest request;

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

    public String getCategoryPhoto(String categoryName){

        StringBuilder sb = new StringBuilder();
        String protocol = request.getScheme();

        if(protocol.equals("http")){
            sb.append("http://iztyfajjvmsf17707682.cdn.ntruss.com/category/");
        }else if(protocol.equals("https")){
            sb.append("https://iztyfajjvmsf17707682.cdn.ntruss.com/category/");
        }

        sb.append(categoryName);
        sb.append("?type=m&w=80&h=80&quality=90&bgcolor=121212&ttype=png&extopt=0&anilimit=1");

        return sb.toString();
    }

    public String getStoreCharacterPhoto(String characterName){

        StringBuilder sb = new StringBuilder();
        String protocol = request.getScheme();

        if(protocol.equals("http")){
            sb.append("http://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/");
        }else if(protocol.equals("https")){
            sb.append("https://iztyfajjvmsf17707682.cdn.ntruss.com/store_img/");
        }

        sb.append(characterName);
        sb.append("?type=m&w=120&h=120&bgcolor=EBEBEB");

        return sb.toString();
    }

    public String getLargePhoto(Photo photo){

        String photoUrl = photo.getPhotoUrl();

        String[] split = photoUrl.split("devchall/");

        StringBuilder sb = new StringBuilder();
        String protocol = request.getScheme();

        if(protocol.equals("http")){
            sb.append("http://iztyfajjvmsf17707682.cdn.ntruss.com/");
        }else if(protocol.equals("https")){
            sb.append("https://iztyfajjvmsf17707682.cdn.ntruss.com/");
        }

        sb.append(split[1]);
        sb.append("?type=m&w=700&h=400&quality=90&bgcolor=FFFFFF&extopt=3");

        return sb.toString();
    }

    public String getSmallPhoto(Photo photo){

        String photoUrl = photo.getPhotoUrl();
        String[] split = photoUrl.split("devchall/");

        StringBuilder sb = new StringBuilder();
        String protocol = request.getScheme();

        if(protocol.equals("http")){
            sb.append("http://iztyfajjvmsf17707682.cdn.ntruss.com/");
        }else if(protocol.equals("https")){
            sb.append("https://iztyfajjvmsf17707682.cdn.ntruss.com/");
        }

        sb.append(split[1]);
        sb.append("?type=m&w=200&h=115&quality=90&bgcolor=FFFFFF&extopt=0&anilimit=1");

        return sb.toString();
    }

    public String getPhotoUrl(MultipartFile file) throws IOException {

        RsData<String> fileRsData = isImgFile(file.getOriginalFilename());

        if (fileRsData.isFail()) {
            return fileRsData.getResultCode();
        } else if (fileRsData.getResultCode().equals("S-7")) {

            return "https://kr.object.ncloudstorage.com/devchall/devchall_img/example1.png";

        } else if (fileRsData.getResultCode().equals("S-6")) {

            //이미지가 있는 경우 이미지 리사이징, 경로 할당
            return photoUpload(file);
        }

        return fileRsData.getResultCode();
    }

}


