package com.dvc.RtLogistics.document.service;

import com.dvc.RtLogistics.document.dto.ChatMessage;
import com.dvc.RtLogistics.document.entity.Document;
import com.dvc.RtLogistics.document.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    public Document loadDocument(String id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id));
    }

    public void applyEdit(String docId, ChatMessage chatMessage) throws IOException {
        Document document = loadDocument(docId);
        StringBuilder stringBuilder = new StringBuilder(document.getContentSnapshot());
        stringBuilder.replace(chatMessage.getPosition(),chatMessage.getPosition() + chatMessage.getDeleteCnt(),chatMessage.getInsertTxt());
        document.setContentSnapshot(stringBuilder.toString());
        document.setVersion(chatMessage.getBaseVersion() + 1);
        document.setUpdatedAt(Instant.now());
        documentRepository.save(document);
        System.out.println("DB call initiated!");
        simpMessagingTemplate.convertAndSend("/topic/document/"+docId,document);
    }
}
