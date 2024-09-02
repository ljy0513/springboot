package com.study.tobySpringboot;


import com.study.tobySpringboot.controller.HelloController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class TobySpringbootApplication {

	public static void main(String[] args) {
		// SpringContainer 만들기
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBean(HelloController.class);
		context.refresh();

		//SpringApplication.run(TobySpringbootApplication.class, args);

		//HelloController helloController = new HelloController();

		// ServletContainer가 client의 요청을 받고 어떤 servlet을 동작시킬지 정한다.
		// 이때 요청은 Mapping url로 servlet을 결정한다.

		// Servlet 만들어보기
		// spring boot가 내장된 Tomcat을 쉽게 구동할 수 있도록 코드로 구현해 놓은 라이브러리
		// 최종 인터페이스인 WebServer를 구현한 여러 부모 클래스를 상속 받음
		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("frontController", new HttpServlet() {
				@Override
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
					/* 	FrontController의 역할 : Servlet Container의 Mapping 기능을 대신 담당
						가장 앞에서 모든 요청을 받음
						인증, 보안, 다국어 처리, 공통기능 등 우선 처리 가능 */

					if(req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
						//요청
						String name = req.getParameter("name");

						HelloController helloController = context.getBean(HelloController.class);
						// frontController로 모든 요청을 받고 적절한 오브젝트에 로직 처리를 위한 작업을 위임
						String ret = helloController.hello(name); // 요청받은 정보를 binding

						//응답
						resp.setStatus(HttpStatus.OK.value()); // == resp.setStatus(200);
						//resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE); // == resp.setHeader("Content-Type","text/plain");
						resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
						resp.getWriter().println(ret); // 오브젝트에서 처리한 결과값 응답
					}else if(req.getRequestURI().equals("/user")) {
						//
					}else {
						resp.setStatus(HttpStatus.NOT_FOUND.value());
					}

				}
			}).addMapping("/*");
		}); // Tomcat 외의 다른 웹 서버도 사용할 수 있도록 추상화함
		webServer.start();
	}

}
