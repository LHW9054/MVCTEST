package control;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Moperation;

@WebServlet("/login-servlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストパラメータの取得
        request.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("userId");
        String userpw = request.getParameter("userpw");

        // 입력된 사용자 아이디와 비밀번호 출력
        System.out.println("LoginServlet - 입력된 userId: " + userId);
        System.out.println("LoginServlet - 입력된 userpw: " + userpw);

        // 로그인 처리
        HttpSession session = request.getSession();    // セッションオブジェクト取得
        Moperation op = new Moperation();
        boolean result = op.loginProc(userId, userpw, session);

        // 로그인 처리 후 출력
        System.out.println("LoginServlet - 로그인 처리 결과: " + (result ? "성공" : "실패"));

        // 転送先設定
        String url = "select.jsp";  // 기본적으로 select.jsp로 리다이렉션
        if (!result) {               // 로그인 실패 시 에러 메시지와 함께 login.jsp로 리다이렉션
            System.out.println("LoginServlet - 로그인 실패, 에러 메시지 설정");
            request.setAttribute("errorMsg", "ユーザID または パスワードに 誤りがあります。");
            url = "login.jsp";
        } else {
            // 로그인 성공 시 세션에 사용자 정보를 설정 (예: 사용자 ID)
            System.out.println("LoginServlet - 로그인 성공, 세션에 사용자 ID 저장");
            System.out.println("session -> userid : " + userId);
            session.setAttribute("userId", userId);
            
        }

        // 전송 URL 출력
        System.out.println("LoginServlet - 최종 전송 URL: " + url);

        // 転送
        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }
}
