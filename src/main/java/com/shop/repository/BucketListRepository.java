package com.shop.repository;

import com.shop.entity.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BucketListRepository extends JpaRepository<Bucket, Long> {

    @Query("SELECT b FROM Bucket b ORDER BY b.createDate DESC")
    List<Bucket> findAllByOrderByCreateDateDesc();
}
