package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    @Column(nullable = true)
    private Integer averageCookingTime;
}
