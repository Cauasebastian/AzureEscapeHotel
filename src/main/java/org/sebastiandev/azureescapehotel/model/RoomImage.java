package org.sebastiandev.azureescapehotel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.sebastiandev.azureescapehotel.config.BlobSerializer;
import org.sebastiandev.azureescapehotel.model.Room;

import java.sql.Blob;

@Entity
@Getter
@Setter
public class RoomImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @OneToOne(mappedBy = "roomImage", cascade = CascadeType.ALL)
    @JsonBackReference
    private Room room;
}
