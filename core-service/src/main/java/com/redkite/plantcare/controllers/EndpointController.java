package com.redkite.plantcare.controllers;

import com.redkite.plantcare.dao.LogDataDao;
import com.redkite.plantcare.model.nosql.LogData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class EndpointController {

    @Autowired
    private LogDataDao logDataDao;

    @RequestMapping(value = "/endpoints/data", method = RequestMethod.POST)
    public void logData(@RequestBody LogData logData) {
        logData.setLogTime(new Date());
        logDataDao.save(logData);
    }

}
