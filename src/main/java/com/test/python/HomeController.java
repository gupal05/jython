package com.test.python;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static PythonInterpreter intPre;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "index";
	}
	
	@RequestMapping("/test")
	 public String pyInfo() {
        // Jython 환경에서 site 모듈을 사용하지 않도록 설정
        // jython-standalone이 아닌 경우 site 모듈 에러 해결 방안
        System.setProperty("python.import.site", "false");

        // ClassPathResource를 사용하여 리소스 파일의 경로를 가져옴
        Resource resource = new ClassPathResource("static/python/test.py");
        String scriptPath = ""; // 스크립트 파일의 절대 경로를 저장할 변수
        try {
            // Resource 객체를 File 객체로 변환
            File file = resource.getFile();
            // 스크립트 파일의 절대 경로를 얻음
            scriptPath = file.getAbsolutePath();
        } catch (IOException e) {
            // 파일을 읽는 중 오류 발생 시 스택 트레이스 출력 및 에러 메시지 반환
            e.printStackTrace();
            return "Error retrieving script path";
        }

        // PythonInterpreter 객체를 생성하여 Python 스크립트를 실행
        PythonInterpreter intPre = new PythonInterpreter();
        // Python 스크립트 파일을 실행
        intPre.execfile(scriptPath);
        // Python 스크립트에서 정의된 함수 testFunc() 호출하여 반환값을 'a'에 저장
        intPre.exec("a = testFunc()");  // Python에서 배열을 가져오기
        // 'a' 변수의 값을 PythonInterpreter에서 가져옴
        PyObject result = intPre.get("a");  // Python에서 반환된 배열을 가져오기

        // 배열의 길이만큼 반복하여 각 요소를 출력
        for (int i = 0; i < result.__len__(); i++) {
            // 배열의 i번째 요소를 가져와 출력
            System.out.println(result.__getitem__(i));
        }

        // 메소드가 완료된 후 반환할 뷰 이름 또는 경로
        return "pythonpj/pyInfo";
	}

	
}
