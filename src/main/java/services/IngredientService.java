package services;

import entities.Ingredient;
import repositories.IngredientRepository;
import java.util.List;
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository){
        this.ingredientRepository=ingredientRepository;
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredient(Long id){
        return ingredientRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    public Ingredient createIngredient(Ingredient ingredient){
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(Long id){
        ingredientRepository.deleteById(id);
    }
}