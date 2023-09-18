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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class TransferServiceImpl implements  TransferService{
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public TransferDepositRespDto createTransfer(TransferDepositReqDto transferDepositReqDto, String memberUuid) {
        log.info("TransferServiceImpl_createTransfer -> 계좌 이체");
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            //출금 계좌
            Account senderAccount = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(transferDepositReqDto.getSenderAccountNum());
            //입금 계좌
            Account recAccount = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(transferDepositReqDto.getReceiverAccountNum());
            // 출금계좌와 입금계좌가 동일하면 안됨
            if(transferDepositReqDto.getSenderAccountNum().equals(transferDepositReqDto.getReceiverAccountNum())){
                log.error("TransferServiceImpl_createTransfer -> 입 출금 계좌가 중복됩니다.");
                throw new CustomApiException("입 출금 계좌가 중복됩니다.");
            }
            // 출금계좌 확인
            if(senderAccount == null){
                log.error("TransferServiceImpl_createTransfer -> 출금 계좌번호가 잘못되었습니다.");
                throw new CustomApiException("출금 계좌번호가 잘못되었습니다.");
            }
            // 입금계좌 확인
            if(recAccount == null ){
                log.error("TransferServiceImpl_createTransfer -> 입금 계좌번호가 잘못되었습니다.");
                throw new CustomApiException("입금 계좌번호가 잘못되었습니다.");
            }
            //은행 확인
            System.out.println("ww" + recAccount.getBankId().getBankCode() );
            System.out.println("ww" + transferDepositReqDto.getReceiverBankCode() );
            if(recAccount.getBankId().getBankCode() != transferDepositReqDto.getReceiverBankCode() ){
                log.error("TransferServiceImpl_createTransfer -> 은행이 일치하지 않습니다.");
                throw new CustomApiException("은행이 일치하지 않습니다.");
            }
            //비밀번호 확인
            if(!senderAccount.getAccountPassword().equals(transferDepositReqDto.getSenderAccountPassword())){
                log.error("TransferServiceImpl_createTransfer -> 비밀번호가 일치하지 않습니다.");
                throw new CustomApiException("비밀번호가 일치하지 않습니다.");
            }
            //출금계좌 소유자랑 입금계좌 소유자가 로그인한 사용자것인지 확인
            if(senderAccount.getMemberId() != member){
                log.error("TransferServiceImpl_createTransfer -> 출금계좌 소유자와 accessToken이 일치하지 않습니다.");
                throw new CustomApiException("출금계좌 소유자와 accessToken이 일치하지 않습니다.");
            }
            if(recAccount.getMemberId() != member){
                log.error("TransferServiceImpl_createTransfer -> 입금계좌 소유자와 accessToken이 일치하지 않습니다.");
                throw new CustomApiException("입금계좌 소유자와 accessToken이 일치하지 않습니다.");
            }
            //출금계좌 잔액 확인
            if(senderAccount.getBalance() < transferDepositReqDto.getDepositAmount()){
                log.error("TransferServiceImpl_createTransfer -> 잔액이 부족합니다");
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
            log.info("TransferServiceImpl_createTransfer -> 계좌 이체 성공한 사용자 정보: {}" , memberUuid);
            return transferDepositRespDto;
        } else {
            log.error("TransferServiceImpl_createTransfer -> accessToken 정보가 잘못되었습니다.");
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }

    }
    @Transactional
    @Override
    public List<GetTransferListRespDto> getTransferList(Pageable fixedPageable, GetTransferListReqDto getTransferListReqDto, String memberUuid) {
        log.info("TransferServiceImpl_getTransferList -> 계좌 이체 내역 조회");
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Account account = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(getTransferListReqDto.getAccountNum());
            if(account == null){
                log.error("TransferServiceImpl_getTransferList -> 계좌번호가 일치하지 않습니다.");
                throw new CustomApiException("계좌번호가 잘못되었습니다.");
            }
            if(!account.getAccountPassword().equals(getTransferListReqDto.getAccountPassword())){
                log.error("TransferServiceImpl_getTransferList -> 비밀번호가 일치하지 않습니다.");
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
            log.info("TransferServiceImpl_getTransferList -> 계좌 이체 내역 조회 성공한 사용자 정보: {}" , memberUuid);
            return getTransferListRespDtos;
        } else {
            log.error("TransferServiceImpl_getTransferList -> accessToken 정보가 잘못되었습니다.");
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }
    }
    @Transactional
    @Override
    public PageInfo getPage(Pageable fixedPageable, String memberUuid, GetTransferListReqDto getTransferListReqDto) {
        log.info("TransferServiceImpl_getPage -> 계좌 이체 내역 조회");
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Account account = accountRepository.findAccountByAccountNumAndAccountStatusIsFalse(getTransferListReqDto.getAccountNum());
            if(account == null){
                log.error("TransferServiceImpl_getPage -> 계좌번호가 존재하지 않습니다.");
                throw new CustomApiException("계좌번호가 잘못되었습니다.");
            }
            if(!account.getAccountPassword().equals(getTransferListReqDto.getAccountPassword())){
                log.error("TransferServiceImpl_getPage -> 비밀번호가 일치하지 않습니다.");
                throw new CustomApiException("비밀번호가 일치하지 않습니다.");
            }
            Page<Transfer> transferList = transferRepository.findTransfersByDepositAccountIdOrWithdrawAccountId(account, fixedPageable);
            int totalCnt = transferRepository.countByDepositAccountIdOrWithdrawAccountId(account);
            boolean isNext = !transferList.isLast();
            log.info("TransferServiceImpl_getPage -> 계좌 이체 내역 조회 성공한 사용자 정보: {}" , memberUuid);
            return new PageInfo(isNext, totalCnt);
        }
        log.error("TransferServiceImpl_getPage -> accessToken 정보가 잘못되었습니다.");
        throw new CustomApiException("accessToken정보가 잘못되었습니다.");
    }
    @Transactional
    @Override
    public Boolean deleteTransfer(TransferDelete transferDelete, String memberUuid) {
        log.info("TransferServiceImpl_deleteTransfer -> 계좌 이체 삭제 조회");
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Transfer transfer = transferRepository.findTransfersByTransferUuid(transferDelete.getTransferUuid());
            if (transfer == null) {
                log.error("TransferServiceImpl_deleteTransfer -> 계좌가 존재하지 않습니다.");
                throw new CustomApiException("계좌가 존재하지 않습니다.");
            }
            transferRepository.delete(transfer);
            log.info("TransferServiceImpl_getPage -> 계좌 이체 내역 삭제 성공한 사용자 정보: {}" , memberUuid);
            return true;
        }
        log.error("TransferServiceImpl_deleteTransfer -> accessToken 정보가 잘못되었습니다.");
        throw new CustomApiException("accessToken정보가 잘못되었습니다.");
    }
}
