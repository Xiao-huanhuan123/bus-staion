package com.basic.bustation.controller;

import com.basic.bustation.model.Roadline;
import com.basic.bustation.model.Roadstation;
import com.basic.bustation.model.Stationtoline;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Transactional(propagation= Propagation.REQUIRED)
public class StationToLineController extends BaseController{
    @RequestMapping(value = "/stationtoline_deleteByIds.action",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteByRoadlineId(@RequestParam long id)throws ParseException {
        Map<String,Object> map=new HashMap<>();
        System.out.println("hello");
        Roadline roadline = roadlineDAO.findById(id);
        roadlineDAO.delete(roadline);
        roadstationDAO.save(new Roadstation(9L,"未添加站点","站点",Double.valueOf(0),Double.valueOf(0),Double.valueOf(2)));
        map.put("success",true);
        return gson.toJson(map);
    }
}
