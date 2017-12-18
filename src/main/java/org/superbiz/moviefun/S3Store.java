package org.superbiz.moviefun;

import com.amazonaws.services.apigateway.model.Op;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by aw169 on 12/14/17.
 */
public class S3Store implements BlobStore{

    private AmazonS3Client client;
    private  String bucketName;
    Bucket bucket;

    public S3Store(AmazonS3Client s3Client, String s3BucketName) {
        this.client = s3Client;
        this.bucketName = s3BucketName;

    }

    @Override
    public void put(Blob blob) throws IOException {
        PutObjectResult result = client.putObject(new PutObjectRequest(this.bucketName, blob.name, blob.inputStream, null));
        System.out.println("Put Object metadata : " + result.getMetadata());
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        System.out.println("Get Object for buket : " + this.bucketName + " and fileName : " + name);
        if(!client.doesObjectExist(this.bucketName,name)){
            return Optional.empty();
        }
       S3Object s3Object =  client.getObject(this.bucketName, name);
       Blob blob = new Blob(name, s3Object.getObjectContent(), s3Object.getObjectMetadata().getContentType());
       return Optional.of(blob);
    }

    @Override
    public void deleteAll() {

    }

    /*@Override
    public void afterPropertiesSet() throws Exception {
        if(!client.doesBucketExist(this.bucketName)){
           bucket =  client.createBucket(this.bucketName);
        }
    }*/
}
