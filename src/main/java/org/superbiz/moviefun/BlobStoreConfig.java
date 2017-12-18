package org.superbiz.moviefun;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by aw169 on 12/14/17.
 */
@Configuration
public class BlobStoreConfig {

     String s3EndpointUrl;
     String s3AccessKey;
     String s3SecretKey;
     String s3BucketName;

    public BlobStoreConfig( @Value("${s3.endpointUrl}") String s3EndpointUrl,
        @Value("${s3.accessKey}") String s3AccessKey,
        @Value("${s3.secretKey}") String s3SecretKey,
        @Value("${s3.bucketName}") String s3BucketName){

        System.out.println("S3 accesskey : " + s3AccessKey);
        System.out.println("S3 secret key : " + s3SecretKey);

        this.s3EndpointUrl = s3EndpointUrl;
        this.s3AccessKey = s3AccessKey;
        this.s3SecretKey = s3SecretKey;
        this.s3BucketName = s3BucketName;
    }

    @Bean
    public BlobStore blobStore() {
        AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        s3Client.setEndpoint(s3EndpointUrl);
        return new S3Store(s3Client, s3BucketName);
    }

}
