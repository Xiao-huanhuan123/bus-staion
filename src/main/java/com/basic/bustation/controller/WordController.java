package com.basic.bustation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.basic.bustation.model.Word;
import com.basic.bustation.util.CommonTools;
import com.basic.bustation.util.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WordController extends BaseController{

    @RequestMapping(value = "/getWords",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Response getWords(){
        List<Word> words = wordDAO.findAll();
        if(words.size()>0)
            return new Response("0", JSONArray.toJSONString(words));
        else
            return new Response("0", "没有留言");
    }

    @RequestMapping(value = "/leaveWord",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Response leaveWord(@RequestParam("title")String title,
                              @RequestParam("content")String content){
        if(CommonTools.isEmpty(title))
            return new Response("-1","标题不能为空");
        if(CommonTools.isEmpty(content))
            return new Response("-1","内容不能为空");
        Word word = new Word();
        word.setTitle(title);
        word.setContent(content);
        word.setLeaveTime(CommonTools.getCurrentTime());
        wordDAO.save(word);
        return new Response("0", JSON.toJSONString(word));
    }
}
