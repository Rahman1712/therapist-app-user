package com.ar.therapist.user.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3.buckets")
public class S3Buckets {

	private String therapist;

	public String getTherapist() {
		return therapist;
	}

	public void setTherapist(String therapist) {
		this.therapist = therapist;
	}



}
