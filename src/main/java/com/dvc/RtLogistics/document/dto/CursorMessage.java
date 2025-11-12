package com.dvc.RtLogistics.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorMessage {
    private String userId;
    private String userName;
    private int cursorPosition;
    private String color;
}
