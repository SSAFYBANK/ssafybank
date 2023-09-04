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

    public Account toAccountEntity(Member member, AccountHolder accountHolder, Bank bank, int dbValue) {
        String accountNum = generateAccountNum(dbValue);
        return Account.builder()
                .createdDate(LocalDateTime.now())
                .accountPassword(this.accountPassword)
                .accountHolderId(accountHolder)
                .bankId(bank)
                .balance(1000000L)
                .memberId(member)
                .accountNum(accountNum)
                .accountStatus(false)
                .build();
    }

    // 계좌 번호
    private static String generateAccountNum(int dbValue) {
        StringBuilder accountNumBuilder = new StringBuilder("369"); // 맨 앞에 369를 붙임

        // 난수 4자리를 생성
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10); // 0부터 9까지 랜덤
            accountNumBuilder.append(digit);
        }

        int incrementedValue = dbValue + 1; // DB에서 가져온 값에 1을 더함
        String incrementedValueStr = String.valueOf(incrementedValue); // 정수를 문자열로 변환

        // 4자리를 채우기 위해 필요한 0의 개수 계산
        int numberOfZerosNeeded = 4 - incrementedValueStr.length();

        // 필요한 0을 추가
        for (int i = 0; i < numberOfZerosNeeded; i++) {
            accountNumBuilder.append("0");
        }

        // DB에서 가져온 값 + 1을 추가
        accountNumBuilder.append(incrementedValueStr);

        // 마지막 1자리 계산 (12자리 숫자의 각 자릿수를 더한 후 10으로 나눈 값)
        int sum = 0;
        for (int i = 0; i < accountNumBuilder.length(); i++) {
            sum += Character.getNumericValue(accountNumBuilder.charAt(i));
        }
        int lastDigit = sum % 10;

        // 마지막 자리를 붙임
        accountNumBuilder.append(lastDigit);

        return accountNumBuilder.toString();
    }
}
