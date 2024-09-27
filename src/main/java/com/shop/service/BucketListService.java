package com.shop.service;

import com.shop.DataNotFoundException;
import com.shop.entity.Bucket;
import com.shop.repository.BucketListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BucketListService {

    private final BucketListRepository bucketListRepository;
    private final String uploadDir = "/Users/hyeranpakr/Desktop/happy/9.14/shop/";

    @Transactional(readOnly = true)
    public List<Bucket> getList() {
        return bucketListRepository.findAllByOrderByCreateDateDesc();
    }

    public Bucket getBucket(Long id) {
        return bucketListRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Bucket not found with id: " + id));
    }

    @Transactional
    public void create(String title, String items, MultipartFile file, int rating) throws IOException {
        validateFile(file);

        String fileName = saveFile(file);

        Bucket bucket = new Bucket();
        bucket.setTitle(title);
        bucket.setItems(items);
        bucket.setFileName(fileName);
        bucket.setFilePath("/files/" + fileName);
        bucket.setRating(rating);
        bucket.setCreateDate(LocalDateTime.now());

        bucketListRepository.save(bucket);
    }

    @Transactional
    public void modify(Bucket bucket, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // 기존 파일 삭제
            if (bucket.getFileName() != null) {
                deleteFile(bucket.getFileName());
            }

            // 새 파일 저장
            String fileName = saveFile(file);
            bucket.setFileName(fileName);
            bucket.setFilePath("/files/" + fileName);
        }

        bucket.setCreateDate(LocalDateTime.now());
        bucketListRepository.save(bucket);
    }

    @Transactional
    public void deleteBucket(Long id) {
        Bucket bucket = bucketListRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Bucket not found with id: " + id));

        // 파일 삭제
        if (bucket.getFileName() != null) {
            deleteFile(bucket.getFileName());
        }

        // 데이터베이스에서 Bucket 삭제
        bucketListRepository.deleteById(id);
    }

    // 파일 검증 로직
    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalStateException("파일 크기가 너무 큽니다. 최대 5MB까지 업로드할 수 있습니다.");
        }
    }

    // 파일 저장 로직
    private String saveFile(MultipartFile file) throws IOException {
        String projectPath = uploadDir;
        Files.createDirectories(Paths.get(projectPath)); // 경로가 없으면 생성

        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(projectPath + fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

    // 파일 삭제 로직
    private void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace(); // 로그 처리 또는 예외 처리 필요
            throw new IllegalStateException("파일 삭제 중 오류가 발생했습니다.");
        }
    }
}
