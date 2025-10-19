package com.davidlowe.submarinekata.models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScannerKey implements Serializable
{
    private int horizontalPos;
    private int depthPos;


}
