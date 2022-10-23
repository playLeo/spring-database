package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */

@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private  final DataSource dataSource;
    private final MemberRepositoryV2 memberRepositoryV2;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();

        try{
            con.setAutoCommit(false);
            //비지니스 로직
            Member fromMember = memberRepositoryV2.findById(con, fromId);
            Member toMember = memberRepositoryV2.findById(con, toId);

            memberRepositoryV2.update(con, fromId, fromMember.getMoney() - money);
            // 트랜잭션을 사용하지 않고 오토커밋을 사용할 경우 예외를 발생시키는 예제
            if (toMember.getMemberId().equals("ex")) {
                throw new IllegalStateException("이체중 예외 발생");
            }
            memberRepositoryV2.update(con, toId, toMember.getMoney() + money);
            con.commit();

        }catch(Exception e){
            con.rollback();
            new IllegalStateException("예외가 발생 롤백진행", e);

        }finally {
            if (con != null) {
                try{
                    con.setAutoCommit(true);
                    con.close();
                }
                catch(Exception e){
                    log.info("error", e);
                }
            }

        }

    }
}
