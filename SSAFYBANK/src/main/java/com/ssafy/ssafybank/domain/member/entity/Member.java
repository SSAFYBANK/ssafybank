package com.ssafy.ssafybank.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class Member {
	@Id
	@GeneratedValue
	private Long memberId;

	@Column(nullable = false)
	private String memberUuid;

	@Column(nullable = false)
	private String kakaoId;

	@Column(nullable = false, length = 60) // 패스워드 인코딩(BCrypt) 카카오아이디 로그인이어도 패스워드는 필요함
	private String password;

	@Column(nullable = false)
	private String role; // ADMIN, CUSTOMER

	@Column(nullable = false)
	private Boolean status = false;
	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;
	@Builder
	public Member(Long memberId, String memberUuid, String kakaoId, String password, String role, Boolean status, LocalDateTime createdDate) {
		this.memberId = memberId;
		this.memberUuid = memberUuid;
		this.kakaoId = kakaoId;
		this.password = password;
		this.role = role;
		this.status = status;
		this.createdDate = createdDate;
	}
}
