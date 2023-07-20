package dev.hub.security.domain.api;

import com.auth0.jwt.interfaces.Payload;
import dev.hub.security.domain.jwt.JwtTokenProvider;
import dev.hub.security.entity.RoleType;
import dev.hub.security.entity.StatusRoles;
import dev.hub.security.entity.UserRoles;
import dev.hub.security.entity.repo.UserRolesRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SecurityController {

    private final UserRolesRepository userRolesRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityController(UserRolesRepository userRolesRepository, JwtTokenProvider jwtTokenProvider, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRolesRepository = userRolesRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    /**
     * 신규 가입시 GUEST 권한, 승인전 상태값 부여, 토큰 발행전
     * @param user
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity<?> createAuth(@RequestBody UserRoles user){
        try {
            UserRoles userRoles = UserRoles.builder()
                    .userId(user.getUserId())
                    .passWord(bCryptPasswordEncoder.encode(user.getPassWord()))
                    .roleType(RoleType.GUEST)
                    .statusRoles(StatusRoles.JOIN)
                    .build();
            userRolesRepository.save(userRoles);
            return ResponseEntity.ok(userRoles);
        } catch (DataIntegrityViolationException e) {
            // 유니크 키 제약조건 위반 예외 처리
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("이미 사용 중인 사용자 ID입니다.");
        }
    }


    /**
     * 관리자 권한 부여, 토큰 발행, 승인처리 상태값 변경
     * @param userRoles
     * @return
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> approveUserAuth(@RequestBody UserRoles userRoles){
        Optional<UserRoles> optionalUserRoles = userRolesRepository.findByUserId(userRoles.getUserId());

        if (optionalUserRoles.isPresent()) {
            // 엔티티가 존재하는 경우 상태를 업데이트합니다.
            UserRoles getUserRoles = null;
            try {
                getUserRoles = optionalUserRoles.get();
                getUserRoles.setRoleType(userRoles.getRoleType());
                getUserRoles.setStatusRoles(StatusRoles.ALLOW);

                //JWT PASSWORD 필수값 입력 처리
                userRoles.setPassWord(getUserRoles.getPassWord());
                String token = jwtTokenProvider.createNewToken(userRoles);
                Payload tokenInfo = jwtTokenProvider.getTokenInfo(token);

                // TODO: 2023-07-20 만기일자 정책 필요
                //Date expiration = tokenInfo.getExpiresAt();
                //Instant instant = expiration.toInstant();
                //LocalDateTime expireDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                //getUserRoles.setExpireDate(LocalDate.from(expireDate));

                //만기일자, 토큰생성, 업데이트 일자 입력
                getUserRoles.setExpireDate(LocalDate.parse("2030-12-25"));
                getUserRoles.setApiKeyToken(token);
                getUserRoles.setUpdateDateTime(LocalDateTime.now());

                //update 처리
                userRolesRepository.save(getUserRoles);
                return ResponseEntity.ok(getUserRoles);
            } catch (DataIntegrityViolationException e) {
                Throwable cause = e.getCause();
                cause.printStackTrace();
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Fail to Approved");
            }
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User not found with userId: " + userRoles.getUserId());
        }
    }

    /**
     * 만기시 권한 인증 및 갱신 처리
     * @param header
     * @param userRoles
     */
    @PostMapping("/authenticate")
    public void executeAuth(@RequestHeader HttpHeaders header, @RequestBody UserRoles userRoles) {
        //권한인증 및 갱신 처리
        // TODO: 2023-07-20 private keyToken 정책필요
    }

}
