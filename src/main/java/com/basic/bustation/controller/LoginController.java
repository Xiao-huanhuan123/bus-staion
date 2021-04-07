package com.basic.bustation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController extends BaseController{


    @RequestMapping(value="/v1/login")
    @ResponseBody
    public String login(@RequestParam("userAccount") String username, @RequestParam("userPassword") String password,Map<String,Object> map, HttpSession session){

        //验证用户名和密码，输入正确，跳转到dashboard
        if(!StringUtils.isEmpty(username)&&"123456".equals(password)){

            session.setAttribute("userName",username);
            System.out.println("----" + username);
//            map.put("age",30);
            map.put("success",true);
            return gson.toJson(map);
//            return "redirect:/";

        }
        else  //输入错误，清空session，提示用户名密码错误
        {
            session.invalidate();
//            map.put("msg","用户名密码错误");
            return "login";
        }
    }


    @RequestMapping("dashboard")
    public String goMain(Map<String,Object> map)
    {
        map.put("name","zhangfang");
        map.put("age",28);
        map.put("sex","女");
        return "dashboard";
    }
}

