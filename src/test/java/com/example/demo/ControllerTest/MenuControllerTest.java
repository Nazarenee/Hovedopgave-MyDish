package com.example.demo.ControllerTest;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.controllers.MenuController;
import com.example.demo.services.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    private MenuDTO menuDTO;

    @BeforeEach
    void setUp() {
        menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setName("Test Menu");
        menuDTO.setDescription("Test Description");
        menuDTO.setAuthorId(1L);
    }

    @Test
    void getAllMenus_ShouldReturnListOfMenus() {
        // Arrange
        MenuDTO menu2 = new MenuDTO();
        menu2.setId(2L);
        menu2.setName("Second Menu");
        menu2.setDescription("Second Description");

        List<MenuDTO> expectedMenus = Arrays.asList(menuDTO, menu2);
        when(menuService.getAllMenus()).thenReturn(expectedMenus);

        // Act
        List<MenuDTO> result = menuController.getAllMenus();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedMenus, result);
        verify(menuService, times(1)).getAllMenus();
    }

    @Test
    void searchMenu_ShouldReturnFilteredMenus() {
        // Arrange
        String query = "Test";
        List<MenuDTO> expectedMenus = Arrays.asList(menuDTO);
        when(menuService.searchMenu(query)).thenReturn(expectedMenus);

        // Act
        List<MenuDTO> result = menuController.searchMenu(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuDTO.getName(), result.get(0).getName());
        verify(menuService, times(1)).searchMenu(query);
    }

    @Test
    void getMenu_WithValidId_ShouldReturnMenu() {
        // Arrange
        Long menuId = 1L;
        when(menuService.getMenu(menuId)).thenReturn(menuDTO);

        // Act
        MenuDTO result = menuController.getMenu(menuId);

        // Assert
        assertNotNull(result);
        assertEquals(menuDTO.getId(), result.getId());
        assertEquals(menuDTO.getName(), result.getName());
        assertEquals(menuDTO.getDescription(), result.getDescription());
        verify(menuService, times(1)).getMenu(menuId);
    }

    @Test
    void createMenu_WithValidDTO_ShouldReturnCreatedMenu() {
        // Arrange
        when(menuService.createMenu(any(MenuDTO.class))).thenReturn(menuDTO);

        // Act
        MenuDTO result = menuController.createMenu(menuDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuDTO.getId(), result.getId());
        assertEquals(menuDTO.getName(), result.getName());
        assertEquals(menuDTO.getDescription(), result.getDescription());
        verify(menuService, times(1)).createMenu(any(MenuDTO.class));
    }

    @Test
    void deleteMenu_WithValidId_ShouldCallServiceDelete() {
        // Arrange
        Long menuId = 1L;
        doNothing().when(menuService).deleteMenu(menuId);

        // Act
        menuController.deleteMenu(menuId);

        // Assert
        verify(menuService, times(1)).deleteMenu(menuId);
    }
}