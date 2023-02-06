//package com.cf.service;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.springframework.web.multipart.MultipartFile;
//
//public interface IFirebaseService {
//
//	 String uploadFile(File file, String fileName) throws IOException; // used to upload a file
//	 File convertToFile(MultipartFile multipartFile, String fileName) throws IOException; // used to convert MultipartFile to File
//	 String getExtension(String fileName);  // used to get extension of a uploaded file
//	 Object upload(MultipartFile multipartFile,String candidateEmail);
//	 Object download(String fileName) throws IOException;
//
//}