package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QTransferHistory.transferHistory;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import edu.hcmus.doc.mainservice.model.dto.TransferHistory.GetTransferDocumentHistoryResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistorySearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.QUser;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.repository.custom.CustomTransferHistoryRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomTransferHistoryRepositoryImpl extends
    DocAbstractCustomRepository<TransferHistory>
    implements CustomTransferHistoryRepository {

  @Override
  public GetTransferDocumentHistoryResponse getTransferDocumentHistory(Long currentUserId) {
    return null;
  }

  @Override
  public long getTotalElements(TransferHistorySearchCriteriaDto criteria) {
    return searchQueryByCriteria(criteria)
        .select(transferHistory.id.count())
        .fetchFirst();
  }

  @Override
  public long getTotalPages(TransferHistorySearchCriteriaDto criteria, long limit) {
    long totalElements = getTotalElements(criteria);
    return (totalElements / limit) + (totalElements % limit == 0 ? 0 : 1);
  }

  @Override
  public List<TransferHistory> searchByCriteria(TransferHistorySearchCriteriaDto criteria,
      long offset, long limit) {
    return searchQueryByCriteria(criteria)
        .offset(offset)
        .limit(limit)
        .fetch();
  }

  @Override
  public JPAQuery<TransferHistory> buildSearchQuery(TransferHistorySearchCriteriaDto criteria) {
    return null;
  }

  private JPAQuery<TransferHistory> searchQueryByCriteria(
      TransferHistorySearchCriteriaDto criteria) {
    BooleanBuilder where = new BooleanBuilder();

    if (criteria != null && criteria.getUserId() != null) {
      where.and(transferHistory.sender.id.eq(criteria.getUserId())
          .or(transferHistory.receiver.id.eq(criteria.getUserId())));
    }

    JPAQuery<TransferHistory> query = selectFrom(transferHistory)
        .leftJoin(QUser.user).on(transferHistory.sender.id.eq(QUser.user.id))
        .fetchJoin()
        .leftJoin(QUser.user).on(transferHistory.receiver.id.eq(QUser.user.id))
        .fetchJoin()
        .where(where)
        .orderBy(transferHistory.createdDate.desc());
    return query;
  }

  @Override
  public List<TransferHistory> findByUserIdAndUnread(Long userId) {
    return selectFrom(transferHistory)
        .where((transferHistory.receiver.id.eq(userId).or(transferHistory.sender.id.eq(userId)))
            .and(transferHistory.isRead.eq(false)))
        .fetch();
  }
}
