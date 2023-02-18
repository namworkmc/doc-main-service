package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.util.PostgresContainerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
@ExtendWith(PostgresContainerExtension.class)
abstract class AbstractRepositoryTest {

  @Autowired
  protected UserRepository userRepository;
}
