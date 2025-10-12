package com.davidlowe.submarinekata.models;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

@AllArgsConstructor
@ToString
public enum Direction implements Serializable
{
    FORWARD("forward"),
    UP("up"),
    DOWN("down");

    private final String name;

    public static Direction fromString(String directionString)
    {
        if (StringUtils.isBlank(directionString))
            return null;

        return Arrays.stream(values())
                     .filter(dir -> dir.name.equals(directionString))
                     .findFirst()
                     .orElse(null);
    }
}
