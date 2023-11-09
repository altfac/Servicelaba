package com.example.servicelabaplayer3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.Utilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository repository;

    public String uploadAudio(MultipartFile file) throws IOException {

        Audio audioData = repository.save(Audio.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .audioData(AudioUtil.compressAudio(file.getBytes())).build());
        if (audioData != null) {
            return file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadAudio(String fileName){
        Optional<Audio> dbAudioData = repository.findByName(fileName);
        byte[] audios=AudioUtil.decompressAudio(dbAudioData.get().getAudioData());
        return audios;
    }
    public List<Audio> getAllFiles() {
        return repository.findAll();
    }
    public boolean deleteAudio(String fileName) {
        Optional<Audio> audioData = repository.findByName(fileName);
        if (audioData.isPresent()) {
            repository.delete(audioData.get());
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteAudioById(Long id) {
        Optional<Audio> audioData = repository.findById(id);
        if (audioData.isPresent()) {
            repository.delete(audioData.get());
            return true;
        } else {
            return false;
        }
    }
    public List<String> getNamesOfAllFiles() {
        List<Audio> allFiles = repository.findAll();
        List<String> fileNames = new ArrayList<>();
        for (Audio file : allFiles) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }
}
