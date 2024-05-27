package com.productstore.InfoStoreAplication.controllers;


import com.productstore.InfoStoreAplication.models.Product;
import com.productstore.InfoStoreAplication.models.ProductDto;
import com.productstore.InfoStoreAplication.services.ProductsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    @GetMapping({"", "/"})
    public String showProductList (Model model){
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage (Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
            ){

            if (productDto.getImageFileName().isEmpty()){
                result.addError(new FieldError("productDto", "imageFileName", "Imagem necessária!"));
            }

            if (result.hasErrors()){
                return "products/CreateProduct";
            }

            //salvar imagem
        MultipartFile image = productDto.getImageFileName();
        Date criadoEm = new Date();
        String storageFileName = criadoEm.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        Product product = new Product();
        product.setNome(productDto.getNome());
        product.setMarca(productDto.getMarca());
        product.setCategoria(productDto.getCategoria());
        product.setPreco(productDto.getPreco());
        product.setDescricao(productDto.getDescricao());
        product.setCriadoEm(criadoEm);
        product.setImageFileName(storageFileName);

        repo.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(
            Model model,
            @RequestParam int id
    ){

        try {
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setNome(product.getNome());
            productDto.setMarca(product.getMarca());
            productDto.setCategoria(product.getCategoria());
            productDto.setPreco(product.getPreco());
            productDto.setDescricao(product.getDescricao());

            model.addAttribute("productDto", productDto);
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/products";
        }

        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ){
        try{
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);

            if (result.hasErrors()){
                return "products/EditProduct";
            }

            if (!productDto.getImageFileName().isEmpty()) {
                // Deletar imagem antiga
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
                try {
                    Files.delete(oldImagePath);
                }
                catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
                //Salvar nova imagem
                MultipartFile image = productDto.getImageFileName();
                Date criadoEm = new Date();
                String storageFileName = criadoEm.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }

            product.setNome(productDto.getNome());
            product.setMarca(productDto.getMarca());
            product.setCategoria(productDto.getCategoria());
            product.setPreco(productDto.getPreco());
            product.setDescricao(productDto.getDescricao());

            repo.save(product);

        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }

        @GetMapping("/delete")
        public String deleteProduct(
                @RequestParam int id
        ){

        try {
            Product product = repo.findById(id).get();

            // deletar produto imagem
            Path imagePath = Paths.get("public/images/" + product.getImageFileName());

            try {
                Files.delete(imagePath);
            }
            catch (Exception ex){
                System.out.println("Exception: " + ex.getMessage());
            }

            //deletar produto
            repo.delete(product);
        }
        catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
        }
}
