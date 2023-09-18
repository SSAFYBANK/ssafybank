package com.ssafy.ssafybank.domain.accountHolder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.account.repository.AccountRepository;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderDelete;
import com.ssafy.ssafybank.domain.accountHolder.dto.response.AccountHolderListRespDto;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.accountHolder.repository.AccountHolderRepository;
import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.global.ex.CustomApiException;

import lombok.RequiredArgsConstructor;
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountHolderServiceImpl implements AccountHolderService {
	private final MemberRepository memberRepository;
	private final AccountHolderRepository accountHolderRepository;
	private final AccountRepository accountRepository;

	@Transactional
	@Override
	public Boolean createAccountHolder(AccountHolderCreate accountHolderCreate, String memberUuid) {
		log.info("AccountHolderServiceImpl_createAccountHolder -> 새로운 예금주 생성");
		Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);

		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			String accountUuid = UUID.randomUUID().toString();
			int cnt = accountHolderRepository.countAccountHolderByMemberIdAndAccountHolderStatusIsFalse(member);
			if (cnt >= 3) {
				log.error("AccountHolderServiceImpl_createAccountHolder -> 예금주는 최대 3명 만들 수 있습니다.");
				throw new CustomApiException("예금주는 최대 3명 만들 수 있습니다.");
			}
			accountHolderRepository.save(accountHolderCreate.toAccountHolderEntity(member, accountUuid));
			log.info("AccountHolderServiceImpl_createAccountHolder -> 새로운 예금주 생성 성공한 사용자 정보: {}" , memberUuid);
			return true;
		} else {
			log.error("AccountHolderServiceImpl_createAccountHolder -> accessToken 정보가 잘못되었습니다.");
			throw new CustomApiException("accessToken정보가 잘못되었습니다.");
		}
	}

	@Transactional
	@Override
	public List<AccountHolderListRespDto> getAccountHolderList(String memberUuid) {
		log.info("AccountHolderServiceImpl_getAccountHolderList -> 예금주 리스트 조회");
		Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			List<AccountHolderListRespDto> accountHolderRespList = new ArrayList<>();
			Optional<List<AccountHolder>> accountHoldersOptional = accountHolderRepository.findAccountHoldersByMemberIdAndAccountHolderStatusIsFalse(
				member);
			if (accountHoldersOptional.isPresent()) {
				List<AccountHolder> accountHolders = accountHoldersOptional.get();
				for (AccountHolder accountHolder : accountHolders) {
					AccountHolderListRespDto respDto = new AccountHolderListRespDto(
						accountHolder.getAccountHolderName(),
						accountHolder.getAccountHolderUuid()
					);
					accountHolderRespList.add(respDto);
				}
				log.info("AccountHolderServiceImpl_getAccountHolderList -> 예금주 리스트 조회 성공한 사용자 정보: {}" , memberUuid);
				return accountHolderRespList;
			} else {
				return new ArrayList<>();
			}

		} else {
			log.error("AccountHolderServiceImpl_getAccountHolderList -> accessToken 정보가 잘못되었습니다.");
			throw new CustomApiException("accessToken정보가 잘못되었습니다.");
		}

	}

	@Transactional
	@Override
	public Boolean deleteAccountHolder(AccountHolderDelete accountHolderDelete, String memberUuid) {
		log.info("AccountHolderServiceImpl_deleteAccountHolder -> 예금주 삭제");
		Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);

		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			String accountName = accountHolderDelete.getAccountHolderName();
			String accountUuid = accountHolderDelete.getAccountHolderUuid();
			Optional<AccountHolder> accountHolderOptional = accountHolderRepository.findAccountHolderByMemberIdAndAccountHolderNameAndAccountHolderUuidAndAccountHolderStatusIsFalse(
				member, accountName, accountUuid);
			if (accountHolderOptional.isPresent()) {
				AccountHolder accountHolder = accountHolderOptional.get();
				List<Account> accounts = accountRepository.findAccountsByAccountHolderIdAndAccountStatusIsFalse(
					accountHolder);
				for (Account account : accounts) {
					account.deactivateAccount();
				}
				accountHolder.deactivateAccountHolder();
				log.info("AccountHolderServiceImpl_deleteAccountHolder -> 예금주 삭제 성공한 사용자 정보: {}" , memberUuid);
				return true;
			} else {
				throw new CustomApiException("예금주 토큰정보가 잘못되었습니다.");
			}
		} else {
			log.error("AccountHolderServiceImpl_deleteAccountHolder -> accessToken 정보가 잘못되었습니다.");
			throw new CustomApiException("accessToken정보가 잘못되었습니다.");
		}
	}
}
