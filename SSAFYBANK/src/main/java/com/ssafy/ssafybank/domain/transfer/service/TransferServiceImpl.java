package com.ssafy.ssafybank.domain.transfer.service;

import com.ssafy.ssafybank.domain.account.dto.response.PageInfo;
import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.account.repository.AccountRepository;
import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.domain.transfer.dto.request.GetTransferListReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDelete;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDepositReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.GetTransferListRespDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.TransferDepositRespDto;
import com.ssafy.ssafybank.domain.transfer.entity.Transfer;
import com.ssafy.ssafybank.domain.transfer.repository.TransferRepository;
import com.ssafy.ssafybank.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            Account senderAccount = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(transferDepositReqDto.getSenderAccountNum());
            //입금 계좌
            Account recAccount = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(transferDepositReqDto.getReceiverAccountNum());
            // 출금계좌와 입금계좌가 동일하면 안됨
            if(transferDepositReqDto.getSenderAccountNum().equals(transferDepositReqDto.getReceiverAccountNum())){
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
            System.out.println("ww" + transferDepositReqDto.getReceiverBankCode() );
            if(recAccount.getBankId().getBankCode() != transferDepositReqDto.getReceiverBankCode() ){
                throw new CustomApiException("은행이 일치하지 않습니다.");
            }
            //비밀번호 확인
            if(!senderAccount.getAccountPassword().equals(transferDepositReqDto.getSenderAccountPassword())){
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
            transferRepository.save(transferDepositReqDto.toTransferEntity(Uuid, senderAccount, recAccount, senderAccount.getBalance() , recAccount.getBalance()));

            String reqName = recAccount.getAccountHolderId().getAccountHolderName();
            Long balance = transferDepositReqDto.getDepositAmount();
            TransferDepositRespDto transferDepositRespDto = new TransferDepositRespDto(reqName , balance );
            return transferDepositRespDto;
        } else {

            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }

    }
    @Transactional
    @Override
    public List<GetTransferListRespDto> getTransferList(Pageable fixedPageable, GetTransferListReqDto getTransferListReqDto, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Account account = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(getTransferListReqDto.getAccountNum());
            if(account == null){
                throw new CustomApiException("계좌번호가 잘못되었습니다.");
            }
            if(!account.getAccountPassword().equals(getTransferListReqDto.getAccountPassword())){
                throw new CustomApiException("비밀번호가 일치하지 않습니다.");
            }
            Page<Transfer> transferList = transferRepository.findTransfersByDepositAccountIdOrWithdrawAccountId(account, fixedPageable);
            List<GetTransferListRespDto> getTransferListRespDtos = new ArrayList<>();
            for(Transfer transfer : transferList){
                //출금자가 본인일때
                if(transfer.getWithdrawAccountId().getAccountNum().equals(getTransferListReqDto.getAccountNum())){
                    GetTransferListRespDto getTransferListRespDto = GetTransferListRespDto
                            .builder()
                            .transferDate(transfer.getCreatedDate())
                            .afterAmount(transfer.getWithdrawAccountBalance())
                            .flag("출금")
                            .content(transfer.getWithdrawAccountContent())
                            .amount(transfer.getAmount())
                            .transferUuid(transfer.getTransferUuid())
                            .build();

                    getTransferListRespDtos.add(getTransferListRespDto);
                }else{ //입금자가 본인일 때
                    GetTransferListRespDto getTransferListRespDto = GetTransferListRespDto
                            .builder()
                            .transferDate(transfer.getCreatedDate())
                            .afterAmount(transfer.getDepositAccountBalance())
                            .flag("입금")
                            .content(transfer.getDepositAccountContent())
                            .amount(transfer.getAmount())
                            .transferUuid(transfer.getTransferUuid())
                            .build();

                    getTransferListRespDtos.add(getTransferListRespDto);
                }
            }
            return getTransferListRespDtos;
        } else {
            //멤버가 없다는 것은 accessToken정보가 잘못 되었다는 것
            //예외 종류 별로 code를 정해서 줘야할듯??
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }
    @Transactional
    @Override
    public PageInfo getPage(Pageable fixedPageable, String memberUuid, GetTransferListReqDto getTransferListReqDto) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Account account = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(getTransferListReqDto.getAccountNum());
            if(account == null){
                throw new CustomApiException("계좌번호가 잘못되었습니다.");
            }
            if(!account.getAccountPassword().equals(getTransferListReqDto.getAccountPassword())){
                throw new CustomApiException("비밀번호가 일치하지 않습니다.");
            }
            Page<Transfer> transferList = transferRepository.findTransfersByDepositAccountIdOrWithdrawAccountId(account, fixedPageable);
            int totalCnt = transferRepository.countByDepositAccountIdOrWithdrawAccountId(account);
            boolean isNext = !transferList.isLast();

            return new PageInfo(isNext, totalCnt);
        }
        throw new CustomApiException("accessToken정보가 잘못되었습니다.");
    }
    @Transactional
    @Override
    public Boolean deleteTransfer(TransferDelete transferDelete, String memberUuid) {
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Transfer transfer = transferRepository.findTransfersByTransferUuid(transferDelete.getTransferUuid());
            if (transfer == null) {
                throw new CustomApiException("계좌가 존재하지 않습니다.");
            }
            transferRepository.delete(transfer);
            return true;
        }
        throw new CustomApiException("accessToken정보가 잘못되었습니다.");
    }
}
