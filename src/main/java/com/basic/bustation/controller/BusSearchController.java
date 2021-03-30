package com.basic.bustation.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by hp-pc on 2021/1/28.
 */

@Controller
@Transactional(propagation= Propagation.REQUIRED)
public class BusSearchController extends BaseController{

    @RequestMapping(value = "/roadsearch.action",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String BusSearch(@RequestParam String startstation,@RequestParam String endstation,@RequestParam Integer type){
       Map<String,Object> map= busSearchUtil.findBusSearch(startstation,endstation,type);
        gson=new Gson();
        return gson.toJson(map);
    }
}
