package com.mrg.testshrtome.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mrg.testshrtome.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private String message;
    private StatusEnum status;
    private String requestId;
    private String error;
}
