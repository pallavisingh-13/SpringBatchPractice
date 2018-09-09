package com.practice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by psingh15 on 5/17/17.
 */
@Getter
@Setter
@NoArgsConstructor
@Table("user")

public class User implements Serializable {
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    String userId;

    @PrimaryKeyColumn(name = "start_date", ordinal = 2, type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    Date startDate;

    @Column(value = "end_date")
    Date endDate;

    Map<String, String> role;

    String name;


    @Override
    public String toString() {
        StringBuffer user = new StringBuffer();
        return user.append("User ")
                .append("[")
                .append("  id=").append(userId)
                .append(", start_date=").append(startDate)
                .append(", end_date=").append(endDate)
                .append(", role=").append(role)
                .append(", name=").append(name)
                .append("]").toString();
    }

    /**
     * getStartDate.
     * @return Date
     */
    public Date getStartDate() {
        if (null == startDate) {
            return null;
        } else {
            return new Date(startDate.getTime());
        }
    }

    /**
     * setStartDate.
     * @param startDate startDate
     */
    public void setStartDate(Date startDate) {
        if (null == startDate) {
            this.startDate = null;
        } else {
            this.startDate = new Date(startDate.getTime());
        }
    }
    /**
     * getEndDate.
     * @return Date
     */
    public Date getEndDate() {
        if (null == endDate) {
            return null;
        } else {
            return new Date(endDate.getTime());
        }
    }

    /**
     * setEndDate.
     * @param endDate startDate
     */
    public void setEndDate(Date endDate) {
        if (null == endDate) {
            this.endDate = null;
        } else {
            this.endDate = new Date(endDate.getTime());
        }
    }
}
