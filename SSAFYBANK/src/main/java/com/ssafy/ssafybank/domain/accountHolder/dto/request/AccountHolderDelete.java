package com.ssafy.ssafybank.domain.accountHolder.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountHolderDelete {

	@NotNull
	@Size(max = 20)
	private String accountHolderName;

	@NotNull
	@Size(max = 100)
	private String accountHolderUuid;

}
