package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.DistributionOrganization;
import edu.hcmus.doc.mainservice.model.entity.QDistributionOrganization;
import edu.hcmus.doc.mainservice.repository.custom.CustomDistributionOrganizationRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomDistributionOrganizationRepositoryImpl
    extends DocAbstractCustomRepository<DistributionOrganization>
    implements CustomDistributionOrganizationRepository {

  @Override
  public long getTotalElements(DistributionOrganizationSearchCriteria criteria) {
    return buildSearchQuery(criteria)
        .select(QDistributionOrganization.distributionOrganization.id.count())
        .fetchFirst();
  }

  @Override
  public long getTotalPages(DistributionOrganizationSearchCriteria criteria, long limit) {
    return 0;
  }

  @Override
  public List<DistributionOrganization> searchByCriteria(DistributionOrganizationSearchCriteria criteria, long offset, long limit) {
    QDistributionOrganization qDistributionOrganization = QDistributionOrganization.distributionOrganization;
    return buildSearchQuery(criteria)
        .orderBy(qDistributionOrganization.name.asc())
        .orderBy(qDistributionOrganization.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch();
  }

  @Override
  public JPAQuery<DistributionOrganization> buildSearchQuery(DistributionOrganizationSearchCriteria criteria) {
    return selectFrom(QDistributionOrganization.distributionOrganization);
  }
}
