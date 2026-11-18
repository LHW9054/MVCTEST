<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Cart" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>カート内一覧</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <%@include file="header-navi.jsp" %>

    <div class="cart-container">
    <h2 class="cart-title">カート</h2>
    <div class="cart-items">
        <%
            List<Product> listProd;
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                listProd = new ArrayList<Product>();
            } else {
                listProd = cart.getListProd();
            }

            if (listProd.size() > 0) {
                for (int idx = 0; idx < listProd.size(); idx++) {
                    Product prod = listProd.get(idx);
        %>
                    <div class="cart-item">
                        <div class="cart-item-info">
                            <h3><%= prod.getProductName() %></h3>
								<p class="cart-price"><%= String.format("%,d", prod.getProductPrice()) + "円" %></p>
                        </div>
                        <form action="remove-prod-servlet" method="POST">
                            <input type="hidden" name="idx" value="<%= idx %>">
                            <button type="submit" class="remove-btn">削除</button>
                        </form>
                    </div>
        <%
                }
        %>
        <%
            } else {
        %>
                <p class="empty-cart">カートの中は空です。</p>
        <%
            }
        %>
    </div>
    <div class="checkout-container"> <!-- 결제 가격과 버튼을 포함하는 컨테이너 -->
        <div class="total">総金額: <%= cart.getTotalPriceString() %></div>
        <div class="checkout">
            <form action="pay-servlet" method="post">
                <button type="submit" class="checkout-btn">精算</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
