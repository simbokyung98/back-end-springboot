package com.mycompany.backend.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.backend.dto.Board;
import com.mycompany.backend.dto.Member;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/rest")
public class RestControllerTest {
   
   @GetMapping("/getObject")
   public Board getObject() {
       log.info("실행");
       Board board = new Board();
       board.setBno(1);
       board.setBtitle("제목");
       board.setBcontent("내용");
       board.setMid("user");
       board.setBdate(new Date());
       return board;
   }
   
   @GetMapping("/getMap")
   public Map<String, Object> getMap() {
      log.info("실행");
         
      Map<String, Object> map = new HashMap<>();
      map.put("name", "홍길동");
      map.put("age", 25);
      
      Board board = new Board();
      board.setBno(1);
      board.setBtitle("제목");
      board.setBcontent("내용");
      board.setMid("user");
      board.setBdate(new Date());
      map.put("board", board);
      
      return map;
   }
   
   @GetMapping("/getArray")
   public String[] getArray() {
	   log.info("실행");
	   String[] array = {
			   "Java", "Spring", "Vue", "JavaScript"
	   };
	   return array;
   }
   
   @GetMapping("/getList1")
   public List<String> getList1() {
	   log.info("실행");
	   List<String> list = new ArrayList<String>();
	   list.add("Java");
	   list.add("Spring");
	   list.add("Vue");
	   return list;
   }
   @GetMapping("/getList2")
   public List<Board> getList2() {
      log.info("실행");
      List<Board> list = new ArrayList<>();
      for(int i=1; i<=3; i++) {
         Board board = new Board();
         board.setBno(i);
         board.setBtitle("제목" + i);
         board.setBcontent("내용" + i);
         board.setMid("user");
         board.setBdate(new Date());
         list.add(board);
      }
      return list;
   }

   @GetMapping("/useHttpServletResponse")
   public void getHeader(HttpServletResponse response) throws IOException {
	   					//저급 API
	   
	   response.setContentType("application/json; charser=UTF-8");
	   response.addHeader("TestHeader", "value");
	   
	   Cookie cookie = new Cookie("refreshToken", "xzxxxxxx");
	   response.addCookie(cookie);
	   
	   PrintWriter pw = response.getWriter();
	   JSONObject jsonpObject = new JSONObject();
	   jsonpObject.put("result", "success");
	   String json = jsonpObject.toString();
	   
	   pw.println(json);
	   pw.flush();
	   pw.close();
	  
   }
   
   @GetMapping("/useResponseEntity")
   public ResponseEntity<String> useResponseEntity(){
	   	
		/* //StringBuilder [잘 쓰진 않지만 이런 방식도 있음]
		 StringBuilder sb = new StringBuilder();
		 sb.append("A");
		 sb.append("B");
		 sb.append("c");
		 String result = sb.toString();
		 
		 String result2 = new StringBuilder()
			   				.append("A")
			   				.append("B")
			   				.append("C")
			   				.toString();*/
	   
	   
	   
		/* //ResponseEntity<String>를 return이 아니게 생성하기
		 * BodyBuilder bodyBuilder = ResponseEntity.ok();
		 ResponseEntity<String> result = bodyBuilder.body("success");
		 return result;
		 */
	   
	   JSONObject jsonpObject = new JSONObject();
	   jsonpObject.put("result", "success");
	   String json = jsonpObject.toString();
	   
	   String cookieStr =  ResponseCookie.from("refreshToken", "xxx")
	   									.build()
	   									.toString();
	   
	   //기본 형식이 test/plain 형식이라 json 자동 파싱 안됨.
	   // -> header을 .header("name", "value") 에서 .header(HttpHeaders.CONTENT_TYPE, "application/json")으로 변경해야함
	   return ResponseEntity.ok()
			   				.header(HttpHeaders.CONTENT_TYPE, "application/json")
			   				.header("TestHeader", "value")
			   				.header(HttpHeaders.COOKIE,cookieStr)
			   				.body(json);
	   
	   
   }
   
   @RequestMapping("/sendQueryString")
   public Member sendQueryString(Member member) {
	   return member;
   }
   
   @PostMapping("/sendJson")	///바디에 있는 json을 바로 파싱해서 받을 수 있게 함
   public Member sendJson(@RequestBody Member member) {
	   return member;
   }
   
   @PostMapping("/sendMultipartFormData")
   public Map<String, String> sendMultipartFormData(String title, MultipartFile attach) throws Exception{
	   String saveFile = new Date().getTime() + attach.getOriginalFilename();
	   attach.transferTo(new File("C:/Temp/uploadfiles/"+saveFile));
	   
	   Map<String ,String> map = new HashMap<String, String>();
	   map.put("result", "success");
	   map.put("savaFile", saveFile);
	   return map;
   }
   
   
}