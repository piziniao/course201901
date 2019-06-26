package com.mashibing.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.mashibing.springboot.entity.Account;
import com.mashibing.springboot.mapper.AccountExample;
import com.mashibing.springboot.mapper.AccountMapper;

@Service
public class AccountService {

	@Autowired
	AccountMapper accMapper;
	
	public Account findByLoginNameAndPassword(String loginName, String password) {

		AccountExample example = new AccountExample();
		example.createCriteria()
		.andLoginNameEqualTo(loginName)
		.andPasswordEqualTo(password);
		
		// password
		// 1. 没有 
		// 2. 有一条 
		// 3. 好几条 X
		List<Account> list = accMapper.selectByExample(example );
		
		return list.size() == 0? null:list.get(0);
	}

	public List<Account> findAll() {

		AccountExample example = new AccountExample();
		return accMapper.selectByExample(example );
	}

	public List<Account> findByPage(int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		
		AccountExample example = new AccountExample();
		return accMapper.selectByExample(example );
	}

}
