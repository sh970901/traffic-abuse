package com.totoro.AntiAbuse.rabbitmq.dto;

import com.totoro.AntiAbuse.tools.storage.Blacklist;
import com.totoro.AntiAbuse.tools.storage.Rule;
import com.totoro.AntiAbuse.utils.TotoroToStringStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "from")
public class RuleMessage {

    private Rule rule;

    private Blacklist blacklist;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, TotoroToStringStyle.simpleStyle());
    }

}
