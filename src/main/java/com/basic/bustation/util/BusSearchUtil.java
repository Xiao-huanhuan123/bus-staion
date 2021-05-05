package com.basic.bustation.util;

import com.basic.bustation.dao.RoadsectionDAO;
import com.basic.bustation.dao.RoadstationDAO;
import com.basic.bustation.dao.RoadstationDAO;
import com.basic.bustation.dao.StationtolineDAO;
import com.basic.bustation.dijkstra.FloydInGraph;
import com.basic.bustation.model.Roadline;
import com.basic.bustation.model.Roadstation;
import com.basic.bustation.model.Stationtoline;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component("busSearchUtil")
public class BusSearchUtil {

    protected Gson gson = new Gson();

    @Autowired
    RoadstationDAO roadstationDAO;

    @Autowired
    RoadsectionDAO roadsectionDAO;

    @Autowired
    StationtolineDAO stationtolineDAO;

    @Autowired
    JsonUtil jsonUtil;

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

    /**
     *  公交搜索算法基于佛洛伊德最短路径算法
     * @param startStation  起始站点
     * @param endstartStation   结束站点
     * @param type     类型 0为 权值为时间  1为 权值为距离
     * @return
     */
    public Map<String,Object> findBusSearch(String startStation,String endstartStation,int type){
        gson=new GsonBuilder().setExclusionStrategies(exclusionStrategy).create();
        Map<String,Object> map=new HashMap<>();
        Roadstation startstation=findRoadStation(startStation);
        Roadstation endstation=findRoadStation(endstartStation);
        List<Roadstation> changeStation = new ArrayList<>();
        List<Roadline> changeLine = new ArrayList<>();
        if(startstation!=null&endstation!=null){
            //起始站点有数据
            FloydInGraph graph=new FloydInGraph();
            graph.initMap(roadsectionDAO.findAll(),roadstationDAO.findAll(),type);
            double weight=graph.findMIXDistance(startstation,endstation);
            List<Roadstation> roadstations =  graph.findelapsedStations();
            if(weight!=Double.MAX_VALUE){
                for(int i=1;i<roadstations.size()-1;i++){
                    Boolean hasChange=true;
                    Roadstation lastRoadstation = roadstations.get(i-1);
                    Roadstation nextRoadstation = roadstations.get(i+1);
                    Roadstation indexRoadstation = roadstations.get(i);
                    List<Stationtoline> lastLineList=stationtolineDAO.findBystation(lastRoadstation);
                    List<Stationtoline> nextLineList=stationtolineDAO.findBystation(nextRoadstation);
                    List<Stationtoline> indexLineList=stationtolineDAO.findBystation(indexRoadstation);
                    OK:for(Stationtoline l:lastLineList){
                        for(Stationtoline n:nextLineList){
                            //如果经过前一个站点和后一个站点的所有线路中有同样的线路则没有发生换乘
                            if(l.getRoadline()==n.getRoadline()) {
                                hasChange=false;
                                break OK;
                            }
                        }
                    }
                    //换乘发生则添加站点和线路到集合中
                    if(hasChange){
                        for(Stationtoline m:indexLineList){
                            for(Stationtoline n:nextLineList){
                                if(m.getRoadline()==n.getRoadline()){
                                    //第一次记录换乘线路需要同时记录换乘前后的线路
                                    if(changeLine.isEmpty()){
                                        for(Stationtoline l:indexLineList){
                                            for(Stationtoline k:lastLineList){
                                                if(l.getRoadline()==k.getRoadline()){
                                                    changeLine.add(l.getRoadline());
                                                }
                                            }
                                        }
                                    }
                                    changeLine.add(m.getRoadline());
                                }
                            }
                        }
                        changeStation.add(roadstations.get(i));
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append("从" + startstation.getName() + "到" + endstation.getName() + "的最短换乘路线为：" + "<br>" + "乘");
                for(int i=0;i<changeStation.size();i++){
                    sb.append(changeLine.get(i).getName() + "至" + changeStation.get(i).getName() + "换乘");
                    if(i==changeStation.size()-1){
                        sb.append(changeLine.get(changeStation.size()).getName()).append("后直达终点");
                    }
                }
                //转换为json格式的数据
                map.put("html",sb.toString());
                map.put("changeStation",jsonUtil.roadStationList(changeStation));
//                map.put("changeLine",changeLine);
                map.put("roadstations",jsonUtil.roadStationList(graph.findelapsedStations()));
                map.put("type",type);
                map.put("weight",weight);
                map.put("status","success");
            }else{
                map.put("status","没有这样的最短路径");
            }
        }
        else{
            //没有这样的起始站点
            map.put("status","找不到这样的起点终点的站台,请输入正确的起点和终点的站台。");
        }

        return map;
    }

  /*  public static void main(String[] args) {
        List<Integer> test = new ArrayList<>();
        test.add(1);
        System.out.println(test.get(0));
    }*/
}
