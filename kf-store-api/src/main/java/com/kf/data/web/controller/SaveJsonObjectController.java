package com.kf.data.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kf.data.web.service.SaveJsonObjectService;

/***
 * 
 * @Title: AddJsonObjectController.java
 * @Package com.kf.data.web.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年6月27日 上午11:13:15
 * @version V1.0
 */
@Controller
@RequestMapping("/")
public class SaveJsonObjectController extends CommonController {

	@Autowired
	SaveJsonObjectService saveJsonObjectService;

	@RequestMapping(value = { "/saveJson" }, method = { RequestMethod.POST, RequestMethod.GET })
	public void reviceJson(String json, String type, HttpServletResponse response, HttpServletRequest request) {

		try {
			String token = request.getHeader("token");
			if (token != null && token.equals("kfsave")) {
				String obj = saveJsonObjectService.parserJson(json, type);
				responseJson(response, obj);
			} else {
				responseJson(response, "非法访问");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
