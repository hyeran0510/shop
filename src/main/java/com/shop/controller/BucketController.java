package com.shop.controller;

import com.shop.repository.BucketListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // Correct import for @Value
import org.springframework.ui.Model;
import com.shop.dto.BucketForm;
import com.shop.entity.Bucket;
import com.shop.service.BucketListService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/bucket")
public class BucketController {

    private final BucketListRepository bucketListRepository;
    private final BucketListService bucketListService;

    @Value("${upload.dir}") // Use @Value from Spring
    private String uploadDir;

    @GetMapping("/list")
    public String getBucketList(Model model) {
        List<Bucket> bucketList = bucketListService.getList();
        model.addAttribute("bucketList", bucketList); // 버킷리스트 모델에 추가
        return "bucket_list";
    }

    @PostMapping("/create")
    public String create(String title, String items, MultipartFile file, int rating) throws IOException {
        Bucket bucket = new Bucket();
        bucket.setTitle(title);
        bucket.setItems(items);
        bucket.setRating(rating);

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            File destinationFile = new File(uploadDir + fileName);
            file.transferTo(destinationFile);
            bucket.setFilePath("/upload/" + fileName);  // /upload/ 파일이 실제로는 static/upload 디렉토리에 위치하게 됩니다.
        }

        bucketListRepository.save(bucket); // 인스턴스를 사용하여 호출
        return "redirect:/bucket/list"; // 성공 후 리다이렉트
    }

    @GetMapping("/bucket/modify/{id}")
    public String modifyBucketForm(@PathVariable Long id, Model model) {
        Bucket bucket = bucketListService.getBucket(id);
        model.addAttribute("bucket", bucket);
        return "modify"; // modify.html
    }

    @PostMapping("/update")
    public String updateBucket(@ModelAttribute Bucket bucket, @RequestParam MultipartFile file) throws IOException {
        bucketListService.modify(bucket, file); // 수정 로직
        return "redirect:/bucket/list"; // 수정 후 목록으로 리다이렉트
    }
}
