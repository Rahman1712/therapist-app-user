package com.ar.therapist.user.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.ar.therapist.user.service.VideoService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
	
     private final VideoService  videoService;

     @GetMapping("/stream/{videoName}")
	 public Mono<ResponseEntity<Resource>> streamVideo(ServerWebExchange exchange, @PathVariable("videoName") String videoName) throws IOException {
	    InputStreamResource resource = videoService.streamVideo(videoName);
	
	    return Mono.just(ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource));
	}
	
    
    @GetMapping(value = "/video-file/{title}", produces = "video/mp4")
 	public Mono<Resource> getVideos(@PathVariable String title, @RequestHeader("Range") String range){
 		System.out.println("range in bytes() : " + range);
 		return videoService.getVideo(title); 
 	}

    
    @GetMapping("/poli")
    public String getString(){
    	return "Its just string..."; 
    }
    
    @GetMapping("/polimono")
    public Mono<String> getStringMono(){
    	return Mono.just("Its just mono data"); 
    }
    
    @GetMapping("/videos/{videoName}")
    public ResponseEntity<FileSystemResource> streamVideo(@PathVariable String videoName) throws IOException {
        File videoFile = videoService.getFile(videoName);
    	
        if (videoFile.exists()) {
            return ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .body(new FileSystemResource(videoFile));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
