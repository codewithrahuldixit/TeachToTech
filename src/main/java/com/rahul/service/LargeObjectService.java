package com.rahul.service;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import com.rahul.model.Course;

@Transactional
public class LargeObjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveEntity(List<String> data) {
        entityManager.getTransaction().begin();
        try {
            Course course = new Course();
            course.setDescription(data); // Directly setting List<String>
            entityManager.persist(course);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error saving entity", e);
        }
    }

    public List<String> retrieveEntity(Long id) {
        try {
            Course course = entityManager.find(Course.class, id);
            if (course != null) {
                return course.getDescription();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving entity", e);
        }
    }
}
