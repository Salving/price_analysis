package teamworks.server.service.Parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import teamworks.server.domain.Product;
import teamworks.server.domain.ProductInfo;
import teamworks.server.domain.Store;
import teamworks.server.repository.ProductInfoRepository;
import teamworks.server.repository.ProductsRepository;
import teamworks.server.repository.StoresRepository;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.Math.pow;
import static java.lang.Thread.sleep;

@Service
public class KRuokaParseServiceDeprecated {
    private Map<String, String> urls;
    private List<String> productCategories;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    StoresRepository storesRepository;

    @Autowired
    ProductInfoRepository productInfoRepository;

    public KRuokaParseServiceDeprecated() {
    }

    @Async
    private Future<String> downloadPage(String url) throws IOException {
        System.out.println(String.format(Thread.currentThread().toString() + "Connecting to %s", url));
        String page = Jsoup.connect(url).userAgent("Mozilla").timeout(60000).get().html();
        System.out.println("Connected");
        return new AsyncResult<>(page);
    }

    public void parseStoresUrls(String page) {
        HashMap<String, String> map = new HashMap<>();

        Document doc = Jsoup.parse(page);

        Elements els = doc.getElementsByAttributeValue("class", "store-list");
        for (Element el : els) {

            for (Element ref : el.getElementsByTag("a")) {
                String store, url;
                store = ref.child(0).child(0).text();
                url = ref.attr("href");
                map.put(store, url);
            }

        }

        this.urls = map;
    }

    public List<FoundProductInfo> parseKRuokaStorePage(String page, Store store) {
        List<FoundProductInfo> foundProducts = new ArrayList<>();

        Document doc = Jsoup.parse(page);

        ArrayList<String> classes = new ArrayList<>();
        classes.add("product-result-item discounted");
        classes.add("product-result-item");

        populateAllDB(doc, classes, store);

        return foundProducts;
    }

    @Async
    public Future<Map<String, List<FoundProductInfo>>> parseAllKRuokaStores() throws IOException {
        HashMap<String, List<FoundProductInfo>> map = new HashMap<>();
        List<Future<String>> pages = new ArrayList<>();

        for (Map.Entry<String, String> store : urls.entrySet()) {
            for(String path : productCategories) {
                String[] pathParts = store.getValue().split("[?]");
                String url = pathParts[0].concat(path).concat("?").concat(pathParts[1]);

                pages.add(downloadPage(url));
                Collections.shuffle(pages);

                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Future<String> page : pages) {
                while (!page.isDone()) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                List<FoundProductInfo> products = new ArrayList<>();
                try {
                    Store st = storesRepository.findByName(store.getKey());
                    products = parseKRuokaStorePage(page.get(), st );
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                map.put(store.getKey(), products);
            }
        }



        return new AsyncResult<>(map);
    }
    private Elements getElementByClass(Document doc ,String cls) {
        return doc.getElementsByAttributeValue("class",cls);
    }

    private void populate(Elements els, List<FoundProductInfo> foundProducts) {
        for (Element el : els) {
            FoundProductInfo info = new FoundProductInfo();
            info.name = el.child(2).child(0).child(0).child(0).text();

            String priceInt = el.child(3).child(0).child(0).text();
            String priceFraction = el.child(3).child(0).child(2).text();
            info.price = Double.parseDouble(priceInt.concat(".").concat(priceFraction));

            info.unit = el.child(3).child(0).child(3).child(1).text();

            foundProducts.add(info);
        }
    }

    private void populateAll(Document doc, List<FoundProductInfo> foundProducts, List<String> classes) {
        for (String cls : classes) {
            Elements els = getElementByClass(doc, cls);
            populate(els, foundProducts);
        }
    }

    private void populateAllDB(Document doc, List<String> classes, Store store) {
        for (String cls : classes) {
            Elements els = getElementByClass(doc, cls);
            populateDB(els, store);
        }
    }

    private void populateDB(Elements els, Store store) {
        List<FoundProductInfo> foundProduct = new ArrayList<>();
        populate(els, foundProduct);
        for (FoundProductInfo product : foundProduct) {
            if (!productsRepository.existsByInfo_NameAndStoreAndPrice(product.getName(), store, product.getPrice())) {
                ProductInfo info;
//                productsRepository.save(new Product());
            }
        }
    }

    public void parseProductCategories(String page) {
        List<String> urls = new ArrayList<>();
        Document doc = Jsoup.parse(page);

        Elements categoryList = doc.getElementsByAttributeValue("class", "product-category-list");
        for(Element el : categoryList.last().children()) {
            String[] urlParts = el.child(0).attr("href").split("[/]");
            urls.add("/".concat(urlParts[urlParts.length-1]));
        }

        this.productCategories = urls;
    }

    public class FoundProductInfo {
        String name;
        Double price;
        String unit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

}
