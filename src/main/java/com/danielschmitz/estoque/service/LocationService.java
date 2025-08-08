package com.danielschmitz.estoque.service;

import com.danielschmitz.estoque.model.Location;
import com.danielschmitz.estoque.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location getById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));
    }

    public Location create(Location location) {
        validateName(location.getName());
        if (locationRepository.existsByNameIgnoreCase(location.getName())) {
            throw new RuntimeException("Já existe um local com este nome");
        }
        return locationRepository.save(location);
    }

    public Location update(Long id, String name) {
        validateName(name);
        Location existing = getById(id);
        if (!existing.getName().equalsIgnoreCase(name)
                && locationRepository.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Já existe um local com este nome");
        }
        existing.setName(name);
        return locationRepository.save(existing);
    }

    public void delete(Long id) {
        Location existing = getById(id);
        locationRepository.delete(existing);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("O nome do local é obrigatório");
        }
    }
}
