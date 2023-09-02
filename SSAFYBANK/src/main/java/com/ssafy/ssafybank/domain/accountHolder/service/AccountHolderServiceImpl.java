package com.ssafy.ssafybank.domain.accountHolder.service;

import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
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

@RequiredArgsConstructor
@Service
public class AccountHolderServiceImpl implements AccountHolderService {
    private final MemberRepository memberRepository;
    private final AccountHolderRepository accountHolderRepository;
    @Transactional
    @Override
    public Boolean createAccountHolder(AccountHolderCreate accountHolderCreate, String memberUuid) {

        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);


        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String accountName = accountHolderCreate.getAccountHolderName();

            accountHolderRepository.save(accountHolderCreate.toAccountHolderEntity(member));

            return true; // Account holder creation successful
        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }

    @Transactional
    @Override
    public AccountHolderListRespDto getAccountHolderList(String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            List<String> accountHolderList = new ArrayList<>();
            Optional<List<AccountHolder>> accountHoldersOptional = accountHolderRepository.findAccountHoldersByMemberId(member);
            if(accountHoldersOptional.isPresent()){
                List<AccountHolder> accountHolders = accountHoldersOptional.get();
                for(AccountHolder accountHolder : accountHolders) {
                    accountHolderList.add(accountHolder.getAccountHolderName());
                }
                return new AccountHolderListRespDto(accountHolderList);
            }else{
                return new AccountHolderListRespDto(new ArrayList<>());
            }

        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }


    }
}
