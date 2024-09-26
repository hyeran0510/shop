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

    // 최신 항목이 위로 올라오게 하는 getList 메서드
    @Transactional(readOnly = true)
    public List<Bucket> getList() {
        return bucketListRepository.findAllByOrderByCreateDateDesc();
    }

    // 개별 Bucket 가져오기
    public Bucket getBucket(Long id) {
        Optional<Bucket> bucket = this.bucketListRepository.findById(id);
        return bucket.orElseThrow(() -> new DataNotFoundException("Bucket not found with id: " + id));
    }

    // 파일 및 데이터 저장
    public void create(String title, String items, MultipartFile file, int rating) throws IOException {
        long maxFileSize = 5 * 1024 * 1024; // 5MB

        // 파일 크기 체크
        if (file.getSize() > maxFileSize) {
            throw new IllegalStateException("파일 크기가 너무 큽니다. 최대 5MB까지 업로드할 수 있습니다.");
        }

        // 파일 저장할 경로 설정
        String projectPath = uploadDir;

        // 디렉토리가 존재하지 않으면 생성
        Files.createDirectories(Paths.get(projectPath));

        // 파일 이름 중복 방지를 위한 UUID 생성
        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(projectPath + fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        // Bucket 객체 생성 및 저장
        Bucket bucket = new Bucket();
        bucket.setTitle(title);
        bucket.setItems(items);
        bucket.setFileName(fileName);
        bucket.setFilePath("/files/" + fileName);
        bucket.setRating(rating);
        bucket.setCreateDate(LocalDateTime.now());

        this.bucketListRepository.save(bucket);
    }

    // Bucket 수정
    public void modify(Bucket bucket, MultipartFile file) throws IOException {
        String projectPath = uploadDir;

        // 파일이 있으면 새로운 파일 저장
        if (file != null && !file.isEmpty()) {
            // 기존 파일 삭제
            if (bucket.getFileName() != null) {
                Files.deleteIfExists(Paths.get(projectPath + bucket.getFileName()));
            }

            // 새 파일 저장
            String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(projectPath + fileName);
            Files.copy(file.getInputStream(), filePath);

            // 파일 이름 및 경로 업데이트
            bucket.setFileName(fileName);
            bucket.setFilePath("/files/" + fileName);
        }

        // 다른 필드 업데이트
        bucket.setCreateDate(LocalDateTime.now());

        // 데이터베이스에 업데이트
        this.bucketListRepository.save(bucket);
    }

    // Bucket 삭제
    public void deleteBucket(Long id) {
        Optional<Bucket> optionalBucket = bucketListRepository.findById(id);
        if (optionalBucket.isPresent()) {
            Bucket bucket = optionalBucket.get();
            try {
                // 파일 삭제
                if (bucket.getFileName() != null) {
                    String projectPath = uploadDir;
                    Files.deleteIfExists(Paths.get(projectPath + bucket.getFileName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 데이터베이스에서 Bucket 삭제
            bucketListRepository.deleteById(id);
        } else {
            throw new DataNotFoundException("Bucket not found with id: " + id);
        }
    }
}
