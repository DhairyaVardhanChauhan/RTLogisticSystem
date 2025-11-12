package com.dvc.RtLogistics.document.controller;

import com.dvc.RtLogistics.document.dto.ChatMessage;
import com.dvc.RtLogistics.document.dto.CursorMessage;
import com.dvc.RtLogistics.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/doc.{docId}.edit")
    public void handleEdit(@DestinationVariable String docId, @Payload ChatMessage edit) throws IOException {
        documentService.applyEdit(docId, edit);
    }

    @MessageMapping("/doc.{docId}.cursor")
    public void handleCursor(@DestinationVariable String docId, CursorMessage cursorMessage){
        simpMessagingTemplate.convertAndSend("/topic/document/" + docId + "/cursor",cursorMessage);
    }

}
