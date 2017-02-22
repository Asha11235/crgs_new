package controllers;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import models.FlatData;
import models.Form;
import models.FormResource;
import models.OptionsInForm;
import models.UnitData;

public class WebEntry extends Controller{

	public static void webFormEntry(Long id){
		
		id = (long) 1;
		Form form = Form.findById(id);
		
		List<FormResource> formResources = FormResource.find("byForm", form).fetch();
		List<OptionsInForm>optionsList = OptionsInForm.find("byForm", form).fetch();
		
		List<FlatData> flatDatas = new ArrayList<FlatData>();
		
		for(FormResource formResource: formResources){
			
			FlatData flatData = new FlatData();
			flatData.titleVar = formResource.nodePath;
			flatData.title = formResource.title;
			flatData.value = "";
			flatData.valueVar = "";
			flatData.type = formResource.type;
			flatData.formResource = formResource;
			flatDatas.add(flatData);
		}
		
		Logger.info("size  "+flatDatas.size());
		render(form, flatDatas, optionsList);
	}
}
