package com.pictspace.back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequestDto {
    private Long bookingId;
    private Long approverId; // HOD or Principal
    private boolean approved; // true = approve, false = reject
    private String message; // Optional message for rejection
}
