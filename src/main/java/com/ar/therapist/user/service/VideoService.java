package com.ar.therapist.user.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class VideoService {

	
	@Autowired
	private ResourceLoader resourceLoader;

	private static final String FORMAT = "classpath:videos/%s";
	
	 public InputStreamResource streamVideo(String videoName) throws IOException {
		 System.err.println("=========================VSSS");
	    //File videoFile = new File("path/to/your/video.mp4");
	    File videoFile = resourceLoader.getResource(String.format(FORMAT, videoName)).getFile();
	    FileInputStream fileInputStream = new FileInputStream(videoFile);
	    InputStreamResource resource = new InputStreamResource(fileInputStream);
	    return resource;
	}
	
	 
	public Mono<Resource> getVideo(String title){
		System.err.println(String.format(FORMAT, title));
		return Mono.fromSupplier(() -> resourceLoader.getResource(String.format(FORMAT, title)));
	}
	
	public File getFile(String fileName) throws IOException {
		File videoFile = resourceLoader.getResource(String.format(FORMAT, fileName)).getFile();
		return videoFile;
	}
}
