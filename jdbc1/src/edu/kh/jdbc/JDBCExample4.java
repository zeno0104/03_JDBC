package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {
	public static void main(String[] args) {
		// 부서명을 입력받아
		// 해당 부서에 근무하는 사원의
		// 사번, 이름, 부서명, 직급명을
		// 직급코드 오름차순 조회
		
		// [실행화면]
		// 부서명 입력 : 총무부
		// 200 / 선동일 / 총무부 / 대표
		// 202 / 노옹철 / 총무부 / 부사장
		// 201 / 송종기 / 총무부 / 부사장
		
		// 부서명 입력 : 개발팀
		// 일치하는 부서가 없습니다!
		
		// hint : SQL 에서 '' (홑따옴표) 필요
		// ex) 
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner sc = null;
		
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh_ajh";
			String password =  "kh1234";


			
			conn = DriverManager.getConnection(url, userName, password);
			sc = new Scanner(System.in);
			
			System.out.print("부서명 입력 : ");
			String input = sc.next();
			
			stmt = conn.createStatement();
			
			String sql = """
								SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME
								FROM EMPLOYEE E
								JOIN JOB J ON E.JOB_CODE = J.JOB_CODE
								LEFT JOIN DEPARTMENT D ON E.DEPT_CODE = D.DEPT_ID
								WHERE DEPT_TITLE = '""" + input + "'  "
							 + "ORDER BY J.JOB_CODE";
			
			rs = stmt.executeQuery(sql);
		
			// 1) flag 방법
//			boolean flag = false;
//			
//			while(rs.next()) {
//				flag = true;
//				String empId = rs.getString("EMP_ID");
//				String empName = rs.getString("EMP_NAME");
//				String deptTitle = rs.getString("DEPT_TITLE");
//				String jobName = rs.getString("JOB_NAME");
//				
//				System.out.printf("%s / %s / %s / %s\n", empId, empName, deptTitle, jobName);
//			}
//			if(!flag) {
//				System.out.println("일치하는 부서가 없습니다!");
//			}
			
			// 2) return 사용법
			if(!rs.next()) { // 첫행
				System.out.println("일치하는 부서가 없습니다!");
				return;
			}
			// 왜 do~while문?
			// 위 if문 조건에서 이미 첫번째 행 커서가 소비됨.
			// 보통 while문 사용 시 next()를 바로 만나면서 2행부터 접근하게 됨.
			// do~while문 사용핳여 next() 하지 않아도 1번 째행 부터 접근할 수 있도록 함.
			
			do {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s\n", empId, empName, deptTitle, jobName);
			} while(rs.next());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null)
					conn.close();
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(sc != null)
					sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}






