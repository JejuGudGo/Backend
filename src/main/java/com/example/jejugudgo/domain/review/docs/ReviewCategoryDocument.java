package com.example.jejugudgo.domain.review.docs;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "review_category")
@Data
public class ReviewCategoryDocument {
    @Id
    private Long categoryId;

    private String category;

    public static ReviewCategoryDocument of(Long id, String category) {
        ReviewCategoryDocument reviewCategoryDocument = new ReviewCategoryDocument();
        reviewCategoryDocument.setCategoryId(id);
        reviewCategoryDocument.setCategory(category);

        return reviewCategoryDocument;
    }
}
