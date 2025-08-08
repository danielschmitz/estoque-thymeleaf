package com.danielschmitz.estoque.config;

import com.danielschmitz.estoque.model.Category;
import com.danielschmitz.estoque.model.Location;
import com.danielschmitz.estoque.model.Product;
import com.danielschmitz.estoque.model.User;
import com.danielschmitz.estoque.repository.CategoryRepository;
import com.danielschmitz.estoque.repository.LocationRepository;
import com.danielschmitz.estoque.repository.ProductRepository;
import com.danielschmitz.estoque.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@email.com") == null){
            User admin = new  User(null, "admin", "admin@email.com", passwordEncoder.encode("123"));
            userRepository.save(admin);
            logger.info("Usuário admin criado com sucesso");
        }

        // Categorias fictícias
        if (categoryRepository.count() == 0) {
            Category c1 = new Category();
            c1.setName("Perecíveis");
            Category c2 = new Category();
            c2.setName("Limpeza");
            Category c3 = new Category();
            c3.setName("Bebidas");
            categoryRepository.save(c1);
            categoryRepository.save(c2);
            categoryRepository.save(c3);
            logger.info("Categorias de exemplo criadas");
        }

        // Locais fictícios
        if (locationRepository.count() == 0) {
            Location l1 = new Location();
            l1.setName("Depósito A");
            Location l2 = new Location();
            l2.setName("Depósito B");
            Location l3 = new Location();
            l3.setName("Loja 1");
            locationRepository.save(l1);
            locationRepository.save(l2);
            locationRepository.save(l3);
            logger.info("Locais de exemplo criados");
        }

        // Produtos fictícios
        if (productRepository.count() == 0) {
            Category pereciveis = categoryRepository.findAll().stream()
                    .filter(c -> "Perecíveis".equalsIgnoreCase(c.getName()))
                    .findFirst().orElse(null);
            Category limpeza = categoryRepository.findAll().stream()
                    .filter(c -> "Limpeza".equalsIgnoreCase(c.getName()))
                    .findFirst().orElse(null);
            Category bebidas = categoryRepository.findAll().stream()
                    .filter(c -> "Bebidas".equalsIgnoreCase(c.getName()))
                    .findFirst().orElse(null);

            if (pereciveis == null || limpeza == null || bebidas == null) {
                logger.warn("Categorias de exemplo não encontradas para criar produtos fictícios");
            } else {
                Product p1 = new Product();
                p1.setName("Leite Integral 1L");
                p1.setCodigoDeBarras("789100000001");
                p1.setCategory(pereciveis);

                Product p2 = new Product();
                p2.setName("Detergente Neutro");
                p2.setCodigoDeBarras("789200000002");
                p2.setCategory(limpeza);

                Product p3 = new Product();
                p3.setName("Refrigerante Cola 2L");
                p3.setCodigoDeBarras("789300000003");
                p3.setCategory(bebidas);

                productRepository.save(p1);
                productRepository.save(p2);
                productRepository.save(p3);
                logger.info("Produtos de exemplo criados");
            }
        }
    }
}
