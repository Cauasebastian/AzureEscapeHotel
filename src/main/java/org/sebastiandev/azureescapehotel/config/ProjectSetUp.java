package org.sebastiandev.azureescapehotel.config;

import org.sebastiandev.azureescapehotel.repository.RoomRepository;
import org.sebastiandev.azureescapehotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectSetUp {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    public void run(String... args) throws Exception {
        
    }
}
