package com.example.jejugudgo.domain.review.service;

import com.example.jejugudgo.domain.review.docs.ReviewCategoryDocument;
import com.example.jejugudgo.domain.review.entity.ReviewCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCategoryDocumentService {

    /* create */
    public List<ReviewCategoryDocument> documentsCategories(List<ReviewCategory> categories) {
        List<ReviewCategoryDocument> categoriesDocuments = new ArrayList<>();

        for (ReviewCategory reviewCategory : categories) {
            String category = reviewCategory.getCategory1() != null ? reviewCategory.getCategory1().getCategory1() :
                    reviewCategory.getCategory2() != null ? reviewCategory.getCategory2().getCategory2() :
                            reviewCategory.getCategory3() != null ? reviewCategory.getCategory3().getCategory3() : null;

            ReviewCategoryDocument reviewCategoryDocument = ReviewCategoryDocument.of(reviewCategory.getId(), category);
            categoriesDocuments.add(reviewCategoryDocument);
        }

        return categoriesDocuments;
    }
}
