package com.kakao.housingfinance.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.housingfinance.exception.ResourceNotFoundException;
import com.kakao.housingfinance.model.User;
import com.kakao.housingfinance.model.constant.DeviceType;
import com.kakao.housingfinance.model.token.EmailVerificationToken;
import com.kakao.housingfinance.repository.EmailVerificationTokenRepository;
import com.kakao.housingfinance.repository.RefreshTokenRepository;
import com.kakao.housingfinance.repository.UserDeviceRepository;
import com.kakao.housingfinance.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class TEST01_API_TEST_WITH_JWT_Tests {
    private String resourceName = "vaildUploadFile.csv";
    private InputStream is;
    private MockMvc mockMvc;


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, Object> normalUserRegisterMap;
    private Map<String, Object> normalUserDeviceInfoMap;
    private Map<String, Object> normalUserLoginRequestMap;

    private Map<String, Object> adminUserRegisterMap;
    private Map<String, Object> adminUserDeviceInfoMap;
    private Map<String, Object> adminUserLoginRequestMap;

    //private Map<String, Object> normalUserLoginResponseMap;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private MvcResult registerUser(Map<String, Object> userRegisterMap) throws Exception {
        return mockMvc.perform(post("/api/auth/signup").contentType(contentType).content(mapper.writeValueAsString(userRegisterMap)))
                .andExpect(status().is(200)).andReturn();
    }

    private MvcResult confirmaRegisteredUser(String tokenStr) throws Exception {
        return mockMvc.perform(get("/api/auth/registrationConfirmation?token=" + tokenStr))
                .andExpect(status().is(200)).andReturn();
    }

    private MvcResult loginUser(Map<String, Object> userLoginRequestMap) throws Exception {
        return mockMvc.perform(post("/api/auth/signin").contentType(contentType).content(mapper.writeValueAsString(userLoginRequestMap)))
                .andExpect(status().is(200)).andReturn();
    }

    private MvcResult refreshUserToken(Map<String, Object> param) throws Exception {
        return mockMvc.perform(post("/api/auth/refresh").contentType(contentType).content(mapper.writeValueAsString(param)))
                .andExpect(status().is(200)).andReturn();
    }

    private MvcResult logoutUser(Map<String, Object> param, Map<String, Object> userLogOutResponseMap) throws Exception{

        System.out.println("param : "+ param);
        System.out.println("userLogOutResponseMap : "+ userLogOutResponseMap);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(userLogOutResponseMap.get("tokenType"));
        stringBuilder.append(userLogOutResponseMap.get("accessToken"));

        return mockMvc.perform(post("/api/user/logout")
                .header("Authorization", stringBuilder.toString())
                .contentType(contentType).content(mapper.writeValueAsString(param)))
                .andExpect(status().is(200)).andReturn();
    }

    @Before
    public void init() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        String absolutePath = file.getAbsolutePath();
        is = new FileInputStream(new File(absolutePath));

        //일반 사용자 회원가입 기초정보 셋팅
        normalUserRegisterMap = new HashMap<String, Object>();
        normalUserRegisterMap.put("email", "iceflower01@gmail.com");
        normalUserRegisterMap.put("password", "qwe123");
        normalUserRegisterMap.put("registerAsAdmin", false);

        //일반 사용자 로그인 기초정보 셋팅
        normalUserLoginRequestMap = new HashMap<String, Object>();
        normalUserLoginRequestMap.put("email", "iceflower01@gmail.com");
        normalUserLoginRequestMap.put("password", "qwe123");
        normalUserDeviceInfoMap = new HashMap<>();
        normalUserDeviceInfoMap.put("deviceId", "deviceIdString"); // 원래대로라면 기기 자체의 토큰을 획득하여야 함
        normalUserDeviceInfoMap.put("deviceType", DeviceType.DEVICE_TYPE_ANDROID.getCode());
        normalUserLoginRequestMap.put("deviceInfo", normalUserDeviceInfoMap);


        //관리자 권한 사용자 회원가입 기초정보 셋팅
        adminUserRegisterMap = new HashMap<String, Object>();
        adminUserRegisterMap.put("email", "iceflower01.developer@gmail.com");
        adminUserRegisterMap.put("password", "qwe123");
        adminUserRegisterMap.put("registerAsAdmin", true);

        //관리자 권한 사용자 로그인 기초정보 셋팅
        adminUserLoginRequestMap = new HashMap<String, Object>();
        adminUserLoginRequestMap.put("email", "iceflower01.developer@gmail.com");
        adminUserLoginRequestMap.put("password", "qwe123");
        adminUserDeviceInfoMap = new HashMap<>();
        adminUserDeviceInfoMap.put("deviceId", "deviceIdString2"); // 원래대로라면 기기 자체의 토큰을 획득하여야 함
        adminUserDeviceInfoMap.put("deviceType", DeviceType.DEVICE_TYPE_IOS.getCode());
        adminUserLoginRequestMap.put("deviceInfo", adminUserDeviceInfoMap);

    }

    @Test
    public void test001_비로그인_성공_일반_계정_생성() throws Exception {
        //사용자 등록
        MvcResult resultOne = registerUser(normalUserRegisterMap);
        String contentOne = resultOne.getResponse().getContentAsString();

        System.out.println(contentOne);

        if (resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 등록 실패");


        Thread.sleep(1500);

        //사용자 인증
        Optional<User> user = userRepository.findByEmail((String) normalUserLoginRequestMap.get("email"));
        if (!user.isPresent()) Assert.fail("사용자 등록여부 확인 실패");


        Optional<EmailVerificationToken> token = emailVerificationTokenRepository.findById(user.get().getId());

        if (!token.isPresent()) Assert.fail("인증메일 토큰 발급여부 확인 실패");

        String tokenStr = token.get().getToken();
        MvcResult resultTwo = confirmaRegisteredUser(tokenStr);
        String contentTwo = resultTwo.getResponse().getContentAsString();
        System.out.println(contentTwo);

        if (resultTwo.getResponse().getStatus() != 200) Assert.fail("사용자 인증 실패");

        Assert.assertEquals(200, resultTwo.getResponse().getStatus()); // 일반 사용자 인증까지 완료 == 회원 신규생성 성공
    }
    @Test
    public void test002_비로그인_성공_관리자_계정_생성() throws Exception {
        //사용자 등록
        MvcResult resultOne = registerUser(adminUserRegisterMap);
        String contentOne = resultOne.getResponse().getContentAsString();

        System.out.println(contentOne);

        if (resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 등록 실패");

        Thread.sleep(1500);
        //사용자 인증
        Optional<User> user = userRepository.findByEmail((String) adminUserLoginRequestMap.get("email"));
        if (!user.isPresent()) Assert.fail("사용자 등록여부 확인 실패");


        Optional<EmailVerificationToken> token = emailVerificationTokenRepository.findById(user.get().getId());


        if (!token.isPresent()) Assert.fail("인증메일 토큰 발급여부 확인 실패");

        String tokenStr = token.get().getToken();
        MvcResult resultTwo = confirmaRegisteredUser(tokenStr);
        String contentTwo = resultTwo.getResponse().getContentAsString();
        System.out.println(contentTwo);

        if (resultTwo.getResponse().getStatus() != 200) Assert.fail("사용자 인증 실패");

        Assert.assertEquals(200, resultTwo.getResponse().getStatus()); // 관리자 사용자 인증까지 완료 == 회원 신규생성 성공
    }
    @Test
    public void test003_로그인_성공_토큰_리프레시() throws Exception {

        Optional<User> user = userRepository.findByEmail((String)normalUserLoginRequestMap.get("email"));
        if(!user.isPresent()) Assert.fail("사용자 등록여부 확인 실패");

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        Assert.assertNotNull(is);
        //사용자 로그인
        MvcResult resultOne = loginUser(adminUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", adminUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        param.put("refreshToken",userLoginResponseMap.get("refreshToken"));
        MvcResult result = refreshUserToken(param);
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
        Assert.assertEquals(200, result.getResponse().getStatus());
    }
    @Test
    public void test003_비로그인_실패_데이터_업로드()  {

        try {
            Assert.assertNotNull(is);

            MockMultipartFile mockMultipartFile = new MockMultipartFile("fileUpload", null, "multipart/form-data", is);
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/data/upload").file(mockMultipartFile)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);
            Assert.assertEquals(200, result.getResponse().getStatus());

        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test004_비로그인_실패_전체_금융기관_조회() {
        try{
            MvcResult result = mockMvc.perform(get("/api/bank/list/all"))
                    .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);

            Assert.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test005_비로그인_실패_개별_금융기관_조회() {
        try{
            MvcResult result = mockMvc.perform(get("/api/bank/info?bankName=신한은행"))
                    .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);

            Assert.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test006_비로그인_실패_존재하지_않는_개별_금융기관_조회(){
        try{
            MvcResult result = mockMvc.perform(get("/api/bank/info?bankName=기업은행"))
                    .andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);
            Assert.assertEquals(404, result.getResponse().getStatus());
        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test007_비로그인_실패_전체_연도별_신용보증_지원내역_조회하기(){
        try{
            MvcResult result = mockMvc.perform(get("/api/creditGuarantee/totalAmountByYear"))
                    .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);

            Assert.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test008_비로그인_실패_전체_금융기관_대상_최대_보증내역_조회(){
        try{
            MvcResult result = mockMvc.perform(get("/api/creditGuarantee/maxAmountOfBank"))
                    .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);

            Assert.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test009_비로그인_실패_개별_금웅기관_대상_최대_최소_보증금액_조회(){
        try{
            MvcResult result = mockMvc.perform(get("/api/creditGuarantee/supportAmountOfBank?bankName=국민은행"))
                    .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);

            Assert.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test010_비로그인_실패_존재하지_않는_개별_금융기관_대상_최대_최소_보증금액_조회(){
        try{
            MvcResult result = mockMvc.perform(get("/api/creditGuarantee/supportAmountOfBank?bankName=한국은행"))
                    .andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

            String content = result.getResponse().getContentAsString();
            System.out.println(content);

            Assert.assertEquals(404, result.getResponse().getStatus());
        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
    }
    @Test
    public void test011_일반계정_로그인_실패_데이터업로드_테스트()  {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();
        try{
            Assert.assertNotNull(is);
            //사용자 로그인
            MvcResult resultOne = loginUser(normalUserLoginRequestMap);
            String contentThree = resultOne.getResponse().getContentAsString();
            System.out.println(contentThree);
            if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

            userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

            param.put("deviceInfo", normalUserDeviceInfoMap);

            System.out.println("param : "+ param);
            System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(userLoginResponseMap.get("tokenType"));
            stringBuilder.append(userLoginResponseMap.get("accessToken"));

            //데이터 업로드
            MockMultipartFile mockMultipartFile = new MockMultipartFile("fileUpload", null, "multipart/form-data", is);
            MvcResult resultTwo = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/data/upload").file(mockMultipartFile)
                    .header("Authorization", stringBuilder.toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();


            String content = resultTwo.getResponse().getContentAsString();
            System.out.println(content);
            if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");
            //로그아웃
            MvcResult resultFour =logoutUser(param, userLoginResponseMap);
            String contentFour = resultFour.getResponse().getContentAsString();
            System.out.println(contentFour);
            Assert.assertEquals(200, resultFour.getResponse().getStatus());}
        catch (Exception e){
            try{
                logoutUser(param, userLoginResponseMap);
                Assert.assertTrue(e.getCause() instanceof AccessDeniedException);
            } catch(Exception ex) {}
        }
    }
    @Test
    public void test012_관리자계정_로그인_성공_데이터업로드_테스트() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        Assert.assertNotNull(is);
        //사용자 로그인
        MvcResult resultOne = loginUser(adminUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", adminUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileUpload", null, "multipart/form-data", is);
        MvcResult resultTwo = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/data/upload").file(mockMultipartFile)
                .header("Authorization", stringBuilder.toString())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");

        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test013_일반계정_로그인_성공_전체_금융기관_조회() throws Exception{
         Map<String, Object> param = new HashMap<>();
         Map<String, Object> userLoginResponseMap = new HashMap<>();

         //사용자 로그인
         MvcResult resultOne = loginUser(normalUserLoginRequestMap);
         String contentThree = resultOne.getResponse().getContentAsString();
         System.out.println(contentThree);
         if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

         userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

         param.put("deviceInfo", normalUserDeviceInfoMap);

         System.out.println("param : "+ param);
         System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

         StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append(userLoginResponseMap.get("tokenType"));
         stringBuilder.append(userLoginResponseMap.get("accessToken"));

        //조회
         MvcResult resultTwo = mockMvc.perform(get("/api/bank/list/all")
                 .header("Authorization", stringBuilder.toString()))
                 .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

         String content = resultTwo.getResponse().getContentAsString();
         System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");

        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test014_관리자계정_로그인_성공_전체_금융기관_조회() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        //사용자 로그인
        MvcResult resultOne = loginUser(adminUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", adminUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        //조회
        MvcResult resultTwo = mockMvc.perform(get("/api/bank/list/all")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");

        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test015_일반계정_로그인_성공_개별_금융기관_조회() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        //사용자 로그인
        MvcResult resultOne = loginUser(normalUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", normalUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        MvcResult resultTwo = mockMvc.perform(get("/api/bank/info?bankName=신한은행")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");

        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test016_관리자계정_로그인_성공_개별_금융기관_조회() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        //사용자 로그인
        MvcResult resultOne = loginUser(adminUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", adminUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        MvcResult resultTwo = mockMvc.perform(get("/api/bank/info?bankName=신한은행")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");

        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test018_관리자계정_로그인_실패_존재하지_않는_개별_금융기관_조회() {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();
        try{
            //사용자 로그인
            MvcResult resultOne = loginUser(adminUserLoginRequestMap);
            String contentThree = resultOne.getResponse().getContentAsString();
            System.out.println(contentThree);
            if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

            userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

            param.put("deviceInfo", adminUserDeviceInfoMap);

            System.out.println("param : "+ param);
            System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(userLoginResponseMap.get("tokenType"));
            stringBuilder.append(userLoginResponseMap.get("accessToken"));

            MvcResult resultTwo = mockMvc.perform(get("/api/bank/info?bankName=기업은행").header("Authorization", stringBuilder.toString()))
                    .andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

            String content = resultTwo.getResponse().getContentAsString();
            System.out.println(content);

        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof ResourceNotFoundException);
        }
        finally {
            try {
                //로그아웃
                logoutUser(param, userLoginResponseMap);
            } catch (Exception e) {}
        }
    }
    @Test
    public void test019_일반계정_로그인_성공_전체_연도별_신용보증_지원내역_조회하기() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        //사용자 로그인
        MvcResult resultOne = loginUser(normalUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", normalUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        //조회
        MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/totalAmountByYear")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);
        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");
        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test020_관리자계정_로그인_성공_전체_연도별_신용보증_지원내역_조회하기() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        //사용자 로그인
        MvcResult resultOne = loginUser(adminUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", adminUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/totalAmountByYear")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);
        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");
        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test021_일반계정_로그인_성공_전체_금융기관_대상_최대_보증내역_조회() throws Exception {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        //사용자 로그인
        MvcResult resultOne = loginUser(normalUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", normalUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));
        MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/maxAmountOfBank")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");
        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test022_관리자계정_로그인_성공_전체_금융기관_대상_최대_보증내역_조회() throws Exception {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();

        //사용자 로그인
        MvcResult resultOne = loginUser(adminUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", adminUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));
        MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/maxAmountOfBank")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");
        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test023_일반계정_로그인_성공_개별_금웅기관_대상_최대_최소_보증금액_조회() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();
        //사용자 로그인
        MvcResult resultOne = loginUser(normalUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", normalUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        //조회
        MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/supportAmountOfBank?bankName=국민은행")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");
        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test024_관리자계정_로그인_성공_개별_금웅기관_대상_최대_최소_보증금액_조회() throws Exception{
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();
        //사용자 로그인
        MvcResult resultOne = loginUser(adminUserLoginRequestMap);
        String contentThree = resultOne.getResponse().getContentAsString();
        System.out.println(contentThree);
        if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

        userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

        param.put("deviceInfo", adminUserDeviceInfoMap);

        System.out.println("param : "+ param);
        System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userLoginResponseMap.get("tokenType"));
        stringBuilder.append(userLoginResponseMap.get("accessToken"));

        //조회
        MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/supportAmountOfBank?bankName=국민은행")
                .header("Authorization", stringBuilder.toString()))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        String content = resultTwo.getResponse().getContentAsString();
        System.out.println(content);

        if(resultTwo.getResponse().getStatus() != 200) Assert.fail("테스트 시나리오 실패");
        //로그아웃
        MvcResult resultThree =logoutUser(param, userLoginResponseMap);
        String contentFour = resultThree.getResponse().getContentAsString();
        System.out.println(contentFour);
        Assert.assertEquals(200, resultThree.getResponse().getStatus());
    }
    @Test
    public void test025_일반계정_로그인_실패_존재하지_않는_개별_금융기관_대상_최대_최소_보증금액_조회(){
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();
        try{
            //사용자 로그인
            MvcResult resultOne = loginUser(normalUserLoginRequestMap);
            String contentThree = resultOne.getResponse().getContentAsString();
            System.out.println(contentThree);
            if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

            userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

            param.put("deviceInfo", normalUserDeviceInfoMap);

            System.out.println("param : "+ param);
            System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(userLoginResponseMap.get("tokenType"));
            stringBuilder.append(userLoginResponseMap.get("accessToken"));
            MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/supportAmountOfBank?bankName=한국은행")
                    .header("Authorization", stringBuilder.toString()))
                    .andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

            String content = resultTwo.getResponse().getContentAsString();
            System.out.println(content);

        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
        finally {
            try {
                //로그아웃
                logoutUser(param, userLoginResponseMap);
            } catch (Exception e) {}
        }
    }
    @Test
    public void test026_관리자계정_로그인_실패_존재하지_않는_개별_금융기관_대상_최대_최소_보증금액_조회(){
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> userLoginResponseMap = new HashMap<>();
        try{
            //사용자 로그인
            MvcResult resultOne = loginUser(adminUserLoginRequestMap);
            String contentThree = resultOne.getResponse().getContentAsString();
            System.out.println(contentThree);
            if(resultOne.getResponse().getStatus() != 200) Assert.fail("사용자 로그인 실패");

            userLoginResponseMap = mapper.readValue(contentThree, new TypeReference<Map<String, String>>(){});

            param.put("deviceInfo", adminUserDeviceInfoMap);

            System.out.println("param : "+ param);
            System.out.println("userLoginResponseMap : "+ userLoginResponseMap);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(userLoginResponseMap.get("tokenType"));
            stringBuilder.append(userLoginResponseMap.get("accessToken"));
            MvcResult resultTwo = mockMvc.perform(get("/api/creditGuarantee/supportAmountOfBank?bankName=한국은행")
                    .header("Authorization", stringBuilder.toString()))
                    .andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

            String content = resultTwo.getResponse().getContentAsString();
            System.out.println(content);

        } catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(e.getCause() instanceof Exception);
        }
        finally {
            try {
                //로그아웃
                logoutUser(param, userLoginResponseMap);
            } catch (Exception e) {}
        }
    }
}




