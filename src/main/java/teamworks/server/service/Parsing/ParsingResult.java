package teamworks.server.service.Parsing;

import teamworks.server.domain.Product;
import teamworks.server.domain.Store;

import java.util.List;
import java.util.ArrayList;

public class ParsingResult {
    private List<Product> products;
    private Store store;

    public ParsingResult(Store store) {
        this.store = store;
        this.products = new ArrayList<>();
    }

    public ParsingResult(List<Product> products, Store store) {
        this.products = products;
        this.store = store;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
