package edu.kh.jdbc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static edu.kh.jdbc.common.JDBCTemplate.*;
// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명.메서드명() 이 아닌 메서드명() 만 작성해도 호출 가능하게 함.

import edu.kh.jdbc.model.dto.User;

// (Model 중 하나) DAO (Data Access Object : 데이터 접근 객체)
// 데이터가 저장된 곳(DB)에 접근하는 용도의 객체
// -> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// SQL을 수행하고 결과를 반환받는 역할
public class UserDAO {
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/**
	 * 1. User 등록 DAO
	 * 
	 * @param conn : DB 연결 정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id, pw, name이 세팅된 User 객체
	 * @return INSERT 결과 행의 갯수
	 */
	public int insertUser(Connection conn, User user) throws Exception {
		// 1. 결과 저장용 변수 선언
		int result = 0;

		// catch를 안쓰는 이유
		// throws Exception을 통해 처음 호출한 곳에서 에러를 처리
		// 이미 main에서 try로 감싸고 있기 때문에, 호출한 곳에서 처리

		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO TB_USER
					VALUES (SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					""";
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// 4. ? 위치홀더에 알맞은 값 대입
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());

			// 5. SQL 수행(executeUpdate()) 후
			// 결과(삽입된 행의 갯수) 반환 받기
			result = pstmt.executeUpdate();
		} finally {
			// 6. 사용한 jdbc 객체 자원 반환
			close(pstmt);
		}
		// 결과 저장용 변수에 저장된 최종 값 반환
		return result;
	}

	public List<User> selectAll(Connection conn) throws Exception {
		// 1. 결과 저장용 변수 선언
		List<User> userlist = new ArrayList<User>();
		try {

			// 2. SQL 작성
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					ORDER BY USER_NO
					""";
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);

			// 4. ? (위치홀더)에 알맞은 값 대입(없으면 패스)

			// 5. SQL(SELECT) 수행(executeQuery()) 후 결과 반환(ResultSet) 받기
			rs = pstmt.executeQuery();

			// 6. 조회 결과(rs)를 1행씩 접근하여 컬럼 값 얻어오기
			// 몇 행이 조회될지 모른다 -> while
			// 무조건 1행만 조회된다 -> if

			while (rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				// -> java.sql.Date 타입으로 값을 저장하지 않은 이유
				// -> SELECT 문에서 TO_CHAR()를 이용하여 문자열 형태ㅗㄹ
				// 변환해 조회해왔기 때문.

				// User 객체 새로 생성하여 DB에서 얻어온 컬럼값 필드로 세팅
				User user = new User(userNo, userId, userPw, userName, enrollDate);

				userlist.add(user);
			}

		} finally {
			// 7. 사용한 자원 반환
			close(rs);
			close(pstmt);
		}
		return userlist;
	}

	public List<User> searchUser(Connection conn, String name) throws Exception {
		List<User> userList = new ArrayList<User>();
		try {
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
						TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NAME LIKE '%' || ? || '%'
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				User user = new User(userNo, userId, userPw, userName, enrollDate);

				userList.add(user);
			}

		} finally {
			close(pstmt);
			close(rs);
		}
		return userList;

	}

	public User searchByNum(Connection conn, int number) throws Exception {
		User user = null;

		try {
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
						TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NO = ?
					""";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, number);
			rs = pstmt.executeQuery();
			
			
			while (rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				user = new User(userNo, userId, userPw, userName, enrollDate);

			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		return user;
	}

	public int deleteUser(Connection conn, int number) throws Exception{
		
		int result = 0;
		
		try {
			String sql = """
					DELETE FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, number);
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateUser(Connection conn, String id, String pw, String name) throws Exception{
		int result = 0;
		
		try {
			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_ID = ? AND USER_PW = ?
					""";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pw);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int addUser(Connection conn, String id, String pw, String name) throws Exception{
		int result = 0;
		
		try {
			String sql1 = """
					SELECT USER_ID
					FROM TB_USER
					WHERE USER_ID = ?
					""";
			
			pstmt = conn.prepareStatement(sql1);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			boolean flag = false;
			
			while(rs.next()) {
				flag = true;
			}
			
			if(flag) {
				return 0;
			}

			
			String sql2 = """
					INSERT INTO TB_USER
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					""";
			pstmt = conn.prepareStatement(sql2);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			
			result = pstmt.executeUpdate();
			
			
		} finally{
			close(pstmt);
		}
		return result;
	}
}














