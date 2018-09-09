package com.practice.processor;

import com.practice.entity.HeaderMap;
import com.practice.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by psingh15 on 5/16/17.
 */
@Data
@Slf4j
public class FileProcessor implements
        ItemProcessor<String, List<User>> {

    String csvSplitBy = ",";

    @Autowired
    HeaderMap positionMap;

    @Override
    public List<User> process(String line)
            throws Exception {
        String[] data;
        User userObject;
        List<User> userList = new ArrayList<>();

        if (line == null || line.isEmpty()) {
            return userList;
        }
        //data = line.split(csvSplitBy);
        data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        userObject = populateUserObject(
                data, positionMap.getPositionMap());
        if (userObject == null) {
            return userList;
        } else {
            userList.add(userObject);
        }
        return userList;
    }
    public User populateUserObject(String[] data, Map<String, Integer> positionMap) {
        DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        User user = new User();
        if (!data[positionMap.get("id")].isEmpty()) {
            user.setUserId(data[positionMap.get("id")]);
        }
        if (!data[positionMap.get("start date")].isEmpty()) {
            Date startTime = format.parseDateTime(data[positionMap.get("start date")]).toDate();
            user.setStartDate(startTime);
        }
        if (!data[positionMap.get("end date")].isEmpty()) {
            Date endTime = format.parseDateTime(data[positionMap.get("end date")]).toDate();
            user.setEndDate(endTime);
        }
        if (!data[positionMap.get("role")].isEmpty()) {
            Map<String,String> roleMap = new HashMap<>();
            roleMap.put("Default",data[positionMap.get("role")]);
            user.setRole(roleMap);
        }
        if (!data[positionMap.get("name")].isEmpty()) {
            user.setName(data[positionMap.get("name")]);
        }
        return user;
    }
}
