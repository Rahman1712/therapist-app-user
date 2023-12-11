package com.ar.therapist.user.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ar.therapist.user.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public String sendOTPMail(User user, String otp) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		
		String mailSubject = "Welcome to ZenithCare";
		String mailContent = 
				"""
				<!DOCTYPE html>
				<html>
				<head>
				  <link rel="preconnect" href="https://fonts.googleapis.com">
				  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
				  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700&display=swap" rel="stylesheet">

				  <style>
				    /* Add inline CSS styles for styling */
				    body {
				      /*font-family: 'Poppins', Arial, sans-serif;*/
				      font-family: 'Poppins', sans-serif;
				      background-color: #f7f7f7;
				      margin: 0;
				      padding: 0;
				    }
				    .container {
				      background-color: #ffffff;
				      border-radius: 10px;
				      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
				      margin: 20px auto;
				      max-width: 600px;
				      padding: 20px;
				      text-align: center;
				    }
				    .header {
				      background-color: #00a7e1;
				      border-radius: 10px 10px 0 0;
				      color: #ffffff;
				      padding: 20px;
				    }
				    .header h1 {
				      font-size: 28px;
				    }
				    .description {
				      font-size: 16px;
				      padding: 20px;
				      text-align: left;
				      background-color: #CCCCCC;
				    }
				    .footer {
				      background-color: #00a7e1;
				      border-radius: 0 0 10px 10px;
				      color: #ffffff;
				      padding: 20px;
				    }
				    .footer a {
				      color: #ffffff;
				      text-decoration: none;
				      margin: 0 10px;
				    }
				    .footer a img{
				      border-radius: 50px;
				      border: 1px solid #FFFFFF;
				      background: #FFFFFF;
				    }
				    .otp{
				      font-size: 25px;
				      font-weight: 600;
				    }
				  </style>
				  
				  
				</head>
				<body>
				  <div class="container">
				    <div class="header">
				      <img src='cid:logoImage' alt="ZenithCare Logo" width="150" />
				      <h1 style="color: #ffffff;">ZenithCare</h1>
				    </div>
				    <div class="description">
				      <p>
				      	Hello %s ,
				      </p>
				      <p>
				        Welcome to ZenithCare! To log in, use the OTP (One-Time Password): <span class="otp">%s</span> sent to your email. The OTP will expire after 5 minutes, so be sure to use it promptly.
				      </p>
				    </div>
				    <div class="footer">
				      <a href="https://facebook.com/zenithcare" target="_blank"><img src='cid:facebookIcon' alt="Facebook"></a>
				      <a href="https://twitter.com/zenithcare" target="_blank"><img src='cid:twitterIcon' alt="Twitter"></a>
				      <a href="https://linkedin.com/zenithcare" target="_blank"><img src='cid:linkedinIcon' alt="LinkedIn"></a>
				      <p>&copy; 2023 ZenithCare</p>
				    </div>
				  </div>
				</body>
				</html>
				""".formatted(user.getFullname(), otp);
		
		helper.setFrom("donrahman6@gmail.com", "ZenithCare Contact");
		helper.setTo(user.getEmail());
		helper.setSubject(mailSubject);
		helper.setText(mailContent, true);
		
		ClassPathResource logoImage = new ClassPathResource("/static/images/logo.png");
		helper.addInline("logoImage", logoImage);
		
		ClassPathResource facebookIcon = new ClassPathResource("/static/images/Facebook.png");
		helper.addInline("facebookIcon", facebookIcon);
		
		ClassPathResource twitterIcon = new ClassPathResource("/static/images/Twitter.png");
		helper.addInline("twitterIcon", twitterIcon);
		
		ClassPathResource linkedinIcon = new ClassPathResource("/static/images/LinkedIn.png");
		helper.addInline("linkedinIcon", linkedinIcon);
		
		ClassPathResource termsAndConditions = new ClassPathResource("/static/files/terms_and_conditions.txt");
		helper.addAttachment("Terms and Conditions", termsAndConditions);

		mailSender.send(message);
		
		return "OTP sended to ✉️ : "+user.getEmail();
	}

}
