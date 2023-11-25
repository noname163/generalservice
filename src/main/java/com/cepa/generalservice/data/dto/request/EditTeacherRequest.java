package com.cepa.generalservice.data.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EditTeacherRequest {
    private String cardNumber;
    private List<Long> subjectId;
}
