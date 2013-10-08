package com.logbookmanager.faces;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.logbookmanager.support.JSFMockHelper;

/**
 * @see https
 *      ://cwiki.apache.org/confluence/display/MFTEST/Configure+MyFaces+Test
 *      +using+Maven
 * @see https://cwiki.apache.org/confluence/display/MFTEST/Index
 * @author Peter
 */
@ActiveProfiles({ "mvc-integration-test" })
@RunWith(SpringJUnit4ClassRunner.class)
/*
 * not necessary as this is declared in the
 * AbstractTransactionalJUnit4SpringContextTests
 */
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
/*
 * not necessary as this is declared in the
 * AbstractTransactionalJUnit4SpringContextTests
 */
@ContextConfiguration(name = "lbm-test-dispatcher-servlet", locations = {
		"file:src/main/webapp/WEB-INF/spring/servlet-context.xml",
		"file:src/main/resources/com/logbookmanager/configuration/spring/app-root.xml"

})
@WebAppConfiguration(value = "src/main/webapp")
// not really necessary because these are the defaults
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginAndSecurityIntegrationTests extends AbstractJsfTestCase {

	private static final Logger logger = LoggerFactory.getLogger(LoginAndSecurityIntegrationTests.class.getName());

	@Autowired
	WebApplicationContext wac; // cached

	MockHttpSession session = new MockHttpSession();

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockMvc mockMvc;

	private final JSFMockHelper jsfMock = new JSFMockHelper();

	@Before
	public void setup() throws Exception {

		this.jsfMock.setUp();

		mockMvc = webAppContextSetup(wac).alwaysDo(print())
				.defaultRequest(get("/web/app").accept(MediaType.APPLICATION_FORM_URLENCODED))
				.addFilter(springSecurityFilterChain).build();

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		logger.debug("We're tearing it down!");
		this.jsfMock.tearDown();
	}

	@Test
	public void getLoginView() throws Exception {

		this.mockMvc.perform(get("/web/app/welcome").contextPath("/web").servletPath("/app"))
				.andExpect(status().isOk()).andExpect(view().name("app/welcome"));
	}

	@Test
	public void doLogin() throws Exception {

		this.mockMvc
				.perform(
						post("/web/app/doLogin").param("loginForm:username", "peterneil")
								.param("loginForm:password", "password").contextPath("/web").servletPath("/app"))
				.andExpect(status().is(302)).andExpect(redirectedUrl("/web/app/main"));
	}

	@Test
	public void doLoginWithInvalidCredentials() throws Exception {

		this.mockMvc
				.perform(
						post("/web/app/doLogin").param("loginForm:username", "loginerrorusername")
								.param("loginForm:password", "password").contextPath("/web").servletPath("/app"))
				.andExpect(status().is(302)).andExpect(redirectedUrl("/web/app/welcome?login_error=1"));
	}

	@Test
	public void getUnauthenticatedMainViewReturnsWelcomeUrl() throws Exception {

		MvcResult result = this.mockMvc.perform(get("/web/app/main").contextPath("/web").servletPath("/app"))
				.andExpect(status().is(302)).andReturn();
		org.junit.Assert.assertEquals("http://localhost/web/app/welcome", result.getResponse().getRedirectedUrl());
		assertNotNull("Result must not be null", result);
	}

	@Test
	/**
	 * @see https://cwiki.apache.org/confluence/display/MFTEST/Configure+MyFaces+Test+using+Maven
	 * @see https://cwiki.apache.org/confluence/display/MFTEST/Index
	 * @throws Exception
	 */
	public void getAuthenticatedMainViewReturnsNoRedirect() throws Exception {

		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_LOGBOOKUSER");
		Authentication authToken = new UsernamePasswordAuthenticationToken("peterneil", "password", authorities);
		SecurityContext sc = new SecurityContextImpl();
		sc.setAuthentication(authToken);
		session.setAttribute(
				org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				sc);

		MvcResult result = this.mockMvc
				.perform(
						get("/web/app/main").session(session).principal(authToken).contextPath("/web")
								.servletPath("/app")).andExpect(status().is(200)).andReturn();
		org.junit.Assert.assertNull("There should be no redirect url", result.getResponse().getRedirectedUrl());
	}

	@Test
	/**
	 * Test src/main/resources/com/logbookmanager/configuration/spring/app-security.xml
	 * and make sure that the user is redirected to the welcome page if they do NOT have the correct ROLE_ADMIN authority  
	 * @throws Exception
	 */
	public void getAuthenticatedLogbookAdminViewWithInvalidRoles() throws Exception {

		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_LOGBOOKUSER");
		Authentication authToken = new UsernamePasswordAuthenticationToken("peterneil", "password", authorities);
		SecurityContext sc = new SecurityContextImpl();
		sc.setAuthentication(authToken);

		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

		this.mockMvc
				.perform(
						get("/web/app/admin/logbook").contextPath("/web").servletPath("/app").session(session)
								.principal(authToken)).andExpect(status().is(403)).andExpect(redirectedUrl(null));
	}

}
