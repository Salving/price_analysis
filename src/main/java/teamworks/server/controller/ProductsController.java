package teamworks.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamworks.server.domain.ProductInfo;
import teamworks.server.domain.Store;
import teamworks.server.domain.Tag;
import teamworks.server.repository.ProductInfoRepository;
import teamworks.server.repository.StoresRepository;
import teamworks.server.repository.TagRepository;
import teamworks.server.service.JsonParseService;
import teamworks.server.domain.Product;
import teamworks.server.repository.ProductsRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductsController {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private StoresRepository storesRepository;

    @Autowired
    private JsonParseService jsonParseService;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private TagRepository tagRepository;

    @ResponseBody
    @RequestMapping("/products/get/{name}")
    public String getProductsByName(@PathVariable String name) {
        List<Product> products = productsRepository.findByInfo_NameContainsIgnoreCase(name);
//        System.out.println(products);
        try {
            return jsonParseService.serialize(products);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping("/products/get/ean")
    public String getProductsByEan(@RequestParam String ean) {
        List<Product> products = productsRepository.findByInfo_Ean(ean);

        try {
            return jsonParseService.serialize(products);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "0";
    }

    @ResponseBody
    @RequestMapping("/products/get/{id}/store")
    public String getStoreByProductId(@PathVariable long id) {
        Product product = productsRepository.findProductById(id);
        Store store = product.getStore();

        try {
            return jsonParseService.serialize(store);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping("/products/{product}/tag/add")
    public String addTagToProduct(@PathVariable String product,
                                  @RequestParam String tag) {
        if (!productInfoRepository.existsByName(product)) {
            return "Product not found";
        }

        ProductInfo productInfo = productInfoRepository.findByName(product);
        Tag tagInDB;

        if (tagRepository.existsByName(tag)) {
            tagInDB = tagRepository.findByName(tag);
        } else {
            tagRepository.save(new Tag(tag));
            tagInDB = tagRepository.findByName(tag);
        }

        if (!productInfoRepository.findByName(product).getTags().contains(tagInDB)) {

            productInfo.getTags().add(tagInDB);
            tagInDB.getProductInfo().add(productInfo);

            tagRepository.save(tagInDB);
            productInfoRepository.save(productInfo);
            return "Tag added";
        } else {
            return "Tag not added";
        }



    }

    @ResponseBody
    @RequestMapping("/tags/{tag}/products")
    public String getProductsByTag(@PathVariable String tag) {
        Tag tagInDB;
        if (tagRepository.existsByName(tag)) {
            tagInDB = tagRepository.findByName(tag);
        } else {
            tagInDB = new Tag(tag);
        }

        List<ProductInfo> productInfos = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        tags.add(tagInDB);

        if (productInfoRepository.existsByTagsIn(tags)) {
            productInfos = productInfoRepository.findByTagsIn(tags);
        }

        List<Product> products = new ArrayList<>();

        for (ProductInfo info : productInfos) {
            products.addAll(productsRepository.findByInfo_Name(info.getName()));
        }

        try {
            return jsonParseService.serialize(products);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
