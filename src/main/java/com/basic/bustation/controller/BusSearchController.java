package com.basic.bustation.controller;

import com.basic.bustation.model.Roadline;
import com.basic.bustation.model.Roadstation;
import com.basic.bustation.model.Stationtoline;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp-pc on 2021/1/28.
 */

@Controller
@Transactional(propagation= Propagation.REQUIRED)
public class BusSearchController extends BaseController{

    //定义两个Gson过滤器用来过滤Gson中的字段或者类
    private static ExclusionStrategy exclusionStrategy=new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            String name=f.getName();
            return  name.contains("roadstationByEndid")|name.contains("roadstationByStartid")
                    |name.contains("stationtolines");
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            String name=aClass.getName();
            return name.contains("LineString");
        }
    };

    private static ExclusionStrategy datagridexclusionStrategy=new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            String name=f.getName();
            return  name.contains("linestations")|name.contains("stationtolines");
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            String name=aClass.getName();
            return name.contains("LineString");
        }
    };

    public Roadstation findRoadStation(String stationName){
        String hql="from Roadstation where name like ?";
        String param="%"+stationName+"%";
        return roadstationDAO.get(hql,param);
    }

    public Roadline findRoadline(String roadlineName){
        String hql="from Roadline where name like ?";
        String param="%"+roadlineName+"%";
        return roadlineDAO.get(hql,param);
    }


    @RequestMapping(value = "/roadsearch.action",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String BusSearch(@RequestParam String startstation,@RequestParam String endstation,@RequestParam Integer type){
        Map<String,Object> map= busSearchUtil.findBusSearch(startstation,endstation,type);
        gson=new Gson();
        return gson.toJson(map);
    }

    @RequestMapping(value = "/roadlineSearch.action",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String RoadlineSearch(@RequestParam String content,@RequestParam Integer type){
        Map<String,Object> map = new HashMap<>();
        gson=new GsonBuilder().setExclusionStrategies(exclusionStrategy).create();
        if(type==0){
            List<Roadline> roadlineList= new ArrayList<>();
            Roadline roadline = findRoadline(content);
            roadlineList.add(roadline);
            map.put("roadline",roadline);
            map.put("linestring",roadUtil.getLineStringPoints(roadline));
        }
        else if(type==1){
            Roadstation roadstation=findRoadStation(content);
            List<Stationtoline> stationtolines = stationtolineDAO.findBystation(roadstation);
            List<Roadline> roadlineList = new ArrayList<>();
            for(Stationtoline stationtoline:stationtolines){
                Roadline roadline = stationtoline.getRoadline();
                roadlineList.add(roadline);
            }
            map.put("roadstation",roadstation);
            map.put("rows",roadlineList);
            map.put("linestring",roadUtil.getLineStringPointsFromRoadList(roadlineList));
        }
        map.put("type",type);
        map.put("status","success");
        return gson.toJson(map);
    }

    @RequestMapping(value = "/findroadline.action",produces = "applicaion/json;charset=UTF-8")
    @ResponseBody
    public String findRoadline(@RequestParam String lastRoadstation,@RequestParam String nextRoadstation){
        List<Stationtoline> lastLineList=stationtolineDAO.findBystation(busSearchUtil.findRoadStation(lastRoadstation));
        List<Stationtoline> nextLineList=stationtolineDAO.findBystation(busSearchUtil.findRoadStation(nextRoadstation));
        Map<String, Object> map = new HashMap<>();
        gson=new Gson();
        for(Stationtoline l:lastLineList){
            for(Stationtoline n:nextLineList){
                if(l==n) {
                    map.put("changeLine",false);
                    return gson.toJson(map);
                }
            }
        }
        map.put("chaneLine",true);
        return gson.toJson(map);
    }
}
