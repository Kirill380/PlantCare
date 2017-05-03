package com.redkite.plantcare.dao;

import com.redkite.plantcare.model.nosql.LogData;
import org.springframework.stereotype.Repository;

@Repository
public class LogDataDao extends AbstractCassandraDao<LogData, String> {


    @Override
    protected Class<LogData> getColumnFamilyClass() {
        return LogData.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return "endpoint_data";
    }

    @Override
    public LogData save(LogData entity) {
        return super.save(entity);
    }

    public LogData getLogData(String email) {
        return find(email);
    }
}
