package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import models.Ngo;
import models.ResourceManagement;
import models.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import play.Logger;
import play.Play;
import play.data.Upload;
import play.data.parsing.TempFilePlugin;
import play.data.validation.Error;
import play.exceptions.UnexpectedException;
import play.libs.Files;
import play.libs.IO;
import play.mvc.With;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@With(Deadbolt.class)
public class ResourceManagements extends Controller {
    static String rootPath = Play.configuration.getProperty("application.path");

    @ExternalRestrictions("View Resource")
    public static void resourcesList() {
        List<String> categoryList = new ArrayList<String>();
        categoryList.add("PDF");
        categoryList.add("Audio");
        categoryList.add("Video");

        List<ResourceManagement> resourceManagementList = ResourceManagement.findAll();

        render(categoryList, resourceManagementList);
    }

    public static void resources() {
        List<ResourceManagement> resourceManagementList = ResourceManagement.findAll();

        render(resourceManagementList);
    }

    public static void submit(@Valid ResourceManagement resourceManagement) throws NullPointerException {
       /* validation.required("Content Title should be unique", resourceManagement.contentTitle);
        validation.required("Version already exist", resourceManagement.resourceVersion);
        validation.required("Version already exist", resourceManagement.filePath);*/

        File fileName = params.get("newFile", File.class);
        File projectRoot = Play.applicationPath;
        //String path = String.valueOf(projectRoot) + "/public/resourceFiles/";
        String path = String.valueOf(projectRoot) + rootPath;
        if (fileName != null) {
            fileName.renameTo(new File(path + fileName.getName()));
            //resourceManagement.filePath = "/public/resourceFiles/" + fileName.getName();
            resourceManagement.filePath = fileName.getName();
            Logger.info("File path " + resourceManagement.filePath);
        } else {
            System.out.println("input file please");

        }

        User user = User.find("byName", session.get("username")).first();
        resourceManagement.update_by = user.displayName;

        if (validation.hasErrors()) {
            Logger.info("HI i am in validation ");
            flash.error("Record can't be saved");
            for (Error error : validation.errors()) {
                flash.error("" + error.getKey());
                break;
            }
            resourcesList();
        }

        validation.valid(resourceManagement);
        resourceManagement.save();
        flash.success("Record saved successfully");
        resourcesList();
    }

    @ExternalRestrictions("Download ResourceManagement")
    public static void saveFile(String fileName) {
        if (fileName != null) {
            Logger.info("URL -- > " + fileName);
            File projectRoot = Play.applicationPath;
            String path = String.valueOf(projectRoot);
            File facturaFile = new File(path + rootPath + fileName);
            response.setHeader("Content-Disposition", "inline; filename=\"" + facturaFile.getName() + "\"");
            response.setHeader("Content-Type", "application/x-download");
            renderBinary(facturaFile);
        } else {

        }
    }

    @ExternalRestrictions("Delete ResourceManagement")
    public static int delete(Long id) {
        session.remove("userId");
        Logger.info("resource id  is : " + id);
        int confirm = 0;
        if (request.isAjax()) {
            ResourceManagement resourceManagement = ResourceManagement.findById(id);
            notFoundIfNull(id, "id not provided");
            notFoundIfNull(resourceManagement, "user not found");
            try {
                resourceManagement.delete();

                //for deleteing file from folder :)
                File projectRoot = Play.applicationPath;
                String path = String.valueOf(projectRoot);
                File facturaFile = new File(path + rootPath + resourceManagement.filePath);
                new ResourceManagements().delete(new File(String.valueOf(facturaFile))); // Delete file single file
                confirm = 1;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return confirm;
    }


    private void delete(File file) {
        boolean success = false;
        if (file.isDirectory()) {
            for (File deleteMe : file.listFiles()) {
                // recursive delete
                delete(deleteMe);
            }
        }
        success = file.delete();
        if (success) {
            System.out.println(file.getAbsoluteFile() + " Deleted");
        } else {
            System.out.println(file.getAbsoluteFile() + " Deletion failed!!!");
        }
    }
}
