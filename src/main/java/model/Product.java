package model;

/**
 * 商品を表すクラス
 * @author M.Takahashi
 */
public class Product {
    private int productIdx;    // 카테고리 구분 ID (1: 셔츠, 2: 바지, 3: 기타)
    private int productId;     // 상품 ID (각 상품을 구별)
    private int amount;        // 상품 수량
    private String productName;  // 상품 이름
    private String productSize;  // 상품 사이즈
    private String productPic;   // 상품 이미지 경로
    private int productPrice;    // 상품 가격

    // 생성자
    public Product(int productIdx, int productId, int amount, String productName, String productSize, String productPic, int productPrice) {
        this.productIdx = productIdx;
        this.productId = productId;
        this.amount = amount;
        this.productName = productName;
        this.productSize = productSize;
        this.productPic = productPic;
        this.productPrice = productPrice;
    }

    // Getter / Setter 메서드
    public int getProductIdx() {
        return productIdx;
    }

    public void setProductIdx(int productIdx) {
        this.productIdx = productIdx;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    // 가격 반환 메서드
    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
