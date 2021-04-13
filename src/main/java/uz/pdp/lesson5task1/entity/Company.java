package uz.pdp.lesson5task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String companyName;
    @OneToOne
    private User directorName;


    public Company(User directorName, String companyName) {
        this.directorName = directorName;
        this.companyName = companyName;
    }
}
