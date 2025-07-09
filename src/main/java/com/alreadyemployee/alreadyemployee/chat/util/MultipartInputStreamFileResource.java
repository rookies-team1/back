package com.alreadyemployee.alreadyemployee.chat.util;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

/**
 * MultipartFile을 다른 서버로 전송할 때 사용되는 InputStream 기반의 리소스 클래스
 */
public class MultipartInputStreamFileResource extends InputStreamResource {

    //Python LLM Server로 파일이름도 같이 보내야 함
    private final String filename;

    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    //저장한 파일 이름을 알려줌
    @Override
    public String getFilename() {
        return this.filename;
    }

    //InputStream은 한 번 읽으면 다시 못 읽는 단방향 통로여서
    //contentLength()를 계산하려고 파일을 읽어버리면 실제로 전송할 때 파일 내용이 없어짐
    //그래서 contentLength()에서 -1을 반환
    //파일을 디스크에 저장하지 않고 메모리 스트림만으로 전송할 때 중요
    @Override
    public long contentLength() {
        return -1; // content length를 알 수 없는 경우
    }
}