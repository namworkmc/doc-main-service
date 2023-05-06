package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import lombok.Data;

@Data
public class UserSearchCriteria {

    private String username;
    private String email;
    private String fullName;
    private DocSystemRoleEnum role;
    private Long departmentId;
}
