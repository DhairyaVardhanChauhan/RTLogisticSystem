package com.dvc.RtLogistics.document.controller;

import com.dvc.RtLogistics.document.entity.Document;
import com.dvc.RtLogistics.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class DocDataController {
    private final DocumentService documentService;
    @GetMapping("/documents/{id}")
    public Document getDocument(@PathVariable String id){
        return documentService.loadDocument(id);
    }


}
