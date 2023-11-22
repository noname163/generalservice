package com.cepa.generalservice.data.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateSubjectTarget {
    private Long targetId;
    private List<StudentSubjectTargetRequest> studentSubjectTargetRequests;
}
