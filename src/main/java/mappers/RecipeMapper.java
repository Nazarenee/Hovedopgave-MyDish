package mappers;

import DTO.RecipeDTO;
import entities.Recipe;

public class RecipeMapper {
    public static RecipeDTO toDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        return dto;
    }

    public static Recipe fromDTO(RecipeDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setAuthor(null);
        recipe.setEnableComments(dto.isEnableComments());
        recipe.setImages(dto.getImages());
        return recipe;
    }
}
