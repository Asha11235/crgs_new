package controllers;

import java.io.File;
import play.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.log.Log;

import models.MediaArchive;
import models.User;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import play.data.FileUpload;
import play.data.validation.Valid;
import play.mvc.With;
import utils.CommonUtil;
import utils.NodeKeyVal;
@With(Deadbolt.class)
public class MediaArchives extends Controller {
	
	@ExternalRestrictions("File Archive")
	public static void fileUpload(){
		
		List<String> typeList = new ArrayList<String>();
		typeList.add("Audio");
		typeList.add("Video");
		typeList.add("Image");
		typeList.add("pdf");
		typeList.add("xlsx");
		typeList.add("Office");
		typeList.add("csv");
		typeList.add("Text");
		
		render(typeList);
	}
	
	public static void submit(@Valid MediaArchive media){
		
		
		File mediaFile = params.get("mediaFile", File.class);
		media.uploadedBy = User.findByName(controllers.Secure.Security.connected());
		 if(!request.params.get("mediaFile").isEmpty())
	 	    {
				FileUpload uploads = (FileUpload) request.params.get("mediaFile", FileUpload.class);
				if (uploads != null) {
					
					String fileName = CommonUtil.getExtension(uploads.getFileName());
					//String fileName = uploads.getFieldName();
					String path = "uploads" + File.separator;
					File file = new File(path + fileName);
					boolean result = true;
					System.console().printf("File extension " + fileName);
					/*try {
						result = Files.deleteIfExists(file.toPath());
						result = true;
					} catch (IOException e) {
						Logger.info("File Deleteing exception occured!");
						e.printStackTrace();
					}*/
					String na = "file"+System.currentTimeMillis() ;
					if (result == true)
						uploads.asFile(path + na+"."+fileName);
					
					 media.name = na+"."+fileName;
				}
				
	 	    }

		 media.save();
		 viewFileList();
	}
	@ExternalRestrictions("File Archive")
	public static void viewFileList(){
		List<MediaArchive> fileList = MediaArchive.find("order by id desc").fetch();
		JsonArray JSON_TABLE_DATA = new JsonArray();
		for(int i=0; i< fileList.size();i++)
		{		
			 	JsonObject g5= new JsonObject();
				g5.addProperty("id", fileList.get(i).id);
				g5.addProperty("Uploaded By", fileList.get(i).uploadedBy.name);
				g5.addProperty("File Name", fileList.get(i).name);
				g5.addProperty("Date", fileList.get(i).uploadDate);
				g5.addProperty("Download Link", "<a class='btn btn-mini btn-info' target = 'blank' href='/home/shakil/WorkspacePerforce/dev_java/crgs/uploads/"+fileList.get(i).name+"'> Download </a>");
				//g5.addProperty("view", "<a class='btn btn-mini btn-danger deleted' data-url='/BeneficiaryProfile/delete?id="+ben.get(i).id+"' href='#deleteModal'><i class='icon-remove icon-white'></i> Delete</a>"  +  "<a class='btn btn-mini btn-info' href='/BeneficiaryProfile/beneficiaryProfileEdit?id=" + ben.get(i).id +"'><i class='glyphicon glyphicon-edit'></i>" + "Edit" + "</a>");
				JSON_TABLE_DATA.add(g5);
		}
		String header="Media Archive List";
		render(fileList);
		
	}
	
	 public static void downloadFile(String fileName) throws IOException{
		 //java.util.loggin
		 Logger.info("URL -- > " + fileName);
			File facturaFile =  new File("/home/mpower/crgs_new_deployment/uploads/"+fileName);
			response.setHeader("Content-Disposition", "inline; filename=\"" + facturaFile.getName() + "\""); 
			response.setHeader("Content-Type", "application/x-download"); 
			renderBinary(facturaFile);
			
	    }    

}
