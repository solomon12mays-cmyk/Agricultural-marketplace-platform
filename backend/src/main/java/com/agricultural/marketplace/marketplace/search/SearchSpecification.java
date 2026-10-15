package com.agricultural.marketplace.marketplace.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * Builds dynamic JPA predicates for marketplace search.
 *
 * <p>TODO: implement keyword (name/description), category, region, price
 * range, rating and verification filters.</p>
 *
 * @param <T> entity type being searched
 */
public class SearchSpecification<T> implements Specification<T> {

    private final String keyword;

    public SearchSpecification(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // TODO: combine filters with AND semantics.
        throw new UnsupportedOperationException("Search specification not implemented yet");
    }
}
