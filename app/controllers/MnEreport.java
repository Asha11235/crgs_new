package controllers;

import java.util.List;

import models.Data;

public class MnEreport extends Controller{
	
	public static void index() {

		List<Data> data = Data.findAll();
		int len = data.size();
		render(data, len);
	}

}
