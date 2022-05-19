package jp.ac.hcs.ShukatsuPortal.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositoryTest {
	@SpyBean
	UserRepository userRepository;

	@Test
	void SelectAllの正常系テスト() {
		// 1.Ready
		// 2.Do
		UserEntity userEntity = userRepository.selectAll();
		// 3.Assert
		assertNotNull(userEntity);
		// 4.Logs
		log.warn("[selectAllメソッドの正常系テスト]userEntity:" + userEntity);
	}

	@Test
	void SelectOneの正常系テスト() {
		// 1.Ready
		String user_id = "sample@gmail.com";
		// 2.Do
		UserData userData = userRepository.selectOne(user_id);
		// 3.Assert
		assertNotNull(userData);
		// 4.Logs
		log.warn("[selectOneメソッドの正常系テスト]userData:" + userData);
	}

	@Test
	void InsertOneの正常系テスト() {
		// 1.Ready
		UserData userData = new UserData();
		Date date = new Date();
		userData.setUser_id("sample@gmail.com");
		userData.setPassword("password");
		userData.setUserName("中野知歩");
		userData.setUserAuthority("STUDENT");
		userData.setBelongClass("S3A1");
		userData.setStudentNumber(24);
		userData.setRegiDate(date);
		userData.setRegiUser("master.gmail.com");
		// 2.Do
		int rowNumber = userRepository.insertOne(userData);
		// 3.Assert
		assertNotSame(rowNumber, 0);
		// 4.Logs
		log.warn("[insertOneメソッドの正常系テスト]rowNumber:" + rowNumber);
	}

	@Test
	void UpdateOneWithPasswordの正常系テスト() {
		// 1.Ready
		UserData userData = new UserData();
		Date date = new Date();
		userData.setUser_id("sample@gmail.com");
		userData.setPassword("password");
		userData.setUserName("中野知歩");
		userData.setUserAuthority("STUDENT");
		userData.setBelongClass("S3A1");
		userData.setStudentNumber(24);
		userData.setUserStatus("VALID");
		userData.setUpDate(date);
		userData.setPasswordErrorCount(0);
		userData.setUpUser("master@gmail.com");
		// 2.Do
		int rowNumber = userRepository.updateOneWithPassword(userData);
		// 3.Assert
		assertEquals(rowNumber, 0);
		// 4.Logs
		log.warn("[UpdateOneWithPasswordの正常系テスト]rowNumber:" + rowNumber);
	}

	@Test
	void UpdateOneの正常系テスト() {
		// 1.Ready
		UserData userData = new UserData();
		Date date = new Date();
		userData.setUser_id("sample@gmail.com");
		userData.setUserName("中野知歩");
		userData.setUserAuthority("STUDENT");
		userData.setBelongClass("S3A1");
		userData.setStudentNumber(24);
		userData.setUserStatus("VALID");
		userData.setUpDate(date);
		userData.setPasswordErrorCount(0);
		userData.setUpUser("master@gmail.com");
		// 2.Do
		int rowNumber = userRepository.updateOne(userData);
		// 3.Assert
		assertEquals(rowNumber, 0);
		// 4.Logs
		log.warn("[UpdateOneの正常系テスト]rowNumber:" + rowNumber);
	}

	@Test
	void UpdateGeneralWithPasswordの正常系テスト() {
		// 1.Ready
		UserData userData = new UserData();
		userData.setUser_id("sample@gmail.com");
		userData.setPassword("password");
		userData.setUserName("中野知歩");
		userData.setDarkMode(false);
		// 2.Do
		int rowNumber = userRepository.updateGeneralWithPassword(userData);
		// 3.Assert
		assertEquals(rowNumber, 0);
		// 4.Logs
		log.warn("[UpdateGeneralWithPasswordの正常系テスト]rowNumber:" + rowNumber);
	}

	@Test
	void UpdateGeneralの正常系テスト() {
		// 1.Ready
		UserData userData = new UserData();
		userData.setUser_id("sample@gmail.com");
		userData.setUserName("中野知歩");
		userData.setDarkMode(false);
		// 2.Do
		int rowNumber = userRepository.updateGeneral(userData);
		// 3.Assert
		assertEquals(rowNumber, 1);
		// 4.Logs
		log.warn("[UpdateGeneralの正常系テスト]rowNumber:" + rowNumber);
	}

	@Test
	void DeleteOneの正常系テスト() {
		// 1.Ready
		String user_id = "sample@gmail.com";
		// 2.Do
		int rowNumber = userRepository.deleteOne(user_id);
		// 3.Assert
		assertNotEquals(rowNumber, 0);
		// 4.Logs
		log.warn("[DeleteOneの正常系テスト]rowNumber:" + rowNumber);
	}

	@Test
	void FilteredSearchの正常系テスト() {
		// 1.Ready
		String user_name = "中野知歩";
		// 2.Do
		UserEntity userEntity = userRepository.filteredSearch(user_name);
		// 3.Assert
		assertNotNull(userEntity);
		// 4.Logs
		log.warn("[FilteredSearchの正常系テスト]userEntity:" + userEntity);
	}
}
