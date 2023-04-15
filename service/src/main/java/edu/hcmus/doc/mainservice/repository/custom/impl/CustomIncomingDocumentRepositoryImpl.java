package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.*;
import edu.hcmus.doc.mainservice.repository.custom.CustomIncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomIncomingDocumentRepositoryImpl
    extends DocAbstractCustomRepository<IncomingDocument>
    implements CustomIncomingDocumentRepository {

  @Override
  public Long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return selectFrom(QIncomingDocument.incomingDocument)
        .select(QIncomingDocument.incomingDocument.id.count())
        .fetchOne();
  }

  @Override
  public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return select(
        QIncomingDocument.incomingDocument.id,
        QIncomingDocument.incomingDocument.version,
        QIncomingDocument.incomingDocument.sendingLevel,
        QIncomingDocument.incomingDocument.documentType,
        QIncomingDocument.incomingDocument.incomingNumber,
        QIncomingDocument.incomingDocument.originalSymbolNumber,
        QIncomingDocument.incomingDocument.arrivingDate,
        QIncomingDocument.incomingDocument.distributionOrg,
        QIncomingDocument.incomingDocument.summary
    ).from(QIncomingDocument.incomingDocument)
        .innerJoin(QIncomingDocument.incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
        .innerJoin(QIncomingDocument.incomingDocument.documentType, QDocumentType.documentType)
        .innerJoin(QIncomingDocument.incomingDocument.distributionOrg, QDistributionOrganization.distributionOrganization)
        .orderBy(QIncomingDocument.incomingDocument.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch()
        .stream()
        .map(tuple -> {
          IncomingDocument incomingDocument = new IncomingDocument();
          incomingDocument.setId(tuple.get(QIncomingDocument.incomingDocument.id));
          incomingDocument.setVersion(tuple.get(QIncomingDocument.incomingDocument.version));
          incomingDocument.setSendingLevel(tuple.get(QIncomingDocument.incomingDocument.sendingLevel));
          incomingDocument.setDocumentType(tuple.get(QIncomingDocument.incomingDocument.documentType));
          incomingDocument.setIncomingNumber(tuple.get(QIncomingDocument.incomingDocument.incomingNumber));
          incomingDocument.setOriginalSymbolNumber(tuple.get(QIncomingDocument.incomingDocument.originalSymbolNumber));
          incomingDocument.setArrivingDate(tuple.get(QIncomingDocument.incomingDocument.arrivingDate));
          incomingDocument.setDistributionOrg(tuple.get(QIncomingDocument.incomingDocument.distributionOrg));
          incomingDocument.setSummary(tuple.get(QIncomingDocument.incomingDocument.summary));
          return incomingDocument;
        })
        .toList();
  }

    @Override
    public IncomingDocument getIncomingDocumentById(Long id) {
        return selectFrom(QIncomingDocument.incomingDocument)
                .join(QIncomingDocument.incomingDocument.folder, QFolder.folder)
                .fetchJoin()
                .join(QIncomingDocument.incomingDocument.documentType, QDocumentType.documentType)
                .fetchJoin()
                .join(QIncomingDocument.incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
                .fetchJoin()
                .join(QIncomingDocument.incomingDocument.distributionOrg, QDistributionOrganization.distributionOrganization)
                .fetchJoin()
                .where(QIncomingDocument.incomingDocument.id.eq(id))
                .fetchFirst();
    }

  @Override
  public List<IncomingDocument> getIncomingDocumentsByIds(List<Long> ids) {
    return selectFrom(QIncomingDocument.incomingDocument)
        .where(QIncomingDocument.incomingDocument.id.in(ids))
        .fetch();
  }
}
