package kr.openbase.adcsmart.web.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFilter implements Filter {
	private transient Logger log = LoggerFactory.getLogger(SessionFilter.class);
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		AdcSession session = new AdcSession(request.getSession());
		String uri = request.getRequestURI();
		String path = uri.replaceFirst(request.getContextPath(), "");

		response.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT");
		response.setHeader("Last-Modified", new Date().toString());
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");

		log.debug("[SessionFilter] request PATH: {}", path);
		log.debug("[SessionFilter] request Session: {}", session.getAccountId());

//		System.out.println("before : " + request.getSession().isNew());
//		System.out.println("before : " + request.getSession().getId());
//		System.out.println("before : [" + request.getContextPath()+"]");
//		System.out.println("before : ["+session.getAccountId()+"]");
		
//		if (request.getContextPath() == "")
//		    System.out.println("aaa");
//		else if (request.getContextPath().isEmpty())
//		    System.out.println("bbb");
		
//		if (request.getSession().isNew() == true)
//		    session.setAccountId(null);
//		if(request.getSession().isNew() == true)
		if(request.getSession().isNew() == true)
		{
		    log.debug("\tsend redirect login page(index.action). ");
            PrintWriter out = response.getWriter();
            out.print("<script>\n");
            out.print("   window.location.href = '" + request.getContextPath() + "/index.html';\n");
            out.print("</script>\n");
            out.flush();
            out.close();
		}
		
		if (session.getAccountId() != null || path.equals("/index.html")
				|| path.equals("/member/login.action") || path.equals("/member/retrieveCookies.action")
				|| path.equals("/member/retrieveLangCode.action"))  
		{
			log.debug("\tsend to next filter. ");
			filterChain.doFilter(servletRequest, servletResponse);
		} 
		else 
		{
			log.debug("\tsend redirect login page(index.action). ");
			PrintWriter out = response.getWriter();
			out.print("<script>\n");
			out.print("   window.location.href = '" + request.getContextPath() + "/index.html';\n");
			out.print("</script>\n");
			out.flush();
			out.close();
		}

	}

}
