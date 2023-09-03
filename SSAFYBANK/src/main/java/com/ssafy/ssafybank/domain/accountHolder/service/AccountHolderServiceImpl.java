package com.ssafy.ssafybank.domain.accountHolder.service;

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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountHolderServiceImpl implements AccountHolderService {
    private final MemberRepository memberRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;
    @Transactional
    @Override
    public Boolean createAccountHolder(AccountHolderCreate accountHolderCreate, String memberUuid) {

        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);


        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String accountUuid = UUID.randomUUID().toString();
            int cnt = accountHolderRepository.countAccountHolderByMemberIdAndAccountHolderStatusIsFalse(member);
            if(cnt >= 3){
                throw new CustomApiException("예금주는 최대 3명 만들 수 있습니다.");
            }
            accountHolderRepository.save(accountHolderCreate.toAccountHolderEntity(member, accountUuid));

            return true; // Account holder creation successful
        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }

    @Transactional
    @Override
    public List<AccountHolderListRespDto> getAccountHolderList(String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            List<AccountHolderListRespDto> accountHolderRespList = new ArrayList<>();
            Optional<List<AccountHolder>> accountHoldersOptional = accountHolderRepository.findAccountHoldersByMemberIdAndAccountHolderStatusIsFalse(member);
            if(accountHoldersOptional.isPresent()){
                List<AccountHolder> accountHolders = accountHoldersOptional.get();
                for(AccountHolder accountHolder : accountHolders) {
                    AccountHolderListRespDto respDto = new AccountHolderListRespDto(
                            accountHolder.getAccountHolderName(),
                            accountHolder.getAccountHolderUuid()
                    );
                    accountHolderRespList.add(respDto);
                }
                return accountHolderRespList;
            } else {
                return new ArrayList<>();  // 빈 리스트를 반환
            }

        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }


    }
    @Transactional
    @Override
    public Boolean deleteAccountHolder(AccountHolderDelete accountHolderDelete, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);


        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String accountName = accountHolderDelete.getAccountHolderName();
            String accountUuid = accountHolderDelete.getAccountHolderToken();
            Optional<AccountHolder> accountHolderOptional = accountHolderRepository.findAccountHolderByMemberIdAndAccountHolderNameAndAccountHolderUuidAndAccountHolderStatusIsFalse(member , accountName , accountUuid);
            if(accountHolderOptional.isPresent()){
                AccountHolder accountHolder = accountHolderOptional.get();
                List<Account> accounts = accountRepository.findAccountsByAccountHolderIdAndAccountStatusIsFalse(accountHolder);
                for (Account account : accounts) {
                    account.deactivateAccount();
                }
                accountHolder.deactivateAccountHolder();// Account holder deletion


                return true; // Account
            }
            else {
                throw new CustomApiException("예금주 토큰정보가 잘못되었습니다.");
            }

        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }
}
