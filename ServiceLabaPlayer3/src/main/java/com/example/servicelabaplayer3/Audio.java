package com.example.servicelabaplayer3;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AudioData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(name = "audiodata",length = 10000000)
    private byte[] audioData;
}
