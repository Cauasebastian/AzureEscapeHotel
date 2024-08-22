package org.sebastiandev.azureescapehotel.config;

import org.sebastiandev.azureescapehotel.model.Role;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.model.User;
import org.sebastiandev.azureescapehotel.repository.RoleRepository;
import org.sebastiandev.azureescapehotel.repository.RoomRepository;
import org.sebastiandev.azureescapehotel.service.UserService;
import org.sebastiandev.azureescapehotel.utils.ImageCompressor;
import org.sebastiandev.azureescapehotel.utils.ImageDecompressor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;
    private final ImageCompressor imageCompressor;
    private final ImageDecompressor imageDecompressor;

    public DataInitializer(UserService userService, RoomRepository roomRepository, RoleRepository roleRepository,
                           ImageCompressor imageCompressor, ImageDecompressor imageDecompressor) {
        this.userService = userService;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.imageCompressor = imageCompressor;
        this.imageDecompressor = imageDecompressor;
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

        // Verifica se já existem quartos no banco de dados
        if (roomRepository.count() < 7) {
            // Salva 7 quartos com diferentes nomes, tipos e preços
            saveRoom("Quarto Luxo", "Luxo", new BigDecimal("250.00"), "C:\\Users\\cauas\\Documents\\GitHub\\AzureEscapeHotel\\assets\\hotel-rooms\\image1.jpg");
            saveRoom("Quarto Padrão", "Padrão", new BigDecimal("100.00"), "C:\\Users\\cauas\\Documents\\GitHub\\AzureEscapeHotel\\assets\\hotel-rooms\\image2.jpg");
            saveRoom("Quarto Econômico", "Econômico", new BigDecimal("75.00"), "C:\\Users\\cauas\\Documents\\GitHub\\AzureEscapeHotel\\assets\\hotel-rooms\\image3.jpg");
            saveRoom("Suíte Presidencial", "Presidencial", new BigDecimal("500.00"), "C:\\Users\\cauas\\Documents\\GitHub\\AzureEscapeHotel\\assets\\hotel-rooms\\image4.jpg");
            saveRoom("Suíte Master", "Master", new BigDecimal("400.00"), "C:\\Users\\cauas\\Documents\\GitHub\\AzureEscapeHotel\\assets\\hotel-rooms\\image5.jpg");
            saveRoom("Quarto Familiar", "Familiar", new BigDecimal("150.00"), "C:\\Users\\cauas\\Documents\\GitHub\\AzureEscapeHotel\\assets\\hotel-rooms\\image6.jpg");
            saveRoom("Quarto Executivo", "Executivo", new BigDecimal("200.00"), "C:\\Users\\cauas\\Documents\\GitHub\\AzureEscapeHotel\\assets\\hotel-rooms\\image7.jpg");
        }
    }

    private void saveRoom(String roomName, String roomType, BigDecimal roomPrice, String imagePath) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        // Carrega a imagem do quarto, comprime e a converte em Blob
        byte[] photoBytes = loadImage(imagePath);
        byte[] compressedPhotoBytes = imageCompressor.compress(photoBytes);
        Blob photoBlob = new SerialBlob(compressedPhotoBytes);
        room.setPhoto(photoBlob);

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
