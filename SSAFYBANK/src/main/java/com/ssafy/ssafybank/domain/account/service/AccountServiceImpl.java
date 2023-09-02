package com.ssafy.ssafybank.domain.account.service;

import com.ssafy.ssafybank.domain.account.dto.request.AccountCreateRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountDeleteRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountGetPasswordReqDto;
import com.ssafy.ssafybank.domain.account.dto.response.AccountGetPasswordRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.GetAccountRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.PageInfo;
import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.account.repository.AccountRepository;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.accountHolder.repository.AccountHolderRepository;
import com.ssafy.ssafybank.domain.bank.entity.Bank;
import com.ssafy.ssafybank.domain.bank.repository.BankRepository;
import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final MemberRepository memberRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public Boolean createAccount(AccountCreateRequestDto accountCreateRequestDto, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Integer bankCode = accountCreateRequestDto.getBankCode();
            Bank bank = bankRepository.findByBankCode(bankCode);
            if(bank == null){
                throw new CustomApiException("은행 코드가 잘못되었습니다.");
            }
            String accountHolderUuid = accountCreateRequestDto.getAccountHolderUuid();
            AccountHolder accountHolder = accountHolderRepository.findByAccountHolderUuid(accountHolderUuid);
            if(accountHolder == null){
                throw new CustomApiException("예금주가 잘못되었습니다.");
            }
            Integer accountPassword = accountCreateRequestDto.getAccountPassword();
            if (Integer.toString(accountPassword).length() != 4) {
                throw new CustomApiException("비밀번호는 4글자만 가능합니다.");
            }
            accountRepository.save(accountCreateRequestDto.toAccountEntity(member, accountHolder, bank));
            return true;
        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }
    @Transactional
    @Override
    public AccountGetPasswordRespDto getPassword(AccountGetPasswordReqDto accountGetPasswordReqDto, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            AccountHolder accountHolder = accountHolderRepository.findByAccountHolderUuid(accountGetPasswordReqDto.getAccountHolderUuid());
            if(accountHolder == null){
                throw new CustomApiException("예금주가 잘못되었습니다.");
            }
            Account account = accountRepository.findAccountByAccountNumAndAccountHolderId(accountGetPasswordReqDto.getAccountNum(), accountHolder);
            if(account == null){
                throw new CustomApiException("예금주 정보와 계좌번호가 일치하지 않습니다");
            }
            AccountGetPasswordRespDto accountGetPasswordRespDto = new AccountGetPasswordRespDto(account.getAccountPassword() , account.getAccountNum());
            return  accountGetPasswordRespDto;
        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }

    }

    @Transactional
    @Override
    public List<GetAccountRespDto> getAccountList(Pageable page, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
        Page<Account> accountList = accountRepository.findAccountsByMemberId(member,page);
        List<GetAccountRespDto> getAccountRespDtos = new ArrayList<>();
        for(Account account : accountList){
            String accountHolderName = account.getAccountHolderId().getAccountHolderName();
            String accountNum = account.getAccountNum();
            String bankName = account.getBankId().getBankName();
            Long balance = account.getBalance();

            GetAccountRespDto getAccountRespDto = GetAccountRespDto
                    .builder()
                    .accountHolderName(accountHolderName)
                    .accountNum(accountNum)
                    .bankName(bankName)
                    .balance(balance)
                    .build();
        getAccountRespDtos.add(getAccountRespDto);
        }
        return getAccountRespDtos;
        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }

    }

    @Override
    public PageInfo getPageInfo(Pageable fixedPageable, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Page<Account> accountList = accountRepository.findAccountsByMemberId(member, fixedPageable);
            int totalCnt = accountRepository.countByMemberId(member);
            boolean isNext = !accountList.isLast();

            return new PageInfo(isNext, totalCnt);
        }
        throw new CustomApiException("accessToken정보가 잘못되었습니다.");
    }

    @Override
    public List<GetAccountRespDto> getHolderAccountList(Pageable fixedPageable, String memberUuid, String accountHolderUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            AccountHolder accountHolder = accountHolderRepository.findByAccountHolderUuid(accountHolderUuid);
            if(accountHolder == null){
                throw new CustomApiException("예금주 키 정보가 잘못되었습니다.");
            }
            Page<Account> accountList = accountRepository.findAccountsByAccountHolderId(accountHolder, fixedPageable);
            List<GetAccountRespDto> getAccountRespDtos = new ArrayList<>();
            for(Account account : accountList){
                String accountHolderName = account.getAccountHolderId().getAccountHolderName();
                String accountNum = account.getAccountNum();
                String bankName = account.getBankId().getBankName();
                Long balance = account.getBalance();

                GetAccountRespDto getAccountRespDto = GetAccountRespDto
                        .builder()
                        .accountHolderName(accountHolderName)
                        .accountNum(accountNum)
                        .bankName(bankName)
                        .balance(balance)
                        .build();
                getAccountRespDtos.add(getAccountRespDto);
            }
            return getAccountRespDtos;
        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }

    @Override
    public PageInfo getPageInfoHolder(Pageable fixedPageable, String memberUuid, String accountHolderUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            AccountHolder accountHolder = accountHolderRepository.findByAccountHolderUuid(accountHolderUuid);
            if(accountHolder == null){
                throw new CustomApiException("예금주 키 정보가 잘못되었습니다.");
            }
            Page<Account> accountList = accountRepository.findAccountsByAccountHolderId(accountHolder, fixedPageable);
            int totalCnt = accountRepository.countByAccountHolderId(accountHolder);
            boolean isNext = !accountList.isLast();

            return new PageInfo(isNext, totalCnt);
        }
        throw new CustomApiException("accessToken정보가 잘못되었습니다.");
    }

    @Transactional
    @Override
    public Boolean deleteAccount(AccountDeleteRequestDto accountDeleteRequestDto, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String accountHolderUuid = accountDeleteRequestDto.getAccountHolderUuid();
            Integer bankCode = accountDeleteRequestDto.getBankCode();
            String accountNum = accountDeleteRequestDto.getAccountNum();
            Integer accountPassword = accountDeleteRequestDto.getAccountPassword();

            Bank bank = bankRepository.findByBankCode(bankCode);
            if (bank == null) {
                throw new CustomApiException("은행 정보를 찾을 수 없습니다.");
            }

            AccountHolder accountHolder = accountHolderRepository.findByAccountHolderUuid(accountHolderUuid);
            if (accountHolder == null) {
                throw new CustomApiException("예금주 정보를 찾을 수 없습니다.");
            }

            Account account = accountRepository.findAccountByAccountNumAndAccountHolderId(accountNum, accountHolder);

            if (account != null) {
                // 비밀번호 확인
                if (account.getAccountPassword().equals(accountPassword)) {
                    // 비밀번호 일치하면 계좌 삭제
                    accountRepository.delete(account);
                    return true;
                } else {
                    throw new CustomApiException("비밀번호가 일치하지 않습니다.");
                }
            } else {
                throw new CustomApiException("계좌 정보를 찾을 수 없습니다.");
            }
        } else {
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }
}
