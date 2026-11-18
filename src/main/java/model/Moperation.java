package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import utils.DBUtil;

public class Moperation {
    
	public boolean loginProc(String userId, String userpw, HttpSession session) {
	    System.out.println("Moperation: 로그인 처리 시작");

	    String sql = "SELECT userpw FROM user_table WHERE userid = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, userId);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            String dbPassword = rs.getString("userpw");

	            if (dbPassword.equals(userpw)) {
	                // ✅ 장바구니 초기화
	                Cart cart = new Cart(userId, new ArrayList<>());
	                session.setAttribute("cart", cart);

	                // ✅ Store 객체 생성 후 상품 목록을 DB에서 불러옴
	                Store store = new Store();
	                String productSql = "SELECT product_id, product_name, product_price, product_idx FROM product_table";

	                try (PreparedStatement prodStmt = conn.prepareStatement(productSql);
	                     ResultSet prodRs = prodStmt.executeQuery()) {

	                    while (prodRs.next()) {
	                        int id = prodRs.getInt("product_id");
	                        String name = prodRs.getString("product_name");
	                        int price = prodRs.getInt("product_price");
	                        int categoryId = prodRs.getInt("product_idx");

	                     // 기존 Product 생성자에 맞게 수정
	                        Product product = new Product(categoryId, id, 1, name, "M", "default.jpg", price);
	                        store.add(product);  // ✅ Store에 상품 추가
	                    }
	                }

	                session.setAttribute("store", store);
	                System.out.println("Moperation: 로그인 후 Store 초기화 완료, 상품 개수: " + store.getListProd().size());

	                return true;
	            } else {
	                System.out.println("Moperation: 비밀번호 불일치");
	                session.setAttribute("loginError", "パスワードが間違っています。");
	            }
	        } else {
	            System.out.println("Moperation: 사용자 아이디 없음");
	            session.setAttribute("loginError", "ユーザーIDが見つかりません。");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        session.setAttribute("loginError", "ログイン処理中にエラーが発生しました。");
	    }

	    return false;
	}


	// 카테고리별 상품 목록을 가져오는 메서드
    public List<Product> getProductsByCategory(int categoryId) {
        System.out.println("상품 목록을 가져오는 중... 카테고리 ID: " + categoryId);  // 카테고리 ID 확인

        // 상품 목록을 담을 리스트 초기화
        List<Product> productList = new ArrayList<>();
        
        // DB에서 카테고리별 상품 정보를 가져오는 SQL 쿼리 작성
        String sql = "SELECT product_idx, product_id, product_amount, product_name, product_size, product_pic, product_price FROM product_table WHERE product_idx = ?";
        
        try (Connection conn = DBUtil.getConnection();  // DB 연결
             PreparedStatement stmt = conn.prepareStatement(sql)) {  // PreparedStatement로 쿼리 준비
            
            stmt.setInt(1, categoryId);  // 카테고리 ID를 쿼리에 전달 (첫 번째 ?에 categoryId 값 설정)
            ResultSet rs = stmt.executeQuery();  // SQL 실행하여 결과 가져오기

            // 결과셋에서 데이터를 하나씩 꺼내어 Product 객체에 담기
            while (rs.next()) {
                int productIdx = rs.getInt("product_idx");
                int productId = rs.getInt("product_id");
                int product_amount = rs.getInt("product_amount");
                String productName = rs.getString("product_name");
                String productSize = rs.getString("product_size");
                String productPic = rs.getString("product_pic");
                int productPrice = rs.getInt("product_price");

                // Product 객체 생성 (DB에서 가져온 데이터를 이용하여 생성)
                Product product = new Product(productIdx, productId, product_amount, productName, productSize, productPic, productPrice);
                
                // 생성된 Product 객체를 리스트에 추가
                productList.add(product);
            }
        } catch (SQLException e) {  // SQLException 예외 처리
            e.printStackTrace();
        }

        // 완성된 상품 목록 리스트 반환
        return productList;
    }
	    
    public class MakeStore {

        public Store makeStore(Moperation mOperation) {
            System.out.println("Moperation: 상점 정보 생성");

            // 카테고리별로 상품 목록을 가져옵니다. 각각 셔츠, 바지, 기타 카테고리
            List<Product> shirtList = mOperation.getProductsByCategory(1);  // 셔츠 카테고리
            List<Product> pantsList = mOperation.getProductsByCategory(2);  // 바지 카테고리
            List<Product> otherList = mOperation.getProductsByCategory(3);  // 기타 카테고리

            // 상품 목록을 하나의 리스트로 합칩니다.
            List<Product> productList = new ArrayList<>();
            productList.addAll(shirtList);  // 셔츠 목록 추가
            productList.addAll(pantsList);  // 바지 목록 추가
            productList.addAll(otherList);  // 기타 목록 추가

            // Store 객체 생성 (상점 이름과 상품 목록을 전달하여 상점 생성)
            Store store = new Store("My Store", productList);
            
            return store;
        }
    }

    // 로그아웃 처리
    public void logoutProc(HttpSession session) {
        System.out.println("Moperation: 로그아웃 처리 시작");
        session.invalidate();
        System.out.println("Moperation: 세션 무효화 완료");
    }
////////////////////////////////////////////////////////////////////////////////////////////
    public void addProd(int productId, int productIdx, HttpSession session) {
        System.out.println("Moperation: 상품 추가 처리 시작, 상품 ID - " + productId + ", 카테고리 IDX - " + productIdx);

        // 세션에서 store와 cart를 가져옵니다
        Store store = (Store) session.getAttribute("store");
        Cart cart = (Cart) session.getAttribute("cart");
        
        System.out.println("productId  - " + productId);
        System.out.println("productIdx  - " + productIdx);


        // store가 세션에 있는지 확인
        if (store == null) {
            System.out.println("Moperation: 세션에 store 객체가 없습니다. store를 세션에 추가해주세요.");
        } else {
            System.out.println("상품 목록 크기: " + store.getListProd().size());
        }

        // cart가 세션에 있는지 확인
        if (cart == null) {
            System.out.println("Moperation: 세션에 cart 객체가 없습니다. cart를 세션에 추가해주세요.");
        } else {
            System.out.println("현재 장바구니: " + cart);
        }

        if (store != null && cart != null) {
            // 상품 ID를 기반으로 상품을 찾습니다
            Product selectedProduct = null;
            for (Product prod : store.getListProd()) {
                System.out.println("상품 비교: " + prod.getProductId() + " == " + productId);
                if (prod.getProductId() == productId) {
                    selectedProduct = prod;
                    break;
                }
            }

            // 상품을 찾았는지 여부 확인
            if (selectedProduct != null) {
                // 상품에 카테고리 IDX를 설정
                selectedProduct.setProductIdx(productIdx);  // 카테고리 IDX를 설정

                // 상품을 장바구니에 추가
                cart.add(selectedProduct);  
                session.setAttribute("cart", cart);  // 장바구니 업데이트
                
                System.out.println("productId  - " + productId);
                System.out.println("productIdx  - " + productIdx);
                
                System.out.println("Moperation: 장바구니에 상품 추가 성공 - " + selectedProduct.getProductName());
            } else {
                System.out.println("Moperation: 해당 상품을 찾을 수 없음");
            }
        } else {
            System.out.println("Moperation: 상점 또는 장바구니 정보가 없음");
        }
    }


    public void removeProd(int productId, HttpSession session) {
        System.out.println("Moperation: 상품 삭제 처리 시작, 상품 ID - " + productId);

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            // 장바구니에서 상품 삭제
            cart.remove(productId);
            session.setAttribute("cart", cart);  // 장바구니 업데이트
            System.out.println("Moperation: 장바구니에서 상품 삭제 성공");
        } else {
            System.out.println("Moperation: 장바구니 정보가 없음");
        }
    }

    public void pay(HttpSession session) {
        System.out.println("Moperation: 결제 처리 시작");

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            // 결제 정보 세션에 저장
            session.setAttribute("pay", cart);

            // 새로운 장바구니 생성
            Cart newCart = new Cart(cart.getUserId(), new ArrayList<>());  // 빈 장바구니
            session.setAttribute("cart", newCart);  // 새로운 장바구니로 세션 업데이트
            System.out.println("Moperation: 결제 완료, 새로운 장바구니 생성");
        } else {
            System.out.println("Moperation: 장바구니 정보가 없음");
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////
    // 아이디 중복 체크
    public boolean checkIdDuplication(String userId) {
        String sql = "SELECT COUNT(*) FROM user_table WHERE userid = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // 중복이면 true, 아니면 false
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // 예외 처리나 오류시 기본적으로 중복으로 처리하지 않음
    }

    // 회원가입 처리
    public boolean registerUser(String userId, String userpw, String username, String email, String phone) {
        System.out.println("Moperation: 회원가입 처리 시작");

        String sql = "INSERT INTO user_table (userid, userpw, username, email, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, userpw);
            stmt.setString(3, username);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            
            System.out.println("userId = " + userId);
            System.out.println("userpw = " + userpw);
            System.out.println("username = " + username);
            System.out.println("email = " + email);
            System.out.println("phone = " + phone);
            
            int rows = stmt.executeUpdate();
            System.out.println("Moperation: 회원가입 처리 결과 - " + (rows > 0 ? "성공" : "실패"));
            return rows > 0; // 삽입 성공하면 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
////////////////////////////////////////////////////////////////////////////////////////////