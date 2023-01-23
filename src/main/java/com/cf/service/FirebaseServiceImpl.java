package com.cf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseServiceImpl implements IFirebaseService{
	
	String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/<gs://interviewmanagement-fb358.appspot.com>/o/%s?alt=media";

	public String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("interviewmanagement-fb358.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("//home//tejasrinivas//Downloads//interviewmanagement-fb358-firebase-adminsdk-mtia4-effe91e719.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        System.out.println("downloadUrl"+String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
        
        System.out.println("upload File "+ fileName);
        
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        //System.out.println(DOWNLOAD_URL);
    }

    public File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    public Object upload(MultipartFile multipartFile) {
    	String TEMP_URL = null;

        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
//            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name. 

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();    
            System.out.println("service layer upload method " +fileName);
            System.out.println("temp url:"+TEMP_URL);
            // to delete the copy of uploaded file stored in the project folder
            //return sendResponse("Successfully Uploaded !", TEMP_URL);                     // Your customized response
            return TEMP_URL;
        } catch (Exception e) {
            e.printStackTrace();
           // return sendResponse("500", e, "Unsuccessfully Uploaded!");
            return "Unsuccessfully Uploaded!";
        }

    }
    
    public Object download(String fileName) throws IOException {
       // String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));     // to set random strinh for destination file name
        String destFilePath = "//home//tejasrinivas//Downloads//interview-management-resumes" + fileName;                                    // to set destination file path
        
        ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("//home//tejasrinivas//Downloads//interviewmanagement-fb358-firebase-adminsdk-mtia4-effe91e719.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of("interviewmanagement-fb358.appspot.com", fileName));
        blob.downloadTo(Paths.get(destFilePath));
       // return sendResponse("200", "Successfully Downloaded!");
        return "Successfully Downloaded!";
    }
    
    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("//home//tejasrinivas//Downloads//interviewmanagement-fb358-firebase-adminsdk-mtia4-effe91e719.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://interviewmanagement-fb358-default-rtdb.asia-southeast1.firebasedatabase.app.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

 
}