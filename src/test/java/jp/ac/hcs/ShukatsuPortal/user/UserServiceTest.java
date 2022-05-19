package jp.ac.hcs.ShukatsuPortal.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

	@Autowired
	UserService userService;

	@SpyBean
	UserRepository userRepository;

	@Test
	void GetUserlistメソッドの正常系テスト() {
		// 1.Ready
		// 2.Do
		UserEntity userEntity = userService.getUserlist();
		// 3.Assert
		assertNotNull(userEntity);
		// 4.Logs
		log.warn("[GetUserlistメソッドの正常系テスト]userEntity:" + userEntity.toString());
	}

	@Test
	void GetUserlistメソッドの異常系テスト() {
		// 1.Ready
		doThrow(new DataAccessResourceFailureException("")).when(userRepository).selectAll();
		// 2.Do
		UserEntity userEntity = userService.getUserlist();
		// 3.Assert
		assertNull(userEntity);
		// 4.Logs
		log.warn("[GetUserlistメソッドの異常系テスト]userEntity:" + userEntity);
	}

	@Test
	void Insertメソッドの正常系テスト() {
		// 1.Ready
		UserForm form = new UserForm();
		form.setUser_id("bbb@gmail.com");
		form.setPassword("pass");
		form.setUserName("神");
		form.setUserAuthority("担任");
		form.setBelongClass("S3A2");
		form.setStudentNumber(27);
		String user_id = "master@gmail.com";
		// 2.Do
		boolean result = userService.insert(form, user_id);
		// 3.Assert
		assertEquals(true, result);
		// 4.Logs
		log.warn("[testInsertメソッドの正常系テスト]result:" + result);
	}

	@Test
	void Insertメソッドの異常系テスト() {
		// 1.Ready
		UserForm form = new UserForm();
		form.setUser_id("bbb@gmail.com");
		form.setPassword("pass");
		form.setUserName("神");
		form.setUserAuthority("担任");
		form.setBelongClass("S3A2");
		form.setStudentNumber(27);
		// Mock
		doThrow(new DataAccessResourceFailureException("")).when(userRepository).insertOne(any());
		// 2.Do
		boolean result = userService.insert(form, "aaa@gmail.com");
		// 3.Assert
		assertEquals(false, result);
		// 4.Logs
		log.warn("[Insertメソッドの異常系テスト]result:" + result);
	}

	@Test
	void GetDetailメソッドの正常系テスト() {
		// 1.Ready
		String user_id = "bbb@gmail.com";
		// 2.Do
		UserFormForUpdate form = userService.getDetail(user_id);
		// 3.Assert
		assertNotNull(form);
		// 4.Logs
		log.warn("[GetDetailメソッドの正常系テスト]form:" + form);
	}

	@Test
	void Deleteメソッドの正常系テスト() {
		// 1.Ready
		String user_id = "sample@gmail.com";
		// 2.Do
		boolean result = userService.delete(user_id);
		// 3.Assert
		assertEquals(true, result);
		// 4.Logs
		log.warn("[Deleteメソッドの正常系テスト]result:" + result);
	}

	@Test
	void Deleteメソッドの異常系テスト() {
		// 1.Ready
		// Mock
		doThrow(new DataAccessResourceFailureException("")).when(userRepository).deleteOne(anyString());
		// 2.Do
		boolean result = userService.delete("sample@gmail.com");
		// 3.Assert
		assertEquals(false, result);
		// 4.Logs
		log.warn("[Deleteメソッドの異常系テスト]result:" + result);
	}

	@Test
	void Updateメソッドの正常系テスト1() {
		// 1.Ready
		UserFormForUpdate update = new UserFormForUpdate();
		update.setUser_id("bbb@gmail.com"
				);
		update.setPassword("password");
		update.setUserName("あ");
		update.setUserAuthority("担任");
		update.setBelongClass("SXAX");
		update.setStudentNumber(3);
		update.setUserStatus("VALID");
		String user_id = "sample@gmail.com";
		// 2.Do
		boolean result = userService.update(update, user_id);
		// 3.Assert
		assertEquals(true, result);
		// 4.Logs
		log.warn("[Updateメソッドの正常系テスト1]result:" + result);
	}

	@Test
	void Updateメソッドの正常系テスト2() {
		// 1.Ready
		UserFormForUpdate update = new UserFormForUpdate();
		update.setUser_id("bbb@gmail.com");
		update.setPassword("");
		update.setUserName("あ");
		update.setUserAuthority("担任");
		update.setBelongClass("SXAX");
		update.setStudentNumber(3);
		update.setUserStatus("VALID");
		String user_id = "sample@gmail.com";
		// 2.Do
		boolean result = userService.update(update, user_id);
		// 3.Assert
		assertEquals(true, result);
		// 4.Logs
		log.warn("[Updateメソッドの正常系テスト2]result:" + result);
	}

	@Test
	void Updateメソッドの異常系テスト() {
		// 1.Ready
		UserFormForUpdate update = new UserFormForUpdate();
		update.setUser_id("sample@gmail.com");
		update.setPassword("password");
		update.setUserName("あ");
		update.setUserAuthority("担任");
		update.setBelongClass("SXAX");
		update.setStudentNumber(3);
		update.setUserStatus("VALID");
		// Mock
		doThrow(new DataAccessResourceFailureException("")).when(userRepository).updateOneWithPassword(any());
		doThrow(new DataAccessResourceFailureException("")).when(userRepository).updateOne(any());
		// 2.Do
		boolean result = userService.update(update, "sample@gmail.com");
		// 3.Assert
		assertEquals(false, result);
		// 4.Logs
		log.warn("[Updateメソッドの異常系テスト]result:" + result);
	}

	@Test
	void FilteredSearchメソッドの正常系1() {
		// 1.Ready
		String userName = "神";
		// 2.Do
		UserEntity userEntity = userService.filteredSearch(userName);
		// 3.Assert
		assertNotNull(userEntity);
		// 4.Logs
		log.warn("[FilteredSearchメソッドの正常系1]userEntity:" + userEntity);
	}

	@Test
	void FilteredSearchメソッドの正常系2() {
		// 1.Ready
		String userName = "";
		// 2.Do
		UserEntity userEntity = userService.filteredSearch(userName);
		// 3.Assert
		assertNotNull(userEntity);
		// 4.Logs
		log.warn("[FilteredSearchメソッドの正常系2]userEntity:" + userEntity);
	}

	@Test
	void FilteredSearchメソッドの正常系3() {
		// 1.Ready
		String userName = null;
		// 2.Do
		UserEntity userEntity = userService.filteredSearch(userName);
		// 3.Assert
		assertNotNull(userEntity);
		// 4.Logs
		log.warn("[FilteredSearchメソッドの正常系3]userEntity:" + userEntity);
	}

	@Test
	void FilteredSearchメソッドの異常系() {
		// 1.Ready
		UserEntity userEntity;
		// Mock
		doThrow(new DataAccessResourceFailureException("")).when(userRepository).filteredSearch(anyString());
		doThrow(new DataAccessResourceFailureException("")).when(userRepository).selectAll();
		// 2.Do
		userEntity = userService.filteredSearch("神");
		// 3.Assert
		assertNull(userEntity);
		// 4.Logs
		log.warn("[FilteredSearchメソッドの異常系]userEntity:" + userEntity);
	}

	@Test
	void SelectOneメソッドの正常系() {
		// 1.Ready
		String user_id = "master@gmail.com";
		// 2.Do
		UserData userData = userService.selectOne(user_id);
		// 3.Assert
		assertNotNull(userData);
		// 4.Logs
		log.warn("[SelectOneメソッドの正常系]userData:" + userData);
	}

	@Test
	void UpdateOneメソッドの正常系() {
		// 1.Ready
		UserData userData = new UserData();
		Date date = new Date();
		userData.setUserName("あ");
		userData.setUserAuthority("STUDENT");
		userData.setBelongClass("S3A1");
		userData.setStudentNumber(0);
		userData.setUserStatus("VALID");
		userData.setUpDate(date);
		userData.setUpUser("master@gmail.com");
		userData.setPasswordErrorCount(0);
		userData.setUser_id("master@gmail.com");
		// 2.Do
		boolean result = userService.updateOne(userData);
		// 3.Assert
		assertTrue(result);
		// 4.Logs
		log.warn("[UpdateOneメソッドの正常系]result:" + result);
	}

}
