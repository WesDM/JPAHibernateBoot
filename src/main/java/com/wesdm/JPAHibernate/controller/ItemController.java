package com.wesdm.JPAHibernate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wesdm.JPAHibernate.dao.ItemDao;
import com.wesdm.JPAHibernate.model.Item;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	private ItemDao itemDao;
	
	@Autowired
	public ItemController(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	
	@RequestMapping(method = RequestMethod.GET, path="/all")
	public @ResponseBody List<Item> items(){
		return itemDao.findAll();
	}
}
