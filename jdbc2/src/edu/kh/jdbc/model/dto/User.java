package edu.kh.jdbc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// DTO (Data Transfer Object : 데이터 전송 객체)
// : 값을 묶어서 전달하는 용도의 객체
// -> DB에 데이터를 묶어서 전달하거나,
// DB에서 조회한 결과를 가져올 때 사용 (데이터 교환을 위한 객체)
// == DB 특정 테이블의 한 행의 데이터를
// 저장할 수 있는 형태로 class 정의

// lombok : 자바 코드에서 반복적으로 작성해야하는(보일러플레이트 코드)를
// 내부적으로 자동 완성해주는 라이브러리

// 1. 사용하고자하는 프로젝트에 라이브러리 추가
// 2. java 코드 작성하고있는 IDE(툴) 설치



@Getter
@Setter
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 매개변수가 있는 생성자
@ToString // toString

public class User {
	private int userNo;
	private String userId;
	private String userPw;
	private String userName;
	private String enrollDate;
	// -> enrollDate를 왜 java.sql.Date 타입이 아니라 String으로 정했는가?
	// -> DB 조회 시 날짜 데이터를 원하는 형태의 문자열로 변환하여 조회할 예정
	// -> TO_CHAR() 이용 시 -> EX) 2025-10-28 16:18:51.000
	
	
	
}
