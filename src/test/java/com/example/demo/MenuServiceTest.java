package com.example.demo;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.entities.Menu;
import com.example.demo.entities.User;
import com.example.demo.repositories.MenuRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MenuService menuService;

    private Menu menu;
    private MenuDTO menuDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");

        menu = new Menu();
        menu.setId(1L);
        menu.setName("Test Menu");
        menu.setDescription("Test Description");
        menu.setAuthor(user);

        menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setName("Test Menu");
        menuDTO.setDescription("Test Description");
        menuDTO.setAuthorId(1L);
    }

    @Test
    void getAllMenus_ShouldReturnListOfMenuDTOs() {
        // Arrange
        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("Second Menu");
        menu2.setDescription("Second Description");
        menu2.setAuthor(user);

        List<Menu> menus = Arrays.asList(menu, menu2);
        when(menuRepository.findAll()).thenReturn(menus);

        // Act
        List<MenuDTO> result = menuService.getAllMenus();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    void searchMenu_ShouldReturnFilteredMenus() {
        // Arrange
        String query = "Test";
        List<Menu> menus = Arrays.asList(menu);
        when(menuRepository.findByNameContainingIgnoreCase(query)).thenReturn(menus);

        // Act
        List<MenuDTO> result = menuService.searchMenu(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Menu", result.get(0).getName());
        verify(menuRepository, times(1)).findByNameContainingIgnoreCase(query);
    }

    @Test
    void getMenu_WithValidId_ShouldReturnMenuDTO() {
        // Arrange
        Long menuId = 1L;
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        // Act
        MenuDTO result = menuService.getMenu(menuId);

        // Assert
        assertNotNull(result);
        assertEquals(menu.getId(), result.getId());
        assertEquals(menu.getName(), result.getName());
        assertEquals(menu.getDescription(), result.getDescription());
        verify(menuRepository, times(1)).findById(menuId);
    }

    @Test
    void getMenu_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 999L;
        when(menuRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> menuService.getMenu(invalidId)
        );

        assertTrue(exception.getMessage().contains("Menu not found"));
        verify(menuRepository, times(1)).findById(invalidId);
    }

    @Test
    void createMenu_WithValidData_ShouldReturnCreatedMenu() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        // Act
        MenuDTO result = menuService.createMenu(menuDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuDTO.getName(), result.getName());
        assertEquals(menuDTO.getDescription(), result.getDescription());
        verify(userRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void createMenu_WithInvalidAuthorId_ShouldThrowException() {
        // Arrange
        menuDTO.setAuthorId(999L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> menuService.createMenu(menuDTO)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(999L);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void deleteMenu_WithValidId_ShouldDeleteMenu() {
        // Arrange
        Long menuId = 1L;
        doNothing().when(menuRepository).deleteById(menuId);

        // Act
        menuService.deleteMenu(menuId);

        // Assert
        verify(menuRepository, times(1)).deleteById(menuId);
    }
}