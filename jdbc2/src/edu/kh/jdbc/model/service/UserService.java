package edu.kh.jdbc.model.service;

import java.sql.Connection;
import java.util.List;

import static edu.kh.jdbc.common.JDBCTemplate.*;
import edu.kh.jdbc.model.dao.UserDAO;
import edu.kh.jdbc.model.dto.User;

// (Model 중 하나) Service : 비즈니스 로직을 처리하는 계층, 
// 데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행

public class UserService {
	private UserDAO dao = new UserDAO();

	/**
	 * 1. User 등록 서비스
	 * 
	 * @param user : 입력받은 id, pw, name이 세팅된 객체
	 * @return insert된 결과 행의 갯수
	 */
	public int insertUser(User user) throws Exception {
		// 1. 커넥션 생성
		Connection conn = getConnection();

		// 2. 데이터 가공 (할 것 없으면 생략)

		// 3. DAO 메서드 호출 후 결과 반환받기
		int result = dao.insertUser(conn, user);

		// 4. DML (INSERT) 수행 결과에 따라 트랜잭션 제어 처리
		if (result > 0) { // INSERT 성공
			commit(conn);
		} else { // INSERT 실패
			rollback(conn);
		}

		// 5. Connection 반환
		close(conn);

		// 6. 결과 반환
		return result;
	}

	/**
	 * 2. User 전체 조회 서비스
	 * 
	 * @return 조회된 User들이 담긴 List
	 * @throws Exception
	 */
	public List<User> selectAll() throws Exception {
		Connection conn = getConnection();
		List<User> list = null;

		list = dao.selectAll(conn);

		close(conn);

		return list;
	}

	public List<User> searchUser(String name) throws Exception {
		// 1. 커넥션 생성
		Connection conn = getConnection();

		// 2. DAO 메서드 호출(SELECT) 후 결과반환(List<User>) 받기
		List<User> list = dao.searchUser(conn, name);
		// 3. Conecction 반환
		close(conn);

		// 4. 결과 반환
		return list;
	}

	public User searchByNum(int number) throws Exception{
		Connection conn = getConnection();
		
		User user = dao.searchByNum(conn, number);
		close(conn);
		return user;
	}

	public int deleteUser(int number) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.deleteUser(conn, number);
		
		if (result > 0) { // DELETE 성공
			commit(conn);
		} else { // DELETE 실패
			rollback(conn);
		}

		// 5. Connection 반환
		close(conn);

		// 6. 결과 반환
		return result;
	}

	public int updateUser(String id, String pw, String name) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.updateUser(conn, id, pw, name);
		
		if(result > 0) {
			 commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		
		return result;
	}

	public int addUser(String id, String pw, String name) throws Exception {
		Connection conn = getConnection();
		
		int result = dao.addUser(conn, id, pw, name);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		
		return result;
	}
}






