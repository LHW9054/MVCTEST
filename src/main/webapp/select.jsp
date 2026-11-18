<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ page import="model.Product" %>
<%@ page import="model.Moperation" %>
<%@ page import="java.util.List" %>

<html>
<head>
<meta charset="UTF-8">
<title>商品ページ</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
    <%@ include file="header-navi.jsp" %>
    
    <h3 style="font-size: 2em; text-align: center; margin: 20px 0; color: yellow;">商品目録</h3>
    
    <div class="category-buttons" style="text-align: center; margin: 20px;">
        <form action="<%= request.getContextPath() %>/select.jsp" method="GET">
            <button class="button" type="submit" name="category" value="1">シャツ</button>
            <button class="button" type="submit" name="category" value="2">パンツ</button>
            <button class="button" type="submit" name="category" value="3">その他</button>
        </form>
    </div>

    <div class="product-container">
    <%
    System.out.println("select.jsp가 실행되었습니다.");
        // カテゴリ値を取得します
        String category = request.getParameter("category");
        if (category == null) {
            category = "1"; // 基本的に「シャツ」カテゴリに設定します
        }

        // カテゴリ別フォルダパス設定
        String imageFolder = "";
        switch (category) {
            case "1": imageFolder = "シャツ"; break;
            case "2": imageFolder = "パンツ"; break;
            case "3": imageFolder = "その他"; break;
        }

        // Moperationオブジェクト
        Moperation mOperation = new Moperation();

        // DBから該当カテゴリの商品リストを取得します
        List<Product> products = mOperation.getProductsByCategory(Integer.parseInt(category));

        // セッションに商品リストを保存
        if (products != null) {
            session.setAttribute("products", products);
        }
        // セッションからカテゴリ別の商品リストを取得
        products = (List<Product>) session.getAttribute("products");

        if (products != null) {
            for (Product product : products) {
            	String imagePath = request.getContextPath() + "/resource/image/" + imageFolder + "/" + product.getProductPic() + ".png";
    %>
    <div class="product-card">
    <img src="<%= imagePath %>" alt="<%= product.getProductName() %>" class="product-img">
    <div class="product-info">
        <h3><%= product.getProductName() %></h3>
        <p>価格: ¥<%= product.getProductPrice() %></p>
        <form action="AddProdServlet" method="POST">
            <input type="hidden" name="productId" value="<%= product.getProductId() %>">  <!-- 상품 ID 전달 -->
            <input type="hidden" name="productIdx" value="<%= product.getProductIdx() %>"> <!-- 카테고리 IDX 전달 -->
            <button type="submit" class="select-btn">選択</button>
        </form>
    </div>
</div>

    <% 
            }
        } else {
            System.out.println("select.jsp - 상품 목록이 없습니다.");
        }
    %>

<jsp:include page="footer.jsp" />
</body>


</html>
