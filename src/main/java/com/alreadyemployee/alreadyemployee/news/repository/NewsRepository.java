package com.alreadyemployee.alreadyemployee.news.repository;


import com.alreadyemployee.alreadyemployee.news.controller.dto.NewsIdContentResponseDTO;
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

    // id와 contents만 담아 넘겨주는 JQPL 쿼리
    //반환받을 id와 contents를 한 번에 넘겨주려고 dto 파일을 만듦
    //select로 반환해줄때 dto 객체에 넣어 원하는 값을 묶어줌
    @Query("SELECT " +
                "new com.alreadyemployee.alreadyemployee." +
            "       news.controller.dto.NewsIdContentResponseDTO(n.id, n.contents) " +
            "FROM News n")
    List<NewsIdContentResponseDTO> findAllIdContent();
}
