package teamworks.server.service.Parsing;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import teamworks.server.domain.Product;
import teamworks.server.domain.ProductInfo;
import teamworks.server.domain.Store;
import teamworks.server.domain.Tag;
import teamworks.server.repository.ProductInfoRepository;
import teamworks.server.repository.ProductsRepository;
import teamworks.server.repository.StoresRepository;
import teamworks.server.repository.TagRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KRuokaParseService implements StoreParser {

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    StoresRepository storesRepository;

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    TagRepository tagRepository;

    @Override
    @Async("taskExecutor")
    public void parseProducts(Document doc, String store) {
//        ParsingResult result = new ParsingResult(store);
        System.out.println(Thread.currentThread() + "Parsing page");
        Elements els = doc.getElementsByAttributeValue("class", "product-result-item");

        for (Element foundProduct : els) {
            String name = findProductName(foundProduct);
            Double price = findProductPrice(foundProduct);
            String unit = findProductUnit(foundProduct);

            saveIfNotExist(name, price, unit, store);
        }
    }

    private String findProductName(Element el) {
        return el.getElementsByClass("product-result-name-content").first().child(0).child(0).text();
    }

    private Double findProductPrice(Element el) {
        String integerPart = el.getElementsByClass("price").first().child(0).text();
        String fractionPart = el.getElementsByClass("price").first().child(2).text();
        return Double.parseDouble(integerPart + "." + fractionPart);
    }

    private String findProductUnit(Element el) {
        return el.getElementsByClass("pricing-unit").first().text();
    }

    private void saveIfNotExist(String name, Double price, String unit, String store) {
        ProductInfo info;

//        System.out.println("Saving product");
        if (productInfoRepository.existsByName(name)) {
            info = productInfoRepository.findByName(name);
        } else {
            info = new ProductInfo(name);
//            tagRepository.save(new Tag(name));
//            tagRepository.findByName(name);
            productInfoRepository.save(info);
        }

        Store storeInDB;

        if (storesRepository.existsByName(store)) {
            storeInDB = storesRepository.findByName(store);
        } else {
            storeInDB = new Store(store);
            storesRepository.save(storeInDB);
        }

        if (!productsRepository.existsByInfo_NameAndStoreAndPrice(name, storeInDB, price)) {
            productsRepository.save(new Product(info, price, unit, storeInDB, "Finland"));
        } /*else if(productsRepository.existsByInfo_NameAndStore(name, storeInDB)) {
            productsRepository.delete(productsRepository.findByInfo_NameAndStore(name, storeInDB));
            productsRepository.save(new Product(info, price, unit, storeInDB, "Finland"));
        }*/
    }

    @Override
    public Map<String, String> parseStoresLinks(Document doc) {
        Map<String, String> links = new HashMap<>();
        Element storeList = doc.getElementsByClass("store-list").first();
        for (Element el : storeList.getElementsByClass("store-list-item")) {
            links.put(findHref(el), findStoreName(el));
        }
        return links;
    }

    private String findHref(Element el) {
        return el.attr("href");
    }

    private String findStoreName(Element el) {
        return el.getElementsByClass("store-list-item__name-and-hours").first().child(0).text();
    }

    @Override
    public List<String> parseProductCategories(Document doc) {
        List<String> categoryRelativeHrefs = new ArrayList<>();
        Elements categoryList = doc.getElementsByClass("product-category-list").first().children();
        for (Element el : categoryList) {
            String relativeHref = findHref(el.child(0));
            relativeHref = relativeHref.split("[/]")[relativeHref.split("[/]").length-1];
            relativeHref = "/" + relativeHref;
            categoryRelativeHrefs.add(relativeHref);
        }
        return categoryRelativeHrefs;
    }
}
