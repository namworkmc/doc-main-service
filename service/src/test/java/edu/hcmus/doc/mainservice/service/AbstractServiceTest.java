package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.repository.DistributionOrganizationRepository;
import edu.hcmus.doc.mainservice.repository.DocumentTypeRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.util.PostgresContainerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith({PostgresContainerExtension.class, MockitoExtension.class})
abstract class AbstractServiceTest {

  @MockBean
  protected DistributionOrganizationRepository distributionOrganizationRepository;

  @MockBean
  protected DocumentTypeRepository documentTypeRepository;

  @MockBean
  protected ProcessingDocumentRepository processingDocumentRepository;

  @MockBean
  protected UserRepository userRepository;

  @MockBean
  protected IncomingDocumentRepository incomingDocumentRepository;
}
