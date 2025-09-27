package uz.consortgroup.certificate_service.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.consortgroup.certificate_service.dto.CertificateFilter;
import uz.consortgroup.certificate_service.entity.Certificate;

import java.util.ArrayList;
import java.util.List;



public class CertificateSpecification {
    public static Specification<Certificate> withFilters(CertificateFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getFullName() != null) {
                predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + filter.getFullName().toLowerCase() + "%"));
            }
            if (filter.getCourseName() != null) {
                predicates.add(cb.like(cb.lower(root.get("courseName")), "%" + filter.getCourseName().toLowerCase() + "%"));
            }
            if (filter.getCertificateColor() != null) {
                predicates.add(cb.equal(root.get("certificateColor"), filter.getCertificateColor()));
            }
            if (filter.getIssuedAt() != null) {
                predicates.add(cb.equal(root.get("issuedDate"), filter.getIssuedAt()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
