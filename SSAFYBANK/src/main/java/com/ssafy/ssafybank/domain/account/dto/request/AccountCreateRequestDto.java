package com.ssafy.ssafybank.domain.account.dto.request;

import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.accountHolder.repository.AccountHolderRepository;
import com.ssafy.ssafybank.domain.bank.entity.Bank;
import com.ssafy.ssafybank.domain.bank.repository.BankRepository;
import com.ssafy.ssafybank.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountCreateRequestDto {
    @NotNull
    @NotEmpty
    private String accountHolderUuid;
    @NotNull
    private Integer bankCode;
    @NotNull
    private String accountPassword;

    private BankRepository bankRepository;
    private AccountHolderRepository accountHolderRepository;

    public Account toAccountEntity(Member member, AccountHolder accountHolder, Bank bank) {
        String accountNum = generateAccountNum();
        return Account.builder()
                .createdDate(LocalDateTime.now())
                .accountPassword(this.accountPassword)
                .accountHolderId(accountHolder)
                .bankId(bank)
                .balance(1000000L)
                .memberId(member)
                .accountNum(accountNum)
                .account_status(false)
                .build();
    }

    // 계좌 번호
    private String generateAccountNum() {
        StringBuilder accountNumBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 13; i++) {
            int digit = random.nextInt(10); // 0부터 9까지 랜덤
            accountNumBuilder.append(digit);
        }
        return accountNumBuilder.toString();
    }
}

