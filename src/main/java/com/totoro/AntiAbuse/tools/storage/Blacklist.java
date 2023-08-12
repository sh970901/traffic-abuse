package com.totoro.AntiAbuse.tools.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Blacklist {
    private List<String> ipAddress;
    private List<String> memberIds;

    public static Blacklist fromJson(String toString) {
        return new Blacklist();
    }

    public String toJson() {
        return "gg";
    }
    public Blacklist(){
        this.ipAddress = new ArrayList<>();
        this.memberIds = new ArrayList<>();
    }
    public Blacklist(List<String> ipAddress, List<String> memberIds){
        this.ipAddress = ipAddress;
        this.memberIds = memberIds;
    }
}
