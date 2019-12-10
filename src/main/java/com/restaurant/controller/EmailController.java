package com.restaurant.controller;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.restaurant.model.OrderInfo;
import com.restaurant.util.MyConstants;

@Controller
public class EmailController {

	@Autowired
	public JavaMailSender emailSender;

	@ResponseBody
//	@RequestMapping("/sendSimpleEmail")
	public void sendSimpleEmail(OrderInfo order) {

		// Create a Simple MailMessage.
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(MyConstants.FRIEND_EMAIL);
		message.setSubject("Japanese Restaurant");
		message.setText("Hello, this is the receive for your order:\n " + order.toString());

		// Send Message!
		this.emailSender.send(message);

//		return "Email Sent!";
	}

	@ResponseBody
//	@RequestMapping("/sendAttachmentEmail")
	public String sendAttachmentEmail() throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();

		boolean multipart = true;

		MimeMessageHelper helper = new MimeMessageHelper(message, multipart);

		helper.setTo(MyConstants.FRIEND_EMAIL);
		helper.setSubject("Test email with attachments");

		helper.setText("Hello, Im testing email with attachments!");

		String path1 = "/home/tran/Downloads/test.txt";
		String path2 = "/home/tran/Downloads/readme.zip";

		// Attachment 1
		FileSystemResource file1 = new FileSystemResource(new File(path1));
		helper.addAttachment("Txt file", file1);

		// Attachment 2
		FileSystemResource file2 = new FileSystemResource(new File(path2));
		helper.addAttachment("Readme", file2);

		emailSender.send(message);

		return "Email Sent!";
	}

	@ResponseBody
//	@RequestMapping("/sendHtmlEmail")
	public String sendHtmlEmail() throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();

		boolean multipart = true;

		MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

		String htmlMsg = "<h3>Im testing send a HTML email</h3>"
				+ "<img src='http://www.apache.org/images/asf_logo_wide.gif'>";

		message.setContent(htmlMsg, "text/html");

		helper.setTo(MyConstants.FRIEND_EMAIL);

		helper.setSubject("Test send HTML email");

		this.emailSender.send(message);

		return "Email Sent!";
	}
}
