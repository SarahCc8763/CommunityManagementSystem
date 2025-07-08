package finalProj.dto.finance;

import lombok.Data;

@Data
public class UserSimpleDTO {
    private Integer usersId;
    private String name;
    private String email;
    private String contactInfo;

    // 加上這個建構子即可讓 InvoiceDTO 可以 new UserSimpleDTO(users)
    public UserSimpleDTO(finalProj.domain.users.Users user) {
        this.usersId = user.getUsersId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.contactInfo = user.getContactInfo();
    }

    public UserSimpleDTO() {
    }
}
