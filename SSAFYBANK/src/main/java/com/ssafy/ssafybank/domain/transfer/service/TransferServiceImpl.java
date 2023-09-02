package com.ssafy.ssafybank.domain.transfer.service;

import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.account.repository.AccountRepository;
import com.ssafy.ssafybank.domain.accountHolder.dto.response.AccountHolderListRespDto;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.accountHolder.repository.AccountHolderRepository;
import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDepositReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.TransferDepositRespDto;
import com.ssafy.ssafybank.domain.transfer.repository.TransferRepository;
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
public class TransferServiceImpl implements  TransferService{
    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public TransferDepositRespDto createTransfer(TransferDepositReqDto transferDepositReqDto, String memberUuid) {

        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            //출금 계좌
            Account senderAccount = accountRepository.findAccountByAccountNum(transferDepositReqDto.getSenderAccountNum());
            //입금 계좌
            Account recAccount = accountRepository.findAccountByAccountNum(transferDepositReqDto.getRecAccountNum());
            // 출금계좌와 입금계좌가 동일하면 안됨
            if(transferDepositReqDto.getSenderAccountNum().equals(transferDepositReqDto.getRecAccountNum())){
                throw new CustomApiException("입 출금 계좌가 중복됩니다.");
            }
            // 출금계좌 확인
            if(senderAccount == null){
                throw new CustomApiException("출금 계좌번호가 잘못되었습니다.");
            }
            // 입금계좌 확인
            if(recAccount == null ){
                throw new CustomApiException("입금 계좌번호가 잘못되었습니다.");
            }
            //은행 확인
            System.out.println("ww" + recAccount.getBankId().getBankCode() );
            System.out.println("ww" + transferDepositReqDto.getRecBankCode() );
            if(recAccount.getBankId().getBankCode() != transferDepositReqDto.getRecBankCode()){
                throw new CustomApiException("은행이 일치하지 않습니다.");
            }
            //비밀번호 확인
            if(!senderAccount.getAccountPassword().equals(transferDepositReqDto.getSenderAccountPass())){
                throw new CustomApiException("비밀번호가 일치하지 않습니다.");
            }
            //출금계좌 소유자랑 입금계좌 소유자가 로그인한 사용자것인지 확인
            if(senderAccount.getMemberId() != member){
                throw new CustomApiException("출금계좌 소유자와 accessToken이 일치하지 않습니다.");
            }
            if(recAccount.getMemberId() != member){
                throw new CustomApiException("입금계좌 소유자와 accessToken이 일치하지 않습니다.");
            }
            //출금계좌 잔액 확인
            if(senderAccount.getBalance() < transferDepositReqDto.getDepositAmount() ){
                throw new CustomApiException("잔액이 부족합니다");
            }

            //이체하기(계좌 잔액 조정)
            senderAccount.withdraw(transferDepositReqDto.getDepositAmount());
            recAccount.deposit(transferDepositReqDto.getDepositAmount());
            //거래내역 남기기
            String Uuid = UUID.randomUUID().toString();
            transferRepository.save(transferDepositReqDto.toTransferEntity(Uuid, senderAccount, recAccount, senderAccount.getBalance() , recAccount.getBalance() ));

            String reqName = recAccount.getAccountHolderId().getAccountHolderName();
            Long balance = transferDepositReqDto.getDepositAmount();
            TransferDepositRespDto transferDepositRespDto = new TransferDepositRespDto(reqName , balance );
            return transferDepositRespDto;
        } else {

            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }

    }
}
