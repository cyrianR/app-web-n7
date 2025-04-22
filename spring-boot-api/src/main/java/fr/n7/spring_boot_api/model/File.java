package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Type du fichier : jpeg, txt...
    private String type;

    // Permet d'indiquer que l'attribut doit être stocké sous forme de grande donnée
    @Lob
    private byte[] data;

    public File() {
    }

    public File(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
