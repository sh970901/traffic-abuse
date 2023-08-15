package com.totoro.AntiAbuse.abusing.dto;

import com.totoro.AntiAbuse.tools.storage.Blacklist;
import com.totoro.AntiAbuse.tools.storage.Rule;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AbuseRuleDto {

    private String id;
    private String type;

    private Rule rule;

    private Blacklist blacklist;

}
