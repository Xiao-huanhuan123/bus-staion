package com.basic.bustation.controller;

import com.basic.bustation.model.Roadstation;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional(propagation= Propagation.REQUIRED)
public class StationToLineController extends BaseController{
    @RequestMapping(value = "/product_deletebyIds.action",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void deleteByRoadlineId(@RequestParam int id){
        stationtolineDAO.delete(id);
    }
}
