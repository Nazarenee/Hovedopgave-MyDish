package services;

import java.util.List;

import entities.Menu;
import repositories.MenuRepository;

public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu getMenu(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
    }

    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}