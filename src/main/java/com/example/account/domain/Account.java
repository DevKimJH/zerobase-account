package com.example.account.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue // Account Table의 PK를 Id로 지정해주겠다는 의미
    private Long id;


    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;


}
