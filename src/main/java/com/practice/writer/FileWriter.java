package com.practice.writer;

import com.practice.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psingh15 on 5/20/17.
 */
@Slf4j
@Data
public class FileWriter  implements ItemWriter<List<User>>, InitializingBean {
    @Autowired
    CassandraOperations cassandraOperations;
    @Override
    public void write(List<? extends List<User>> items) throws Exception {
        if (!items.isEmpty()) {
            List<List<User>> wrappedUserItems = (List<List<User>>) items;
            for (List<User> users : wrappedUserItems) {
                cassandraOperations.ingest("INSERT INTO user(id,"
                        + " start_date ,end_date , name, role) values (?, ?, ?, ?, ?) ", getCqlIngestFormatForUser
                        (users));
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * getCqlIngestFormatForUser.
     * This method creates a List for Bulk CQL injestion
     */
    private List<List<?>> getCqlIngestFormatForUser(List<User> users) {
        List<List<?>> userFinalList = new ArrayList<List<?>>();
        for (User user : users) {
            List<Object> userList = new ArrayList<Object>();
            userList.add(user.getUserId());
            userList.add(user.getStartDate());
            userList.add(user.getEndDate());
            userList.add(user.getName());
            userList.add(user.getRole());
            userFinalList.add(userList);
        }
        return userFinalList;
    }
}
