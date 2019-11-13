package teamworks.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamworks.server.domain.Store;
import teamworks.server.repository.StoresRepository;
import teamworks.server.service.JsonParseService;

import java.util.List;

@Controller
public class StoresController {
    @Autowired
    private JsonParseService jsonParseService;

    @Autowired
    private StoresRepository storesRepository;

    @ResponseBody
    @RequestMapping("/stores/get/{id}")
    public String getStoreById(@PathVariable long id) {
        Store store = storesRepository.findById(id);
        try {
            return jsonParseService.serialize(store);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping("/stores/add")
    public String addStore(@RequestParam String name,
                           @RequestParam long lat,
                           @RequestParam long lon) {
        storesRepository.save(new Store(name, lon, lat));
        return "store saved";
    }

    @ResponseBody
    @RequestMapping("/stores/get")
    public String getStores() {
        List<Store> stores = (List<Store>) storesRepository.findAll();
        try {
            return jsonParseService.serialize(stores);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
