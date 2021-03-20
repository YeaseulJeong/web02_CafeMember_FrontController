package servlet.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.xdevapi.Session;

import servlet.model.MemberDAOImpl;
import servlet.model.MemberVO;

/**
 *  <servlet>
    <servlet-name>RegisterServlet</servlet-name>
    <servlet-class>servlet.controller.DispatcherServlet (FQCN)  </servlet-class>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>DispatcherServlet</servlet-name>
    <url-pattern>/front.do</url-pattern>
  </servlet-mapping>
  
  
 */
@WebServlet("/front.do")

// 비즈니스 로직 하나 처리하자고 용량이 큰 서블릿 하나를 각각 계속 만드는 건 비효율적이다 
//그래서 그전의 각각의 서블릿들에 해당하는 메서드들을 하나의 서블릿안에 구현하면 됨
// DispatcherServlet 은 command 를 먼저 확인해서 어떤 요청을 요구하는건지 if else 로 판단을 제일먼저 해줘야


public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DispatcherServlet() {
        super();
        // Servlet은 서버가 만든다  그래서 생성자가 있다 
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 결론적으로 모든 로직은 여기서 작성 한다 
		// service 쓰지 않는다 
		// 1. 요청이 어디서 들어왔는지 알아야 한다 command 값 받는다
		String command = request.getParameter("command");   // hidden으로 command 값을 받아서 각각 어떤 요청을 하는지 구분 
		String path = "index.jsp";   // 기본 페이지를 index페이지로 
		if(command.equals("find")) {
			path = find(request, response);
		}else if(command.equals("login")){
			path = login(request, response);
		}else if(command.equals("allmember")) {
			path = allMember(request, response);
		}else if(command.equals("logout")) {
			path = logout(request, response);
		}else if(command.equals("update")) {
			path = update(request, response);
		}else if(command.equals("register")) {
			path=register(request, response);
		}else if(command.equals("idcheck")) {
			path =idcheck(request, response);
		}
		
		request.getRequestDispatcher(path).forward(request, response);
	}	
		
		
	public String find (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// 예전의 FindServlet 이다 
		// 예전의 Servlet 여러개가 하나의 Servlet 의 여러 메서드로 들어간다
		//1. form값 받아서 
		String id = request.getParameter("id");
		
		String path= "find_fail.jsp";     // 기본 path 를 find_fail.jsp로 설정하고  if 안에서 조건을 충족하면 path 를 "find_ok.jsp"로 바꿔준 후 그렇게 설정된 path 를 return 함 
		
		//2. dao 메서드 호출. 이때 인자값으로 폼값 넣고 , 리턴값 받고
		MemberDAOImpl dao = MemberDAOImpl.getInstance();   
		//MemberDAOImpl dao = new MemberDAOImpl();이건 매번 객체를 만들지만  singletone은 이미 객체를 만들어 놓고  가져다 쓰는 것 
		// dao 에 있는 메서드를 쓰기 위해 매번 다른 Dao 객체를 만들어서 부를 필요 없다. 너무 많은 자원을 차지한다
		// 볼펜을 만들 때마다 공장을 만들 필요가 없다. 미리 만들어놓은 유일무이하게 공장 하나를 만들어 놓고 거기의 기능을 쓰는 것  
		try {
			MemberVO vo = dao.findByIdMember(id);
			if(vo!=null) {
				request.setAttribute("vo", vo);  //3. 바인딩
				// request.getRequestDispatcher("find_ok.jsp").forward(request, response);
				path = "find_ok.jsp";   // 여기서 네이게이션을 하는게 아니라 여기서는 path를 String 으로 리턴하고  doProcess 에서 직접 네비게이션
			}else {
				// request.getRequestDispatcher("find_fail.jsp").forward(request, response);
		}} catch(SQLException e) {
			
		}
		return path;
		
	}
	
		public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			// 예전의LoginServlet 이다 
			// 예전의 Servlet 여러개가 하나의 Servlet 의 여러 메서드로 들어간다
			//1. form값 받아서 
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			String path= "index.jsp";
			
			//2. dao 메서드 호출. 이때 인자값으로 폼값 넣고 , 리턴값 받고
			MemberDAOImpl dao = MemberDAOImpl.getInstance();
			try {
				MemberVO vo = dao.login(id, password);
				if(vo!=null) {
					HttpSession session = request.getSession();  // login 은 무조건 session 에 담아야 한다 
					session.setAttribute("vo", vo);
					
					path ="login_result.jsp";
//					RequestDispatcher rd =  request.getRequestDispatcher("login_result.jsp");
//					rd.forward(request, response);
				}else {
					// request.getRequestDispatcher("find_fail.jsp").forward(request, response);
			}} catch(SQLException e) {
				
			}
			
		return path;
	}
		
		
		public String allMember (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			
			String path = "index.jsp";
			try {
				ArrayList<MemberVO> list=MemberDAOImpl.getInstance().showAllMember();
				request.setAttribute("list", list);
				path = "allView.jsp";
			} catch (SQLException e) {
				
			}

			return path;
			
		}
		
		public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			//예전의 FindServlet
			//1. 폼값받아서
			HttpSession session = request.getSession();
			String path = "index.jsp";
			if(session.getAttribute("vo") !=null) {
				session.invalidate();
				path = "logout.jsp";
			}

			return path;
			
		}
		
		public String update (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			//예전의 FindServlet
			//1. 폼값받아서
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			
			String path = "index.jsp";
			MemberVO pvo = new MemberVO(id, password, name, address);
			
			try {
				MemberDAOImpl.getInstance().updateMember(pvo);
				
				HttpSession session  = request.getSession();
				if(session.getAttribute("vo") !=null) {
					session.setAttribute("vo", pvo); //중요
				}
				path = "update_result.jsp";
			}catch(Exception e) {
				
			}	

			return path;
			
		}
	
		public String register (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			
			String path = "index.jsp";
			
			MemberVO pvo  = new MemberVO(id, password, name, address);			
		
			try {
				MemberDAOImpl dao = MemberDAOImpl.getInstance();
				dao.registerMember(pvo);
				path = "front.do?command=allmember";
			} catch(SQLException e) {
				
			}
			return path;
		}
		
		public String idcheck (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			//예전의 FindServlet
			//1. 폼값받아서
			String id = request.getParameter("id");	
			String path = "idcheck.jsp";
			try {
				boolean flag=MemberDAOImpl.getInstance().idExist(id);			
				request.setAttribute("flag", flag);
				
			}catch(SQLException e) {
				System.out.println(e);
			}			
			return path;
		}
}

