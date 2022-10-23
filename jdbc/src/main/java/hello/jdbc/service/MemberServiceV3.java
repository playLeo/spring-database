package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */

@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3 {

    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepositoryV3;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            //비지니스 로직
            Member fromMember = memberRepositoryV3.findById(fromId);
            Member toMember = memberRepositoryV3.findById(toId);

            memberRepositoryV3.update(fromId, fromMember.getMoney() - money);
            // 트랜잭션을 사용하지 않고 오토커밋을 사용할 경우 예외를 발생시키는 예제
            if (toMember.getMemberId().equals("ex")) {
                throw new IllegalStateException("이체중 예외 발생");
            }
            memberRepositoryV3.update(toId, toMember.getMoney() + money);
            transactionManager.commit(status);

        }catch(Exception e){
            transactionManager.rollback(status);
            new IllegalStateException("예외가 발생 롤백진행", e);

        }

    }
}
