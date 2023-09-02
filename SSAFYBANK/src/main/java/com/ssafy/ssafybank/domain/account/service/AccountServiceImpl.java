package com.ssafy.ssafybank.domain.account.service;

import com.ssafy.ssafybank.domain.account.dto.request.AccountCreateRequestDto;
import com.ssafy.ssafybank.domain.account.repository.AccountRepository;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.accountHolder.repository.AccountHolderRepository;
import com.ssafy.ssafybank.domain.bank.entity.Bank;
import com.ssafy.ssafybank.domain.bank.repository.BankRepository;
import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
}
