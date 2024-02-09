package yass.jouao.labx.serviceImpl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import yass.jouao.labx.DTOs.UserLabDTO;
import yass.jouao.labx.entities.UserLab;
import yass.jouao.labx.exeptions.NotFoundException;
import yass.jouao.labx.repositories.IUserLabRepository;
import yass.jouao.labx.serviceImpl.Mappers.UserMapper;
import yass.jouao.labx.services.IUserLabService;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
@AllArgsConstructor
public class UserLabServiceImpl implements IUserLabService {
	private UserMapper userMapper;
	private IUserLabRepository userLabRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserLabServiceImpl.class);


	@Override
	public List<UserLabDTO> getAllUserLabService() {
		List<UserLab> userLabs = userLabRepository.findAll();
		List<UserLabDTO> userLabDTOs = userLabs.stream().map(userlab -> userMapper.fromUserToUserDTO(userlab))
				.collect(Collectors.toList());
		return userLabDTOs;
	}

	@Override
	@Transactional
	public UserLabDTO getUserLabByIdService(Long id) throws NotFoundException {
		Optional<UserLab> optionalUserLab = userLabRepository.findById(id);
		if (optionalUserLab.isPresent()) {
			UserLabDTO userLabDTO = userMapper.fromUserToUserDTO(optionalUserLab.get());
			return userLabDTO;
		} else {
			throw new NotFoundException("User not found");
		}
	}

	@Override
	@Transactional
	public UserLabDTO updateUserLabService(Long userId, UserLabDTO u) throws NotFoundException, IllegalAccessException {
		UserLab userToUpdate = userMapper.fromUserDTOToUser(getUserLabByIdService(userId));
		u.setId(userId);
		UserLab userNewData = userMapper.fromUserDTOToUser(u);
		updateNonNullFields(userToUpdate, userNewData);
		UserLabDTO userLabDTO = userMapper.fromUserToUserDTO(userLabRepository.save(userToUpdate));
		return userLabDTO;
	}

	@Override
	@Transactional
	public UserLabDTO addUserLabService(UserLabDTO u) {
		try {
			LOGGER.info("Adding new user lab");
			UserLab userLab = userMapper.fromUserDTOToUser(u);
			String password = userLab.getPassword();
			userLab.setPassword(passwordEncoder.encode(password)); // Encode the password
			UserLab savedUserLab = userLabRepository.save(userLab);
			return userMapper.fromUserToUserDTO(savedUserLab);
		} catch (Exception e) {
			LOGGER.error("Error occurred while adding user lab", e);
			throw e;
		}
	}
//	public UserLabDTO addUserLabService(UserLabDTO u) {
//		UserLab userLab = userMapper.fromUserDTOToUser(u);
//		UserLabDTO userLabDTO = userMapper.fromUserToUserDTO(userLabRepository.save(userLab));
//		return userLabDTO;
//	}

	private void updateNonNullFields(UserLab existingEntity, UserLab updatedEntity) throws IllegalAccessException {
		Field[] fields = UserLab.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object updatedValue = field.get(updatedEntity);
			if (updatedValue != null) {
				field.set(existingEntity, updatedValue);
			}
		}
	}

}
