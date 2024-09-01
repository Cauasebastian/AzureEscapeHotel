package org.sebastiandev.azureescapehotel.config;

import org.sebastiandev.azureescapehotel.model.Role;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.model.RoomImage;
import org.sebastiandev.azureescapehotel.model.User;
import org.sebastiandev.azureescapehotel.repository.RoleRepository;
import org.sebastiandev.azureescapehotel.repository.RoomRepository;
import org.sebastiandev.azureescapehotel.service.UserService;
import org.sebastiandev.azureescapehotel.utils.ImageProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;
    private final ImageProcessor imageProcessor;

    public DataInitializer(UserService userService, RoomRepository roomRepository, RoleRepository roleRepository,
                           ImageProcessor imageProcessor) {
        this.userService = userService;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.imageProcessor = imageProcessor;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.getUsers().isEmpty()) {
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("User");
            user.setEmail("admin@email.com");
            user.setPassword("password");

            // Adiciona a role "ROLE_ADMIN"
            Role role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_ADMIN");
                        return roleRepository.save(newRole);
                    });

            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);

            userService.registerUser(user);
        }

        // Adiciona quartos ao banco de dados
        if (roomRepository.count() < 7) {
            // Construa o caminho relativo
            String basePath = Paths.get("assets", "hotel-rooms").toAbsolutePath().toString();

            // Salva 7 quartos com diferentes nomes, tipos e preços
            saveRoom("Quarto Luxo", "Luxo", new BigDecimal("250.00"), Paths.get(basePath, "image1.jpg").toString());
            saveRoom("Quarto Padrão", "Padrão", new BigDecimal("100.00"), Paths.get(basePath, "image2.jpg").toString());
            saveRoom("Quarto Econômico", "Econômico", new BigDecimal("75.00"), Paths.get(basePath, "image3.jpg").toString());
            saveRoom("Suíte Presidencial", "Presidencial", new BigDecimal("500.00"), Paths.get(basePath, "image4.jpg").toString());
            saveRoom("Suíte Master", "Master", new BigDecimal("400.00"), Paths.get(basePath, "image5.jpg").toString());
            saveRoom("Quarto Familiar", "Familiar", new BigDecimal("150.00"), Paths.get(basePath, "image6.jpg").toString());
            saveRoom("Quarto Executivo", "Executivo", new BigDecimal("200.00"), Paths.get(basePath, "image7.jpg").toString());
        }
    }

    private void saveRoom(String roomName, String roomType, BigDecimal roomPrice, String imagePath) throws IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        // Carrega, redimensiona e comprime a imagem do quarto
        byte[] photoBytes = loadImage(imagePath);
        byte[] resizedPhotoBytes = imageProcessor.resizeImage(photoBytes);
        byte[] compressedPhotoBytes = imageProcessor.compress(resizedPhotoBytes);

        // Cria a entidade RoomImage e associa ao Room
        RoomImage roomImage = new RoomImage();
        roomImage.setImage(compressedPhotoBytes);
        room.setRoomImage(roomImage);
        roomImage.setRoom(room);

        // Salva o quarto no banco de dados
        roomRepository.save(room);
    }

    private byte[] loadImage(String path) throws IOException {
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }
}
