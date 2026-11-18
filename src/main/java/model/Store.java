package model;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private String name;              // 상점 이름
    private List<Product> listProd;   // 상품 리스트

    // 기본 생성자 추가
    public Store() {
        this.name = "";  // 상점 이름 초기화 (필요한 초기값을 설정)
        this.listProd = new ArrayList<>();  // 비어있는 상품 리스트 초기화
    }

    // 기존 생성자
    public Store(String name, List<Product> listProd) {
        this.name = name;
        this.listProd = listProd;
    }

    // Getter / Setter
    public List<Product> getListProd() {
        return listProd;
    }

    public String getName() {
        return name;
    }

    // 상품 추가
    public void add(Product prod) {
        listProd.add(prod);
    }

    // 특정 상품 삭제
    public void remove(int index) {
        listProd.remove(index);
    }

    // 상품 ID로 검색
    public Product getProductById(int id) {
        for (Product prod : listProd) {
            if (prod.getProductId() == id) {
                return prod;
            }
        }
        return null;
    }

    // 전체 상품 목록 초기화
    public void clear() {
        listProd.clear();
    }
}
