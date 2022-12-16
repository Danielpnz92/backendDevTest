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
        //primero recuperamos los ids de productos similares al proporcionado, de una de las APIs preexistentes
        SimilarProductsIds similarProductsIds = restTemplate.getForObject("http://localhost:3001/product/"+productId+"/similarids", SimilarProductsIds.class);

        //inicializamos la lista vacía de productos similares
        SimilarProducts similarProducts = new SimilarProducts();
        //en este set(), para evitar elementos duplicados, se guardará los detalles de producto de cada id
        Set<ProductDetail> result = new HashSet<>();

        for (String id : similarProductsIds.getItems()) {
            //le excepción 404 ya es controlada por la ruta "get-product-productId", por lo que no es necesario manejarla aquí
            ProductDetail productDetail = restTemplate.getForObject("http://localhost:3001/product/"+productId, ProductDetail.class);
            result.add(productDetail);
        }

        //se pasan los datos a la instancia de SimilarProducts con el resultado final
        similarProducts.setSimilarProducts(result);
        return similarProducts;
    }
}
