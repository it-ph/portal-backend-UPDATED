package com.personiv.portal.service;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileService {
	public void uploadFile(MultipartFile file,String path) throws IOException {
		 Set<PosixFilePermission> perms = new HashSet<>();
		    perms.add(PosixFilePermission.OWNER_READ);
		    perms.add(PosixFilePermission.OWNER_WRITE);
		    perms.add(PosixFilePermission.OWNER_EXECUTE);

		   
		    perms.add(PosixFilePermission.GROUP_READ);
		    perms.add(PosixFilePermission.GROUP_WRITE);
		    perms.add(PosixFilePermission.GROUP_EXECUTE);
		    
		    File fileDir = new File(path);
		    
		    Path loc = Paths.get(fileDir.toURI());
		    
		    if(!fileDir.exists()) {
		    	
		    	fileDir.mkdirs();
		    	 try {
			        	Files.setPosixFilePermissions(fileDir.toPath(), perms);
		        }catch(UnsupportedOperationException e) {
		        	//do nothing in case this is not supported i.e Windows OS
		        }
		    }
		   
		    Files.copy(file.getInputStream(), loc.resolve(file.getOriginalFilename()));
		    
	}
	
	public void uploadFile(MultipartFile file,String path, String newname) throws IOException {
		 Set<PosixFilePermission> perms = new HashSet<>();
		    perms.add(PosixFilePermission.OWNER_READ);
		    perms.add(PosixFilePermission.OWNER_WRITE);
		    perms.add(PosixFilePermission.OWNER_EXECUTE);

		   
		    perms.add(PosixFilePermission.GROUP_READ);
		    perms.add(PosixFilePermission.GROUP_WRITE);
		    perms.add(PosixFilePermission.GROUP_EXECUTE);
		    
		    File fileDir = new File(path);
		    
		    Path loc = Paths.get(fileDir.toURI());
		    
		    if(!fileDir.exists()) {
		    	
		    	fileDir.mkdirs();
		    	 try {
			        	Files.setPosixFilePermissions(fileDir.toPath(), perms);
		        }catch(UnsupportedOperationException e) {
		        	//do nothing in case this is not supported i.e Windows OS
		        }
		    }
		   
		    Files.copy(file.getInputStream(), loc.resolve(newname));
		    
	}
   public Resource loadFile(String path) {
       try {
       	File file = new File(path);
       	
           Resource resource = new UrlResource(file.toURI());
           if(resource.exists() || resource.isReadable()) {
               return resource;
           }else{
           	throw new RuntimeException("FAIL!");
           }
       } catch (MalformedURLException e) {
       	e.printStackTrace();
       	throw new RuntimeException("FAIL! "+e.getMessage());
       }
   }
}