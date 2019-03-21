package com.example.demo.repositories;

import com.example.demo.beans.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Long> {
}
