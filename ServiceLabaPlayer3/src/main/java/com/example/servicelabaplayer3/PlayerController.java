package com.example.servicelabaplayer3;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/audio")
public class PlayerController {
    @Autowired
    private PlayerService service;

    @PostMapping
    public ResponseEntity<?> uploadAudioRequest(@RequestParam("audio") MultipartFile file) throws IOException {
        String uploadAudio = service.uploadAudio(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadAudio);
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAudio(@RequestParam("audio") MultipartFile file) throws IOException {
        String uploadAudio = service.uploadAudio(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadAudio);
    }

//    @GetMapping("/{fileName}")
//    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
//        byte[] imageData=service.downloadImage(fileName);
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/mp3"))
//                .body(imageData);
//    }
private static class Range {
    @Getter
    private final long start;
    @Getter
    private final long end;
    @Getter
    private final long length;

    public Range(long start, long  end, long length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }
}
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadAudio(
            @PathVariable String fileName,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {

        byte[] audioData = service.downloadAudio(fileName);

        if (audioData == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.setContentLength(audioData.length);

        if (rangeHeader != null) {
            Range range = parseRangeHeader(rangeHeader, audioData.length);

            if (range != null) {
                headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + range.getStart() + "-" + range.getEnd() + "/" + audioData.length);
                headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
                headers.setContentLength(range.getLength());
                return new ResponseEntity<>(new ByteArrayResource(Arrays.copyOfRange(audioData, (int) range.getStart(), (int) (range.getEnd() + 1))), headers, HttpStatus.PARTIAL_CONTENT);
            }
        }

        return new ResponseEntity<>(new ByteArrayResource(audioData), headers, HttpStatus.OK);
    }

    private Range parseRangeHeader(String rangeHeader, long contentLength) {
        // Пример заголовка Range: bytes=500-999
        if (rangeHeader.startsWith("bytes=")) {
            String[] rangeValues = rangeHeader.substring(6).split("-");
            if (rangeValues.length == 2) {
                try {
                    long start = Long.parseLong(rangeValues[0]);
                    long end = Long.parseLong(rangeValues[1]);

                    // Проверяем, что начальное значение не превышает длину контента и
                    // что конечное значение не превышает начальное и длину контента
                    if (end >= start && end < contentLength) {
                        long length = end - start + 1;
                        return new Range(start, end, length);
                    }
                } catch (NumberFormatException e) {
                    // Ошибка парсинга значений Range
                }
            }
        }

        return null;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Audio>> getAllFiles() {
        List<Audio> allFiles = service.getAllFiles();
        return ResponseEntity.status(HttpStatus.OK)
                .body(allFiles);
    }

        @DeleteMapping("/{fileName}")
    public ResponseEntity<?> deleteAudio(@PathVariable String fileName) {
        boolean isDeleted = service.deleteAudio(fileName);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("File deleted successfully: " + fileName);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found: " + fileName);
        }
    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteAudioById(@PathVariable Long id) {
//        boolean isDeleted = service.deleteAudioById(id);
//        if (isDeleted) {
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body("successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("not found ID: " + id);
//        }
//    }
    @GetMapping("/names")
    public ResponseEntity<List<String>> getNamesOfAllFiles() {
        List<String> fileNames = service.getNamesOfAllFiles();
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileNames);
    }
}