package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import javax.persistence.NamedQuery;
import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> findByUserId(int userId, Sort sort);

    Meal findByIdAndUserId(int id, int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=?1 AND m.dateTime >=?2 AND m.dateTime <?3 ORDER BY m.dateTime DESC")
    List<Meal> findByDateTimeBetweenAndUserId(int userId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);
}
