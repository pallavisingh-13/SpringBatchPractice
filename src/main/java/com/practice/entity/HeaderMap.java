package com.practice.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by psingh15 on 5/17/17.
 */
@Data
@Component
public class HeaderMap {
    Map<String, Integer> positionMap;
}
