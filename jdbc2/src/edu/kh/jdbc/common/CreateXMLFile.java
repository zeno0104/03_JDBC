package edu.kh.jdbc.common;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class CreateXMLFile {
	public static void main(String[] args) {
		// XML (eXtensible Markup Language) : 단순화된 데이터 기술 형식
		// XML에 저장되는 데이터 형식 Key : Value (Map)
		// -> Key, Value 모두 String(문자열) 형식
		
		// XML 파일을 읽고, 쓰기 위한 IO 관련된 클래스 필요
		
		// Properties 컬렉션 객체
		// - Map의 후손 클래스
		// - Key, Value 모두 String (문자열 형식)
		// - .xml 이나 .properties파일을 읽고, 쓰는데 특화된 메서드 제공
		
		Scanner sc = null;
		FileOutputStream fos = null;
		
		try {
			sc = new Scanner(System.in);
			
			System.out.print("생성할 파일 이름 : ");
			String fileName = sc.next();
			
			// Properties 객체 생성
			Properties prop = new Properties();
			
			// FileOutputStream 생성
			fos = new FileOutputStream(fileName + ".xml");
			
			// Properties 객체를 이용해서 XML 파일 생성
			prop.storeToXML(fos, fileName + ".xml 파일!!!");
			
			System.out.println(fileName + ".xml 파일 생성 완료");
			
		} catch (Exception e) {
			System.out.println("xml 파일 생성 중 예외 발생");
			e.printStackTrace();
		} finally {
			try {
				if(fos != null)
					fos.close();
				if(sc != null)
					sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}









