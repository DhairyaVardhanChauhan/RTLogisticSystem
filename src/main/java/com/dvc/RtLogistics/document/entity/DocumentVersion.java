package com.dvc.RtLogistics.document.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "document_versions")
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    private Integer version;

//    @Column(columnDefinition = "TEXT")
//    private String snapshot;

    private Long authorId;

    private Instant createdAt;
}

