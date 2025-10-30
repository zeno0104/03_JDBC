package edu.kh.jdbc.view;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model.service.UserService;
import oracle.jdbc.driver.parser.util.Array;

// View : 사용자와 직접 상호작용하는 화면(UI)를 담당,
// (사용자에게) 입력을 받고 결과를 출력하는 역할
public class UserView {

	// 필드
	private UserService service = new UserService();
	private Scanner sc = new Scanner(System.in);

	/**
	 * User 관리 프로그램 메인 메뉴 UI (View)
	 */
	public void mainMenu() {
		int input = 0; // 메뉴 선택용 변수

		do {
			try {
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				switch (input) {
				case 1:
					insertUser();
					break;
				case 2:
					selectAll();
					break;
				case 3:
					selectName();
					break;
				case 4:
					selectUser();
					break;
				case 5:
					deleteUser();
					break;
				case 6:
					updateName();
					break;
				case 7:
					insertUser2(7);
					break;
				case 8:
					multiInsertUser();
					break;
				case 0:
					System.out.println("\n[프로그램 종료]\n");
					break;
				default:
					System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				System.out.println("\n-------------------------------------\n");

			} catch (InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력하셨습니다***\n");
				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거

				e.printStackTrace();
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
		} while (input != 0);
	}

	/**
	 * 1. User 등록 관련된 VIEW
	 */
	public void insertUser() throws Exception {
		System.out.println("\n====1. User 등록====\n");

		System.out.print("ID : ");
		String userId = sc.next();

		System.out.print("PW : ");
		String userPw = sc.next();

		System.out.print("Name : ");
		String userName = sc.next();

		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅

		User user = new User();

		// setter 이용
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);

		// 서비스 호출(INSERT) 후 결과 반환(int, DML은 결과 행의 갯수를 반환함) 받기
		int result = service.insertUser(user);
		// service 객체(UserService)에 있는 insertUser() 라는 이름의 메서드를 호출!

		// 반환된 결과에 따라 출력할 내용 선택
		if (result > 0) {
			System.out.println("\n" + userId + " 사용자가 등록되었습니다.\n");

		} else {
			System.out.println("\n***등록 실패***\n");
		}
	}

	/**
	 * 2. User 전체 조회 관련 View (SELECT)
	 */
	public void selectAll() throws Exception {
		System.out.println("\n====2. User 조회====\n");
		// 서비스 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<User> list = service.selectAll();

		if (list.isEmpty()) {
			System.out.println("존재하지 않습니다.");
			return;
		}

		for (User user : list) {
			System.out.println(user.toString());
		}
	}

	/**
	 * 3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT) 검색어 입력 : 유
	 */
	public void selectName() throws Exception {
		System.out.println("\n====3. 이름 조회====\n");

		System.out.print("검색어 입력 : ");
		String name = sc.next();

		List<User> userList = service.searchUser(name);

		if (userList.isEmpty()) {
			System.out.println("존재하지 않습니다.");
			return;
		}

	}

	/**
	 * 4. USER_NO를 입력 받아 일치하는 User 조회(SELECT) 딱 1행만 조회되거나 or 일치하는 것 못찾았거나
	 * 
	 * -- 찾았을 때 : User 객체 출력 -- 없을 때 : USER_NO가 일치하는 회원 없음
	 */
	public void selectUser() throws Exception {
		System.out.println("\n====4. 번호 조회====\n");

		System.out.print("유저 번호 입력: ");
		int number = sc.nextInt();

		User user = service.searchByNum(number);

		if (user != null)
			System.out.println(user.toString());
		else
			System.out.println("USER_NO가 일치하는 회원 없음");
	}

	/**
	 * 5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE) * DML이다!!
	 * 
	 * -- 삭제 성공했을 때 : 삭제 성공 -- 삭제 실패했을 때 : 사용자 번호가 일치하는 User가 존재하지 않음
	 */
	public void deleteUser() throws Exception {
		System.out.println("\n====5. 번호 입력 후 삭제===\n");

		System.out.print("유저 번호 입력 : ");
		int number = sc.nextInt();
		int result = service.deleteUser(number);

		if (result > 0) {
			System.out.println("\n***삭제 성공.***\n");

		} else {
			System.out.println("\n***사용자 번호가 일치하는 User가 존재하지 않음***\n");
		}
	}

	/**
	 * 6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)
	 */
	public void updateName() throws Exception {
		System.out.println("\n====6. 이름 수정===\n");

		System.out.print("ID 입력 : ");
		String id = sc.next();

		System.out.print("PW 입력 : ");
		String pw = sc.next();

		System.out.println("변경할 이름 : ");
		String name = sc.next();

		int result = service.updateUser(id, pw, name);

		if (result > 0) {
			System.out.println("\n***사용자가 수정되었습니다.***\n");

		} else {
			System.out.println("\n***수정이 실패하였습니다.***\n");

		}
	}

	/**
	 * 7. User 등록(아이디 중복 검사)
	 */
	public void insertUser2(int num) throws Exception {
		if (num == 7) {
			System.out.println("\n===7. 회원 등록===\n");
		}

		System.out.print("ID 입력 : ");
		String id = sc.next();

		System.out.print("PW 입력 : ");
		String pw = sc.next();

		System.out.println("이름 입력 : ");
		String name = sc.next();

		int result = service.addUser(id, pw, name);

		if (result > 0) {
			System.out.println("회원 등록 성공!");
		} else {
			System.out.println("중복된 사용자가 존재합니다.");
		}
	}

	/**
	 * 8. 여러 User 등록하기
	 */
	public void multiInsertUser() throws Exception {
		int input = 0;

		System.out.println("\n===8. 여러 User 등록하기===\n");
		System.out.print("인원 수 입력 : ");
		input = sc.nextInt();
		
		for(int i = 0; i < input; i++) {
			insertUser2(8);
		}
		
	}
}
