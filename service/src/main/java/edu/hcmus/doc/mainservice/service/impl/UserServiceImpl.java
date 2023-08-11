package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.UserPasswordException.PASSWORD_CONFIRMATION_INVALID;
import static edu.hcmus.doc.mainservice.model.exception.UserPasswordException.PASSWORD_NOT_CHANGED;
import static edu.hcmus.doc.mainservice.security.util.SecurityUtils.generateRandomPassword;

import edu.hcmus.doc.mainservice.model.dto.ChangePasswordDto;
import edu.hcmus.doc.mainservice.model.dto.DocListStatisticsDto;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.DocStatisticsDto;
import edu.hcmus.doc.mainservice.model.dto.DocStatisticsSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistoryDto;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistorySearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.UserDepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.dto.UserSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.EmailExistedException;
import edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.TransferHistoryNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserPasswordException;
import edu.hcmus.doc.mainservice.model.exception.UsernameExistedException;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.TransferHistoryRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.EmailService;
import edu.hcmus.doc.mainservice.service.UserService;
import edu.hcmus.doc.mainservice.util.mapper.PaginationMapper;
import edu.hcmus.doc.mainservice.util.mapper.TransferHistoryMapper;
import edu.hcmus.doc.mainservice.util.mapper.UserMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final PaginationMapper paginationMapper;
  private final TransferHistoryRepository transferHistoryRepository;
  private final TransferHistoryMapper transferHistoryMapper;
  private final ProcessingDocumentRepository processingDocumentRepository;
  private final ProcessingUserRepository processingUserRepository;
  private final ProcessingUserRoleRepository processingUserRoleRepository;
  private final EmailService emailService;

  @Override
  public List<User> getUsers(String query, long first, long max) {
    return userRepository.getUsers(query, first, max);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.getAllUsers();
  }

  @Override
  public User getUserById(Long id) {
    return userRepository
        .getUserById(id)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public long getTotalUsers() {
    return userRepository.count();
  }

  @Override
  public boolean validateUserCredentialsByUserId(Long id, String password) {
    return passwordEncoder.matches(password, getUserById(id).getPassword());
  }

  @Override
  public User getCurrentUserFromDB() {
    return userRepository
        .findByUsername(SecurityUtils.getCurrentName())
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public List<User> getUsersByRole(DocSystemRoleEnum role) {
    return userRepository.getUsersByRole(role);
  }

  @Override
  public List<UserDepartmentDto> getUsersByRoleWithDepartment(DocSystemRoleEnum role) {
    return userRepository.getUsersByRoleWithDepartment(role);
  }

  @Override
  public Long createUser(User user) {
    userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
      throw new UsernameExistedException();
    });
    userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
      throw new EmailExistedException();
    });

    String password = generateRandomPassword();
    user.setPassword(passwordEncoder.encode(password));
    Long id = userRepository.save(user).getId();

    // send email to user with new password
    emailService.sendPasswordEmail(user.getEmail(), user.getUsername(),  user.getFullName(), password, true);

    return id;
  }

  @Override
  public Long updateUser(User user) {
    User persistedUser = getUserById(user.getId());
    if (!persistedUser.getUsername().equals(user.getUsername())) {
      userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
        throw new UsernameExistedException();
      });
    }
    if (!persistedUser.getEmail().equals(user.getEmail())) {
      userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
        throw new EmailExistedException();
      });
    }

    return userRepository.save(user).getId();
  }

  @Override
  public Long updateCurrentUserPassword(@Valid ChangePasswordDto changePasswordDto) {

    User user = getCurrentUserFromDB();
    if (changePasswordDto.getOldPassword().equals(changePasswordDto.getNewPassword())) {
      throw new UserPasswordException(PASSWORD_NOT_CHANGED);
    }

    if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
      throw new UserPasswordException();
    }

    if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
      throw new UserPasswordException(PASSWORD_CONFIRMATION_INVALID);
    }

    user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
    return userRepository.save(user).getId();
  }

  @Override
  public DocPaginationDto<UserDto> search(UserSearchCriteria userSearchCriteria, int page,
      int pageSize) {
    long totalElements = userRepository.getTotalElements(userSearchCriteria);
    long totalPages = (totalElements / pageSize) + (totalElements % pageSize == 0 ? 0 : 1);
    List<UserDto> users = userRepository
        .searchByCriteria(userSearchCriteria, page, pageSize)
        .stream()
        .map(userMapper::toDto)
        .toList();

    return paginationMapper.toDto(users, totalElements, totalPages);
  }

  @Override
  public void deleteUsers(List<Long> userIds) {
    userRepository.deleteAllById(userIds);
  }

  @Override
  @Transactional
  public List<TransferHistoryDto> getTransferHistoryByUser(
      TransferHistorySearchCriteriaDto criteriaDto, int offset, int limit) {
    Pageable pageable = PageRequest.of(offset, limit);
    return transferHistoryRepository.findAllByUserId(criteriaDto.getUserId(), pageable)
        .map(transferHistoryMapper::toDto).toList();
  }

  @Override
  public Long getUnreadTransferHistoryByUserId() {
    return (long) transferHistoryRepository.findByUserIdAndUnread(getCurrentUserFromDB().getId())
        .size();
  }

  @Override
  public void updateReadTransferHistory(Long transferHistoryId) {
    TransferHistory transferHistory = transferHistoryRepository.findById(transferHistoryId)
        .orElseThrow(() -> new TransferHistoryNotFoundException(TransferHistoryNotFoundException.TRANSFER_HISTORY_NOT_FOUND));
    transferHistory.setIsRead(true);
    transferHistoryRepository.saveAndFlush(transferHistory);
  }

  @Override
  public void updateAllReadTransferHistory() {
    List<TransferHistory> transferHistories = transferHistoryRepository.findByUserIdAndUnread(getCurrentUserFromDB().getId());
    transferHistories.forEach(transferHistory -> transferHistory.setIsRead(true));
    transferHistoryRepository.saveAll(transferHistories);
  }

  @Override
  public List<DocStatisticsDto> getStatistics(
      DocStatisticsSearchCriteriaDto docStatisticsSearchCriteriaDto) {
    if (Objects.isNull(docStatisticsSearchCriteriaDto.getExpertIds()) && Objects
        .isNull(docStatisticsSearchCriteriaDto.getFromDate()) && Objects
        .isNull(docStatisticsSearchCriteriaDto.getToDate()) && Objects
        .isNull(docStatisticsSearchCriteriaDto.getDocType())) {
      User currentUser = SecurityUtils.getCurrentUser();
      docStatisticsSearchCriteriaDto.setExpertIds(List.of(currentUser.getId()));
      docStatisticsSearchCriteriaDto.setDocType(ProcessingDocumentType.INCOMING_DOCUMENT.value);
    }

    if (Objects.isNull(docStatisticsSearchCriteriaDto.getExpertIds())) {
      throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
    }

    if (ProcessingDocumentType.INCOMING_DOCUMENT.value.equals(
        docStatisticsSearchCriteriaDto.getDocType())
        || ProcessingDocumentType.OUTGOING_DOCUMENT.value.equals(
        docStatisticsSearchCriteriaDto.getDocType())) {

      List<DocStatisticsDto> docStatisticsDtos = new ArrayList<>();

      docStatisticsSearchCriteriaDto.getExpertIds().forEach(userId -> {
        DocListStatisticsDto docListStatisticsDto = processingDocumentRepository.getDocListStatistics(
            userId, docStatisticsSearchCriteriaDto.getFromDate(),
            docStatisticsSearchCriteriaDto.getToDate(),
            ProcessingDocumentType.valueOf(docStatisticsSearchCriteriaDto.getDocType()));
        if (Objects.isNull(docListStatisticsDto)) {
          docStatisticsDtos.add(
              new DocStatisticsDto(getUserById(userId).getUsername(), 0, 0, 0, 0));
        } else {
          docStatisticsDtos.add(quantityStatistics(docListStatisticsDto,
              ProcessingDocumentType.valueOf(docStatisticsSearchCriteriaDto.getDocType())));
        }
      });
      return docStatisticsDtos;
    } else {
      throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
    }
  }

  private DocStatisticsDto quantityStatistics(DocListStatisticsDto docListStatisticsDto,
      ProcessingDocumentType processingDocumentType) {
    List<Long> processedDocumentOnTime = new ArrayList<>();
    List<Long> processedDocumentOverdue = new ArrayList<>();
    List<Long> unprocessedDocumentUnexpired = new ArrayList<>();
    List<Long> unprocessedDocumentOverdue = new ArrayList<>();

    DocStatisticsDto docStatisticsDto = new DocStatisticsDto();
    docStatisticsDto.setUserName(getUserById(docListStatisticsDto.getUserId()).getUsername());

    if (docListStatisticsDto.getProcessedDocuments().isEmpty()) {
      docStatisticsDto.setNumberOfProcessedDocumentOnTime(0);
      docStatisticsDto.setNumberOfProcessedDocumentOverdue(0);
    } else {
      docListStatisticsDto.getProcessedDocuments().forEach(docId -> {
        List<ProcessingUser> processingUser = processingUserRepository.findByUserIdAndProcessingDocumentIdWithRole(
            docListStatisticsDto.getUserId(), docId);
        if (Objects.isNull(processingUser.get(0).getProcessingDuration())) {
          processedDocumentOnTime.add(docId);
        } else {
          ProcessingUserRole processingUserRole = processingUserRoleRepository.findByProcessingUserId(
              processingUser.get(1).getId());
          if (processingUserRole.getCreatedDate().toLocalDate()
              .isBefore(processingUser.get(0).getProcessingDuration())) {
            processedDocumentOnTime.add(docId);
          } else {
            processedDocumentOverdue.add(docId);
          }
        }
      });
    }

    docListStatisticsDto.getUnprocessedDocuments().forEach(docId -> {
      ProcessingDocument processingDocument = processingDocumentRepository
          .findProcessingDocumentById(docId)
          .orElseThrow(ProcessingDocumentNotFoundException::new);
      List<ProcessingUser> processingUser = processingUserRepository.findByUserIdAndProcessingDocumentIdWithRole(
          docListStatisticsDto.getUserId(), docId);
      if (processingUser.isEmpty()) {
        throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
      }

      if (ProcessingDocumentType.INCOMING_DOCUMENT.equals(processingDocumentType)) {
        if (ProcessingStatus.CLOSED.equals(processingDocument.getStatus()) && getUserById(
            docListStatisticsDto.getUserId()).getUsername()
            .equals(processingDocument.getIncomingDoc().getCloseUsername())) {
          if (Objects.isNull(processingUser.get(0).getProcessingDuration())) {
            processedDocumentOnTime.add(docId);
          } else {
            if (Objects.requireNonNull(processingDocument.getIncomingDoc().getCloseDate())
                .isBefore(processingUser.get(0).getProcessingDuration())) {
              processedDocumentOnTime.add(docId);
            } else {
              processedDocumentOverdue.add(docId);
            }
          }
        } else {
          if (Objects.isNull(processingUser.get(0).getProcessingDuration())) {
            unprocessedDocumentUnexpired.add(docId);
          } else {
            if (LocalDate.now().isBefore(processingUser.get(0).getProcessingDuration())) {
              unprocessedDocumentUnexpired.add(docId);
            } else {
              unprocessedDocumentOverdue.add(docId);
            }
          }
        }
      } else {
        if (OutgoingDocumentStatusEnum.RELEASED.equals(processingDocument.getOutgoingDocument().getStatus()) && getUserById(
            docListStatisticsDto.getUserId()).getUsername().equals(processingDocument.getOutgoingDocument().getSigner())) {
          if (Objects.isNull(processingUser.get(0).getProcessingDuration())) {
            processedDocumentOnTime.add(docId);
          } else {
            if (Objects.requireNonNull(processingDocument.getOutgoingDocument().getReleaseDate())
                .isBefore(processingUser.get(0).getProcessingDuration())) {
              processedDocumentOnTime.add(docId);
            } else {
              processedDocumentOverdue.add(docId);
            }
          }
        } else {
          if (Objects.isNull(processingUser.get(0).getProcessingDuration())) {
            unprocessedDocumentUnexpired.add(docId);
          } else {
            if (LocalDate.now().isBefore(processingUser.get(0).getProcessingDuration())) {
              unprocessedDocumentUnexpired.add(docId);
            } else {
              unprocessedDocumentOverdue.add(docId);
            }
          }
        }
      }
    });

    docStatisticsDto.setNumberOfProcessedDocumentOnTime(processedDocumentOnTime.size());
    docStatisticsDto.setNumberOfProcessedDocumentOverdue(processedDocumentOverdue.size());
    docStatisticsDto.setNumberOfUnprocessedDocumentUnexpired(unprocessedDocumentUnexpired.size());
    docStatisticsDto.setNumberOfUnprocessedDocumentOverdue(unprocessedDocumentOverdue.size());

    return docStatisticsDto;
  }

  @Override
  public Long resetUserPasswordById(Long userId) {
    User user = getUserById(userId);
    String password = generateRandomPassword();
    user.setPassword(passwordEncoder.encode(password));

    // send email
    emailService.sendPasswordEmail(user.getEmail(), user.getUsername(), user.getFullName(), password, false);
    return userRepository.save(user).getId();
  }

  @Override
  public Long resetUserPasswordByEmail(String email) {
    User user = getUserByEmail(email);
    String password = generateRandomPassword();
    user.setPassword(passwordEncoder.encode(password));

    // send email
    emailService.sendPasswordEmail(user.getEmail(), user.getUsername(), user.getFullName(), password, false);
    return userRepository.save(user).getId();
  }
}
