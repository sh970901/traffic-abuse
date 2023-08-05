package com.totoro.AntiAbuse.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TotoroResponse<T> {
    @JsonIgnore
    private static final String MESSAGE_FAIL_RESULT = "fail";
    @JsonIgnore
    private static final String MESSAGE_SUCCESS_RESULT = "success";
    @JsonIgnore
    private static final String MESSAGE_UNDEFINED_CODE = "undefined";

    @Builder.Default
    private int resultCode = 200;

    @Builder.Default
    @JsonIgnore
    private String result = MESSAGE_SUCCESS_RESULT;

    @Builder.Default
    private String resultMessage = MESSAGE_SUCCESS_RESULT;
    private T data;

    public TotoroResponse<T> fail(String message) {
        setResult(MESSAGE_FAIL_RESULT);
        setResultCode(500);
        setResultMessage(message);

        return this;
    }

    public TotoroResponse<T> fail(int resultCode, String resultMessage) {
        setResult(MESSAGE_FAIL_RESULT);
        setResultCode(resultCode);
        setResultMessage(resultMessage);

        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, TotoroToStringStyle.simpleStyle());
    }
}
