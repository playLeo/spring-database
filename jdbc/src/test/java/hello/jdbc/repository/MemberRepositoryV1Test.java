package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;

class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepository ;

    @BeforeEach
    void beforEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPoolName(PASSWORD);

        memberRepository = new MemberRepositoryV1(dataSource);
    }


    @Test
    void crud() throws SQLException {
        Member member = new Member("member6", 10000);
        memberRepository.save(member);

        Member findMember = memberRepository.findById("member6");
        Assertions.assertThat(findMember).isEqualTo(member);

        memberRepository.update("member6", 20000);
        Member updateMember = memberRepository.findById("member6");
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

        memberRepository.delete("member6");
        Assertions.assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }


}