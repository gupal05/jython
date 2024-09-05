package com.test.python;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
//	@GetMapping(value = "/test")
//	public String test() {
//		System.out.println("tttt");
//		return "test";
//	}
	
	@RequestMapping("/test")
	public String pyInfo() {
      System.setProperty("python.import.site", "false"); // jython-standalone이 아닐 경우 site 모듈 에러 해결 방안
	  intPre = new PythonInterpreter();
	  intPre.execfile("D:\\Dev\\project\\sts-workspace\\py_test\\src\\main\\resources\\static\\python\\test.py");
	  //intPre.exec("print(testFunc(5,10))"); // testFunc(a,b) 실행
	  
	  intPre.exec("a = testFunc(5,10)");
	  PyObject result = intPre.get("a");  //변수를 받기
	  System.out.println(result.toString());
	  
	  
	  
	  return "pythonpj/pyInfo";
	}
	
}
