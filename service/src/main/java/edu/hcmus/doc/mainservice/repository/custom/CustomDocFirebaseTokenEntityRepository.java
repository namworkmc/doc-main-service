package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.DocFirebaseTokenEntity;
import java.util.List;

public interface CustomDocFirebaseTokenEntityRepository {

  List<DocFirebaseTokenEntity> getTokensByUserId(Long userId);
}
