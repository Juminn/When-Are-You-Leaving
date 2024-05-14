package com.enm.costcalculrator.data.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Transportation {
        WALKING(Collections.emptyList()),
        SUBWAY(Arrays.asList("TRAIN")),
        BUS(Arrays.asList("INTERCITYBUS", "EXPRESSBUS"));
        //AIRPLANE,
        //TRAIN,
       // INTERCITYBUS

        private List<String> aliases;

        Transportation(List<String> aliases) {
                this.aliases = aliases;
        }

        public boolean includes(String transportationName) {
                return this.name().equals(transportationName) || aliases.contains(transportationName);
        }



}
