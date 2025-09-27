package uz.consortgroup.certificate_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.consortgroup.certificate_service.entity.Certificate;


import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    Page<Certificate> findAll(Specification<Certificate> withFilters, Pageable pageable);
    List<Certificate> findAllBySerialNumber(String serialNumber);
}
