package com.gavin.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.entity.Person;
import com.gavin.entity.User;
import com.gavin.mapper.master.UserMapper;
import com.gavin.mapper.slave.PersonMapper;
import com.gavin.service.DataProcessService;

@Service
public class DataProcessServiceImpl implements DataProcessService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PersonMapper personMapper;
	
	@Override
	public void insertData() throws Exception {
		// 创建user
		User user = new User();
		user.setUserName("张萌萌");
		user.setPassword("123456");
		
		// 创建person
		Person person = new Person();
		person.setAge(23);
		person.setPersonName("李依依");
		
		// 保存user
		int userFlag = userMapper.insert(user);
		int personFlag = personMapper.insert(person);
		
		System.out.println("保存user标识:" + userFlag);
		System.out.println("保存person标识:" + personFlag);
	}
	
	
}
