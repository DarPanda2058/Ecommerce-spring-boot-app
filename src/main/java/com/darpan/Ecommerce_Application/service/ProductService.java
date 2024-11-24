package com.darpan.Ecommerce_Application.service;

import com.darpan.Ecommerce_Application.model.Product;
import com.darpan.Ecommerce_Application.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<Product>> getAllProducts() {
        try{
            return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Optional<Product>> getProductById(Integer id) {

        Optional<Product> product = productRepository.findById(id);

        try{
            if (product.isPresent()){
                return new ResponseEntity<>(product,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (MethodArgumentTypeMismatchException e){
            e.printStackTrace();
            return new ResponseEntity<>(Optional.empty(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> addProduct(Product product, MultipartFile imageFile) {
        try{
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
            return new ResponseEntity<>(productRepository.save(product),HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<byte[]> getImage(int id) {
        Optional<Product> product = productRepository.findById(id);
        byte[] image = product.orElseThrow().getImageData();
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    public ResponseEntity<String> updateProduct(int id, MultipartFile imageFile, Product product) throws IOException {
        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getName());
        product.setImageType(imageFile.getContentType());
        try {
            productRepository.save(product);
            return new ResponseEntity<>("Updated Successfully",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public ResponseEntity<List<Product>> searchProduct(String keyword) {
        List<Product> products = productRepository.searchProduct(keyword);
        System.out.println("Searching with: "+keyword);
        if (!products.isEmpty()){
            return new ResponseEntity<>(products,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }
    }
}
