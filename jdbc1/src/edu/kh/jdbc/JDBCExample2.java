package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample2 {
	public static void main(String[] args) {
		// 입력 받은 급여보다 초과해서 받는 사원의
		// 사번, 이름, 급여 조회
		
		// 1. JDBC 객체 참조용 변수 선언
		Connection conn = null; // DB연결 정보 저장 객체
		Statement stmt = null; // SQL 수행, 결과 반환용 객체
		ResultSet rs = null; // select 수행 결과 저장용 객체
		
		Scanner sc =  null; // 키보드 입력용 객체
		
		
		try {
			// 2. DriverManager 객체 이용해서 Connection 객체 생성
			// 2-1) Oracle JDBC Driver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 오라클과 대화할 수 있는 통역사(Driver)를 메모리에 불러오는 코드
			
			String type = "jdbc:oracle:thin:@";
			String host = "localhost";
			
			String port = ":1521";
			String dbName = ":XE";
			
			String userName = "kh_ajh";
			String password = "kh1234";
			
			// 2-3) DB연결 정보와 DriveManager를 이용해서 Connection 객체 생성
			conn = DriverManager.getConnection(type + host + port + dbName, userName, password);
			
			sc = new Scanner(System.in);
			
			System.out.printf("급여 입력 : ");
			int input = sc.nextInt();
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY "
			           + "FROM EMPLOYEE "
			           + "WHERE SALARY > " + input;

			
			// 4. Statement 객체 생성
			stmt = conn.createStatement();			
			
			// 5. Statement 객체를 이용하여 SQL 수행 후 결과 반환 받기
			// executeQuery() : SELECT 실행, ResultSet 반환
			// executeUpdate() : DML 실행, 결과 행의 갯수 반환(int)
			rs = stmt.executeQuery(sql);
			
			// 6. 조회 결과가 담겨있는 ResultSet을
			// 1행씩 접근해 각 행에 작성된 컬럼값 얻어오기
			// -> ResultSet에 1행 이상이 있을 것으로 예상되는 경우 반복문 사용
			
			while(rs.next()) {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				System.out.printf("%s / %s / %d원 \n", empId, empName, salary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				if(sc != null) sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}