package controllers;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import play.data.Upload;
import play.data.parsing.TempFilePlugin;
import play.exceptions.UnexpectedException;
import play.libs.Files;
import play.libs.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//@With(Deadbolt.class)

public class ResourceManagement extends Controller{

	   FileItem fileItem;
	    File defaultFile;
	    
	    public static void resourceList(){
	    	
	    	render();
	    }

	  
}
