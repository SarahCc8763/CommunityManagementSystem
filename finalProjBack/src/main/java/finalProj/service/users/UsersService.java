package finalProj.service.users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import finalProj.domain.users.RolesUsers;
import finalProj.domain.users.Users;
import finalProj.dto.users.UsersDTO;
import finalProj.repository.users.RolesUsersRepository;
import finalProj.repository.users.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private RolesUsersRepository rolesUsersRepository;

	// 檢查是有權限的帳號
	public boolean isValidUser(String email) {
		Optional<Users> optionalUser = usersRepository.findByEmail(email);

		// email 存在
		if (optionalUser.isPresent()) {
			Users user = optionalUser.get();

			// 取得該 user 所有的 role 關聯
			List<RolesUsers> rolesUsersList = rolesUsersRepository.findByUser(user);

			// 判斷是否有任何一個角色的 id 不等於 3
			boolean hasValidRole = rolesUsersList.stream().anyMatch(ru -> ru.getRole().getRolesId() != 3);

			return hasValidRole;
		}
		// email 不存在回傳 false
		return false;
	}

	// 設定user密碼
	public void resetPassword(String email, String password) {
		Optional<Users> optionalUser = usersRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			Users bean = optionalUser.get();
			bean.setPassword(passwordEncoder.encode(password));
			bean.setLastAlterAt(LocalDateTime.now());
			usersRepository.save(bean);
			// 上一行可以不寫但加上@Transactional
			System.out.println("密碼更新成功");
		} else {
			System.out.println("User not found");
		}
	}

	// 首次登入的密碼為未加密，設定新密碼後加密
	public Boolean firstTimeChangePassword(String email, String oldPass, String newPass) {
		// isValid user && oldPass.equals("P@ssw0rd") && valid new password
		if (isValidUser(email) && oldPass.equals("P@ssw0rd") && newPass != null && newPass.length() != 0) {
			resetPassword(email, newPass);
			System.out.println("首次密碼更新成功");
			return true;
		}
		return false;
	}

	public Users login(String email, String password) {
		if (isValidUser(email) && password != null && password.length() != 0) {
			Optional<Users> optionalUser = usersRepository.findByEmail(email);
			if (optionalUser.isPresent()) {
				Users bean = optionalUser.get();
				// if (bean.getPassword().equals(password)) {
				if (passwordEncoder.matches(password, bean.getPassword())) {
					System.out.println("登入成功");
					return bean;
				}
			}
		}
		System.out.println("登入失敗");
		return null;
	}

	public Users findById(Integer id) {
		return usersRepository.findById(id).orElse(null);
	}

	// super user
	// @Autowired
	// private SuperUserProperties superUserProps;
	//
	// public boolean isSuperUser(String email, String password) {
	// return superUserProps.getEmail().equals(email)
	// && superUserProps.getPassword().equals(password);
	// }

	// tescher's
	// public Boolean changePassword(String email, String oldPass, String newPass) {
	// if (newPass != null && newPass.length() != 0) {
	// Users bean = this.login(email, oldPass);
	// if (bean != null) {
	// bean.setPassword(newPass);
	// Users update = usersRepository.save(bean);
	// if (update != null) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	public List<UsersDTO> findAll() {
		List<Users> data = usersRepository.findAll();
	
		return data.stream()
			.map(user -> {
				UsersDTO dto = new UsersDTO();
				dto.setUsersId(user.getUsersId());
				dto.setName(user.getName());
				return dto;
			})
			.collect(Collectors.toList());
	}
}
