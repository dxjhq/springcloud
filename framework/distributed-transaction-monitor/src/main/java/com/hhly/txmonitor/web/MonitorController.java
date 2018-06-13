package com.hhly.txmonitor.web;


//import com.hhly.common.dto.ResultObject;
import com.hhly.txmonitor.dto.QueryReq;
import com.hhly.txmonitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wangxianchen
 * @create 2017-09-08
 * @desc
 */
//@RestController
//@RequestMapping("/monitor")
public class MonitorController {

/*   // @Autowired
    private MonitorService monitorService;

    //@RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView page(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
    }

    //@RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResultObject query(@RequestBody QueryReq req) {
        return monitorService.query(req);
    }
    //@RequestMapping(value = "/giveup", method = RequestMethod.POST)
    public ResultObject giveup(@RequestBody QueryReq req) {
        return monitorService.giveup(req);
    }
    //@RequestMapping(value = "/execut", method = RequestMethod.POST)
    public ResultObject execut(@RequestBody QueryReq req) {
        return monitorService.execut(req);
    }

    //@RequestMapping(value = "/timer/{op}")
    public ResultObject timerControl(@PathVariable String op) {
        return monitorService.timerControl(op);
    }
    //@RequestMapping(value = "/timerRestart", method = RequestMethod.POST)
    public ResultObject timerRestart(@RequestBody QueryReq req) {
        return monitorService.timerRestart(req);
    }*/
}

