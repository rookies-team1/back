package com.alreadyemployee.alreadyemployee.news.repository;


import com.alreadyemployee.alreadyemployee.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
    List<News> findByCompanyName(@Param("companyName") String companyName);

    // title만 가져오는 JPQL 쿼리
    @Query("SELECT n.title FROM News n WHERE n.companyName = :companyName")
    List<String> findTitlesByCompanyName(@Param("companyName") String companyName);
}
