package com.java.www.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.java.www.dto.User_campDto;
import com.java.www.service.EmailService;
import com.java.www.service.User_campService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("my")
public class MyController {
	
	@Autowired User_campService userCampService;
	
	@Autowired HttpSession session;
	
	@Autowired
	EmailService emailService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}// index()
	
	@GetMapping("login")
	public String login() {
		return "/my/login";
	}// login()
	
	@GetMapping("logout")
	public String logout() {
		session.invalidate();
		return "/my/logout";
	}// logout()
	
	@RequestMapping("doLogin")
	public String doLogin(User_campDto ucdto, Model model, HttpServletRequest request) {
		int result = 0;
		System.out.println("FC id : "+ucdto.getId());
		System.out.println("FC pw : "+ucdto.getPw());
		
		User_campDto usercampDto = userCampService.loginSelect(ucdto);
		if(usercampDto != null) {
			session.setAttribute("session_id", usercampDto.getId());
			session.setAttribute("session_name", usercampDto.getName());
			
			result = 1;
		}else {
			System.out.println("FC userDto : null");
		}
		
		model.addAttribute("result", result);
		return "/my/doLogin";
	}// doLogin()
	
	//////////////////////////////////////////로그인(id, pw 찾기)
	//id 찾기
	@GetMapping("idpw_search") //idsearch페이지열기
	public String idpw_search() {
		return "/my/idpw_search";
	}// idpw_search()
	
	@PostMapping("idSearch") //ajax 아이디찾기- name,email
	@ResponseBody
	public String idSearch(String name, String email) {
		System.out.println("FC idsearch name : "+name);
		System.out.println("FC idsearch email : "+email);
		User_campDto usercampDto = userCampService.idsearch(name,email);
		String result = "";
		String tempId=""; //임시아이디
		if(usercampDto!=null) {
			tempId = usercampDto.getId().substring(0,usercampDto.getId().length()-2);
			tempId += "**";
			System.out.println("찾은 아이디 : "+tempId);
			result = tempId;
		}else {
			result="fail";
		}
					
		return result;
	}// idSearch()

	//id 찾기완료
	@GetMapping("idsearch")
	public String idsearch() {
		return "/my/idsearch";
	}// login()
	
	//비밀번호 찾기
	@PostMapping("pwsearch")
	@ResponseBody
	public String pwsearch(String id, String email) {
		System.out.println("MyController id : "+id);
		System.out.println("MyController email : "+email);
		//service연결 비밀번호 찾기-아이디,이메일검색
		String result = userCampService.pwsearch(id,email);
		return result;
	}
	
	@PostMapping("email")
	@ResponseBody
	public String email(String email) {
		System.out.println("MController email : "+email);
		
		//service연결 - 이메일주소 보냄.
		String result = emailService.mailSend(email);
		return result;
	}
	
	@PostMapping("pwChk") //인증코드 확인
	@ResponseBody
	public String pwChk(String pwcode) {
		System.out.println("MController pwcode : "+pwcode);
		String pw = (String) session.getAttribute("email_pwcode");
		String result ="fail";
		if(pw.equals(pwcode)) result="success";
		
		return result;
	}
	//pw 찾기완료
	@GetMapping("pwsearch")
	public String pwsearch() {
		return "/my/pwsearch";
	}// login()
	
	//==================================회원가입
	//회원가입 페이지
	@GetMapping("signUp")
	public String signUp() {
		return "/my/signUp";
	}// signUp()
	
	//회원가입 저장
	@PostMapping("signUp")
	public String signUp(User_campDto ucdto, @RequestPart MultipartFile file, Model model, 
			String mail_id, String mail_tail,
			String address1, String address2,
			String f_tell, String m_tell, String l_tell) throws Exception {
		String email = mail_id+"@"+mail_tail;
		String address = address1+"　"+address2;
		String phone =  f_tell+"-"+m_tell+"-"+l_tell;
		
		ucdto.setEmail(email);
		ucdto.setAddress(address);
		ucdto.setPhone(phone);
		
		//파일업로드 정보 - 파일저장위치
		String fileUrl = "c:/upload/";
		String mfileName = "";
		System.out.println("이름 : "+ucdto.getName());
		
		//파일첨부가 되었는지 확인
		if(!file.isEmpty()) {
			int i = 0;
				String orgfileName = file.getOriginalFilename();
				long time = System.currentTimeMillis();
				String uploadFileName = time+"_"+orgfileName;
				System.out.println("파일이름 : "+uploadFileName);
				
				//파일업로드 - 파일이 c:/upload폴더에 추가됨.
				File f = new File(fileUrl+uploadFileName);
				file.transferTo(f);
				
				if(i==0) mfileName += uploadFileName;
				else mfileName +=","+uploadFileName;
				
				i++;
				
		}//if
		
		//파일첨부가 없으면 빈공백, 1.jpg
		ucdto.setM_img(mfileName);
		System.out.println("최종이름 : "+mfileName);
		
		//회원가입 저장 service호출
		userCampService.signUpinsert(ucdto);
		
		model.addAttribute("signUp","success");
		
		return "/my/signUp02";
	}// signUp()
	
	@PostMapping("idCheck")
	@ResponseBody
	public String idCheck(String id) {
		String result = userCampService.idCheck(id);
		return result;
	}
	
	//회원가입 완료
	@GetMapping("signUp02")
	public String signUp02() {
		return "/my/signUp02";
	}// signUp02()
	
	@RequestMapping("myPage")
	public String myPage() {
		return "/my/myPage";
	}// myPage()
	
}
