package com.example.labxspringboot.service.impl;

import com.example.labxspringboot.entity.Reactif;
import com.example.labxspringboot.repository.IReactifRepository;
import com.example.labxspringboot.service.IReactifService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ComponentScan(basePackages = "com.example.labxspringboot.service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Transactional
public class ReactifServiceImplTest {

    @Autowired
    private IReactifService reactifService;

    private Reactif testReactif;

    @BeforeEach
    void setUp() {
        // Create a new Reactif before each test
        testReactif = new Reactif();
        testReactif.setNom("TestReactif");
        testReactif.setQuantiteStock(10);
        testReactif.setDateExpiration("2025-01-01");

        // Save the Reactif
        reactifService.saveReactif(testReactif);
    }

    @AfterEach
    void tearDown() {
        if (reactifService.getReactifById(testReactif.getId()) != null) {
            reactifService.deleteReactif(testReactif.getId());
        }
    }
    @Rollback(value = false)
    @Test
    void saveReactif() {
        assertNotNull(testReactif.getId(), "Reactif ID should not be null after saving");
    }
    @Rollback(value = false)
    @Test
    void getReactifById() {
        Reactif retrievedReactif = reactifService.getReactifById(testReactif.getId());
        assertNotNull(retrievedReactif, "Retrieved reactif should not be null");
        assertEquals(testReactif.getId(), retrievedReactif.getId(), "IDs should match");
    }
    @Rollback(value = false)
    @Test
    void updateReactif() {
        assertNotNull(testReactif.getId(), "Reactif ID should not be null before updating");
        testReactif.setQuantiteStock(20);
        Reactif updatedReactif = reactifService.updateReactif(testReactif);
        assertNotNull(updatedReactif, "Updated reactif should not be null");
        assertEquals(20, updatedReactif.getQuantiteStock(), "QuantiteStockReactif should be updated");
    }
    @Rollback(value = false)
    @Test
    void getAllReactifs() {
        List<Reactif> reactifs = reactifService.getReactifs();
        assertFalse(reactifs.isEmpty());
    }
    @Rollback(value = false)
    @Test
    void deleteReactif() {
        reactifService.deleteReactif(testReactif.getId());
        assertNull(reactifService.getReactifById(testReactif.getId()), "Reactif should be deleted");
    }
}
