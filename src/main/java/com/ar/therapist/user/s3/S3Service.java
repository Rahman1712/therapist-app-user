package com.ar.therapist.user.s3;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class S3Service {

	private final S3Client s3;
	
	@Autowired
	public S3Service(S3Client s3) {
		this.s3 = s3;
	}
	
	public void putObject(String bucketName, String key, byte[] file, Map<String, String> metadata) {
		PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .metadata(metadata)
                .build();
		
		PutObjectResponse putObject = s3.putObject(objectRequest, RequestBody.fromBytes(file));
		System.err.println(putObject);
	}
	
	public byte[] getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();
		
		ResponseInputStream<GetObjectResponse> res = s3.getObject(getObjectRequest);
		
		try {
			return res.readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}

//https://therapists-app-bucket.s3.ap-south-1.amazonaws.com/SUN.jpg


/*
 chatgpt
 
 public String getObjectUrl(String bucketName, String key) {
	    S3Client s3 = S3Client.builder().build(); // Initialize your S3 client

	    // Create a GetObjectRequest to specify the bucket and key
	    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
	            .bucket(bucketName)
	            .key(key)
	            .build();

	    try {
	        // Use the S3 client to get the object's URL
	        ResponseBytes<GetObjectResponse> objectResponse = s3.getObjectAsBytes(getObjectRequest);
	        S3Object s3Object = objectResponse.response().s3Object();
	        
	        // The URL of the object is available in the S3Object's "url" property
	        return s3Object.url();
	    } catch (S3Exception e) {
	        System.err.println("Error getting the object URL: " + e.getMessage());
	        return null;
	    }
	}
 */
