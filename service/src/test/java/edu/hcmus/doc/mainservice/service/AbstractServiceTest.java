package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.util.PostgresContainerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith({PostgresContainerExtension.class, MockitoExtension.class})
abstract class AbstractServiceTest {

}
