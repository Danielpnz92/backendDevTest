package BackendDevTest.controller;

import BackendDevTest.model.ProductDetail;
import BackendDevTest.model.SimilarProducts;
import BackendDevTest.model.SimilarProductsIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class CatalogController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/product/{productId}/similar")
    @ResponseStatus(HttpStatus.OK)
    public SimilarProducts getProductSimilar(@PathVariable String productId){
        SimilarProductsIds similarProductsIds = restTemplate.getForObject("http://localhost:3001/product/"+productId+"/similarids", SimilarProductsIds.class);

        SimilarProducts similarProducts = new SimilarProducts();
        Set<ProductDetail> result = new HashSet<>();

        for (String id : similarProductsIds.getItems()) {
            //le excepción 404 ya es controlada por la ruta "get-product-productId", por lo que no es necesario manejarla aquí
            ProductDetail productDetail = restTemplate.getForObject("http://localhost:3001/product/"+productId, ProductDetail.class);
            result.add(productDetail);
        }

        similarProducts.setSimilarProducts(result);
        return similarProducts;
    }
}
