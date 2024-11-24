package com.darpan.Ecommerce_Application.controller;

import com.darpan.Ecommerce_Application.model.Product;
import com.darpan.Ecommerce_Application.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Optional<Product>> getProductByCategory(@PathVariable Integer id){
        return productService.getProductById(id);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        return productService.addProduct(product,imageFile);
    }
    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int id){
        return productService.getImage(id);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart MultipartFile imageFile, @RequestPart Product product){
        try {
            return productService.updateProduct(id,imageFile,product);
        } catch (IOException e) {
            return new ResponseEntity<>("Unable to Update", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = productService.getProductById(id).getBody().orElse(null);
        if( product != null){
            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }else{
           return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        return productService.searchProduct(keyword);
    }
}
