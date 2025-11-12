package com.dvc.RtLogistics.document.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private int position;
    private int deleteCnt;
    private String insertTxt;
    private String userId;
    private int baseVersion;
}

