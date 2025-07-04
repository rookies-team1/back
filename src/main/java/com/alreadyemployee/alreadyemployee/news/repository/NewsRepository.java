package com.alreadyemployee.alreadyemployee.news.repository;


import com.alreadyemployee.alreadyemployee.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
}
