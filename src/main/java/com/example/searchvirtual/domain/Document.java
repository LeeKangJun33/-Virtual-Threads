package com.example.searchvirtual.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
