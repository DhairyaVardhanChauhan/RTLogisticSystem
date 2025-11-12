package com.dvc.RtLogistics.document.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name="document")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    private String id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contentSnapshot;

    private Integer version;
    private Long workspaceId;
    private String createdBy;
    private Instant createdAt;

    private Instant updatedAt;
}
