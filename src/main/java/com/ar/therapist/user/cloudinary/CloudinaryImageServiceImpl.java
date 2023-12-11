package com.ar.therapist.user.cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Service
@SuppressWarnings("rawtypes")
public class CloudinaryImageServiceImpl implements CloudinaryImageService{
	
	@Autowired private Cloudinary cloudinary;

	@Override
	public Map upload(MultipartFile file) {
		try {
			Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
			return data;
		} catch (IOException e) {
			throw new RuntimeException("Image uploading fail !!");
		}
	}

}

/*
{
    "signature": "af9e3b7c695af02e6a4caafb88ca170ef28deec5",
    "format": "jpg",
    "resource_type": "image",
    "secure_url": "https://res.cloudinary.com/dsrwvrbp9/image/upload/v1699354825/oajo7cuyutzerbtcrdk9.jpg",
    "created_at": "2023-11-07T11:00:25Z",
    "asset_id": "e50bcfd54842cdcc052e09d693c55157",
    "version_id": "1d04e83a8fed59614869e2d917711344",
    "type": "upload",
    "version": 1699354825,
    "url": "http://res.cloudinary.com/dsrwvrbp9/image/upload/v1699354825/oajo7cuyutzerbtcrdk9.jpg",
    "public_id": "oajo7cuyutzerbtcrdk9",
    "tags": [],
    "folder": "",
    "original_filename": "file",
    "api_key": "392342135431296",
    "bytes": 9753,
    "width": 275,
    "etag": "7421adeac4d72118648186b84519f928",
    "placeholder": false,
    "height": 183
}
 */
