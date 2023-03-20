package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.repository.DistributionOrganizationRepository;
import edu.hcmus.doc.mainservice.repository.DocumentTypeRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.repository.UserRoleRepository;
import edu.hcmus.doc.mainservice.util.PostgresContainerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith({PostgresContainerExtension.class, MockitoExtension.class})
abstract class AbstractServiceTest {

  @Mock
  protected DistributionOrganizationRepository distributionOrganizationRepository;

  @Mock
  protected DocumentTypeRepository documentTypeRepository;

  @Mock
  protected ProcessingDocumentRepository processingDocumentRepository;

  @Mock
  protected UserRoleRepository userRoleRepository;

  @Mock
  protected UserRepository userRepository;

}
