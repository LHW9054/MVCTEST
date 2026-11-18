package control;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Moperation;

@WebServlet("/join-servlet")
public class JoinServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 文字コード設定
        request.setCharacterEncoding("UTF-8");
        System.out.println("JoinServlet: 요청 처리 시작");

        // 아이디 중복 확인만 요청된 경우
        if ("true".equals(request.getParameter("checkIdOnly"))) {
            String userId = request.getParameter("userId");
            Moperation op = new Moperation();
            boolean checkResult = op.checkIdDuplication(userId);
            response.setContentType("text/plain");
            System.out.println("JoinServlet DB에서 받아온 결과 :" + checkResult);
            if (checkResult) {
                response.getWriter().write("unavailable"); // 중복된 ID
            } else {
                response.getWriter().write("available"); // 사용 가능한 ID
            }
            return;  // 아이디 중복 확인만 처리하고 종료
        }

        // 회원가입 처리
        String userId = request.getParameter("userId");
        String userpw = request.getParameter("userpw");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Moperation op = new Moperation();
        boolean result = op.registerUser(userId, userpw, username, email, phone);

        // 결과에 따른 페이지 이동
        String url = "login.jsp";
        if (!result) {
            // 에러 발생 시 회원가입 페이지로 다시 이동
            request.setAttribute("errorMsg", "会員登録に失敗しました。もう一度お試しください。");
            url = "join.jsp";
            System.out.println("JoinServlet: 회원가입 실패");
        } else {
            System.out.println("JoinServlet: 회원가입 성공");
        }

        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }
}
