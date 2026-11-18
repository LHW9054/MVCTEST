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

@WebServlet("/AddProdServlet")
public class AddProdServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // パラメータ取得
        request.setCharacterEncoding("UTF-8");
        int productId = Integer.parseInt(request.getParameter("productId"));  // 상품 ID 받아오기
        int productIdx = Integer.parseInt(request.getParameter("productIdx"));  // 카테고리 IDX 받아오기
        System.out.println("AddProdServlet: 상품 추가 요청, 상품 ID - " + productId + ", 카테고리 IDX - " + productIdx);
        
        
        System.out.println("productId - " + productId);
        System.out.println("productIdx - " + productIdx);

        // セッションオブジェクト取得
        HttpSession session = request.getSession();
        
        // カートへの商品追加処理
        Moperation op = new Moperation();
        op.addProd(productId, productIdx, session);  // 상품 ID와 카테고리 IDX를 함께 전달

        // 転送先設定
        String url = "cart.jsp";
        System.out.println("AddProdServlet: 상품 추가 후 카트 페이지로 리다이렉트");

        // 転送
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }
}
