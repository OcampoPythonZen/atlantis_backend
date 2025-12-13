package com.atlantis.nutritionist.repository;

import com.atlantis.nutritionist.entity.FoodDiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for FoodDiaryEntry entity.
 * Provides database operations for food diary entries.
 */
@Repository
public interface FoodDiaryEntryRepository extends JpaRepository<FoodDiaryEntry, UUID> {

    /**
     * Find all food diary entries for a client ordered by entry date descending.
     * @param clientId The client's UUID
     * @return List of food diary entries
     */
    List<FoodDiaryEntry> findByClientIdOrderByEntryDateDesc(UUID clientId);

    /**
     * Find food diary entries for a client on a specific date.
     * @param clientId The client's UUID
     * @param entryDate The entry date
     * @return List of food diary entries
     */
    List<FoodDiaryEntry> findByClientIdAndEntryDate(UUID clientId, LocalDate entryDate);

    /**
     * Find food diary entries for a client within a date range.
     * @param clientId The client's UUID
     * @param startDate Start date
     * @param endDate End date
     * @return List of food diary entries
     */
    List<FoodDiaryEntry> findByClientIdAndEntryDateBetween(UUID clientId, LocalDate startDate, LocalDate endDate);

    /**
     * Find food diary entries by meal type for a client.
     * @param clientId The client's UUID
     * @param mealType The meal type (breakfast, lunch, dinner, snack)
     * @return List of food diary entries
     */
    List<FoodDiaryEntry> findByClientIdAndMealType(UUID clientId, String mealType);

    /**
     * Calculate total calories for a client on a specific date.
     * @param clientId The client's UUID
     * @param entryDate The entry date
     * @return Total calories
     */
    @Query("SELECT COALESCE(SUM(f.calories), 0) FROM FoodDiaryEntry f WHERE f.client.id = :clientId AND f.entryDate = :entryDate")
    Double calculateDailyCalories(@Param("clientId") UUID clientId, @Param("entryDate") LocalDate entryDate);
}
