package com.totoro.AntiAbuse.abusing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totoro.AntiAbuse.utils.TotoroToStringStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "from")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbuseResponseDTO<T> {
    @JsonIgnore
    private static final String MESSAGE_FAIL_RESULT = "fail";
    @JsonIgnore
    private static final String MESSAGE_SUCCESS_RESULT = "success";
    @JsonIgnore
    private static final String MESSAGE_UNDEFINED_CODE = "undefined";

    @JsonIgnore
    private String resultTrace;

    @Builder.Default
    private String resultCode = "200";
    @Builder.Default
    private boolean block = false;

    @Builder.Default
    private String message = "default message";

    private String blockTime;
    private T data;


    private AbuseResponseDTO(Boolean block){
        this.block = block;
    }
    public AbuseResponseDTO<T> fail(String message) {
        return fail("500", message, null);
    }

    public AbuseResponseDTO<T> fail(String message, String traceMessage) {
        return fail("500", message, traceMessage);
    }
    public AbuseResponseDTO<T> fail(String resultCode, String message, String traceMessage) {
        setResultCode(resultCode);

        if(message != null) {
            message = message.replaceAll("\n", "");
            message = message.replaceAll("\r", "");
        }
        setMessage(message);


        if(traceMessage != null) {
            traceMessage = traceMessage.replaceAll("\n", "");
            traceMessage = traceMessage.replaceAll("\r", "");

            setResultTrace(traceMessage);
        }
        return this;
    }

    // 요청을 어뷰징으로 판단
    public static AbuseResponseDTO abuse(){
        return AbuseResponseDTO.from().block(true).build();
    }
    
    //요청을 정상으로 판단
    public static AbuseResponseDTO nonAbuse(){
        return AbuseResponseDTO.from().block(true).build();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, TotoroToStringStyle.simpleStyle());
    }
}
