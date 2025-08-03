package place.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import place.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c.id from categories c where c.parentId is null")
    List<Long> findParentCategoryIds();

    @Query("select c from categories c where c.parentId is null")
    List<Category> findParentCategories();

    @Query("select c from categories c where c.parentId is not null")
    List<Category> findSubCategories();

    @Query("select c from categories c where c.parentId = :parentId")
    List<Category> findSubCategoriesByParentId(Long parentId);
}
