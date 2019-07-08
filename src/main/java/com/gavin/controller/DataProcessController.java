package com.gavin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavin.service.DataProcessService;

@RestController
@RequestMapping("dp")
public class DataProcessController {
	
	@Autowired
	private DataProcessService dataProcessService;
	
	@GetMapping("add")
	public String addData(){
		try {
			dataProcessService.insertData();
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
}
